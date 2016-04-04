package cn.lankao.com.lovelankao.viewcontroller;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SquareSendActivity;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareSendActivityController implements View.OnClickListener, SquareSendActivity.SquareSendHolder {
    private SquareSendActivity context;
    private EditText etContent;
    private ImageView ivChoose;
    private String path = "";
    public SquareSendActivityController(SquareSendActivity context) {
        this.context = context;
        x.view().inject(context);
        initView();
    }

    private void initView() {
        etContent = (EditText) context.findViewById(R.id.et_square_send_content);
        ivChoose = (ImageView) context.findViewById(R.id.iv_square_choose_photo);
        context.findViewById(R.id.btn_square_send).setOnClickListener(this);
        ivChoose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_square_choose_photo:
                CharSequence[] items = {"相册", "相机"};
                new AlertDialog.Builder(context)
                        .setTitle("选择图片来源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("image/*");
                                    context.startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
                                } else {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    context.startActivityForResult(intent, 1);
                                }
                            }
                        })
                        .create().show();
                break;
            case R.id.btn_square_send:
                Square square = new Square();
                square.setNickName("Carry");
                square.setSquareContent(etContent.getText().toString());
//                square.setSquarePhoto(new BmobFile(new File(path)));
                square.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.show("发表成功");
                        context.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.show(s);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == context.RESULT_OK) {
            if (requestCode == 0){
                Uri uri = data.getData();
                ContentResolver cr = context.getContentResolver();
                Cursor c = cr.query(uri, null, null, null, null);
                c.moveToFirst();
                //这是获取的图片保存在sdcard中的位置
                path = c.getString(c.getColumnIndex("_data"));
                if (path != null){
                    x.image().bind(ivChoose,path);
                }
            }else{
                Bundle extras = data.getExtras();
                Bitmap b = (Bitmap) extras.get("data");
                ivChoose.setImageBitmap(b);
                String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                path = Environment.getExternalStorageDirectory().toString()+File.separator+"dong/image/"+name+".jpg";
            }
        }
    }
}
