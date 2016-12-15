package cn.lankao.com.lovelankao.viewcontroller;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.SquareSendActivity;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PermissionUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareSendActivityController implements View.OnClickListener, SquareSendActivity.SquareSendHolder {
    private SquareSendActivity context;
    private EditText etContent;
    private EditText etTitle;
    private ImageView ivChoose1;
    private ImageView ivChoose2;
    private ImageView ivChoose3;
    private ImageView ivChoose4;
    private ImageView ivChoose5;
    private int imgIndex;
    private Uri imageFilePath;
    private String[] pathArr;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private ProgressDialog dialog;
//    private final int REQUEST_EXTERNAL_STORAGE = 1;
//    private String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
//    };
    public SquareSendActivityController(SquareSendActivity context) {
        this.context = context;
        initView();
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        etContent = (EditText) context.findViewById(R.id.et_square_send_content);
        etTitle = (EditText) context.findViewById(R.id.et_square_send_title);
        ivChoose1 = (ImageView) context.findViewById(R.id.iv_square_choose_photo1);
        ivChoose2 = (ImageView) context.findViewById(R.id.iv_square_choose_photo2);
        ivChoose3 = (ImageView) context.findViewById(R.id.iv_square_choose_photo3);
        ivChoose4 = (ImageView) context.findViewById(R.id.iv_square_choose_photo4);
        ivChoose5 = (ImageView) context.findViewById(R.id.iv_square_choose_photo5);
        context.findViewById(R.id.btn_square_send).setOnClickListener(this);
        context.findViewById(R.id.iv_squaresend_back).setOnClickListener(this);
        ivChoose1.setOnClickListener(this);
        ivChoose2.setOnClickListener(this);
        ivChoose3.setOnClickListener(this);
        ivChoose4.setOnClickListener(this);
        ivChoose5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_square_choose_photo1:
                chooseImg(R.id.iv_square_choose_photo1);
                break;
            case R.id.iv_square_choose_photo2:
                chooseImg(R.id.iv_square_choose_photo2);
                break;
            case R.id.iv_square_choose_photo3:
                chooseImg(R.id.iv_square_choose_photo3);
                break;
            case R.id.iv_square_choose_photo4:
                chooseImg(R.id.iv_square_choose_photo4);
                break;
            case R.id.iv_square_choose_photo5:
                chooseImg(R.id.iv_square_choose_photo5);
                break;
            case R.id.btn_square_send:
                sendMsg();
                break;
            case R.id.iv_squaresend_back:
                context.finish();
                break;
            default:
                break;
        }
    }

    private void sendMsg() {
        if ("".equals(PrefUtil.getString(CommonCode.SP_USER_USERID, ""))){
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }else if("".equals(etTitle.getText().toString())){
            ToastUtil.show("请输入标题");
            return;
        } else if("".equals(etContent.getText().toString()) ||  etContent.getText().toString().length() < 10){
            ToastUtil.show("内容至少输入10个字");
            return;
        }
        dialog.show();
        new Thread(){
            public void run(){
                setPath();
                upLoading();
            }
        }.start();
    }
    private void upLoading(){
        final Square square = new Square();
        square.setNickName(PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "游客"));
        square.setUserPhoto(PrefUtil.getString(CommonCode.SP_USER_PHOTO,null));
        square.setSquareTitle(etTitle.getText().toString());
        square.setSquareContent(etContent.getText().toString());
        if (pathArr == null || pathArr.length == 0){
            square.save(new SaveListener() {
                @Override
                public void done(Object o, Object o2) {

                }

                @Override
                public void done(Object o, BmobException e) {
                    if (e == null){
                        dialog.dismiss();
                        ToastUtil.show("发表成功");
                        EventBus.getDefault().post(square);
                        context.finish();
                    } else {
                        ToastUtil.show(e.getMessage());
                    }
                }
            });
        } else {
//            Bmob.uploadBatch(context, pathArr, new UploadBatchListener() {
//                @Override
//                public void onSuccess(List<BmobFile> list, List<String> strs) {
//                    if (strs.size() == pathArr.length) {
//                        for (int i = 0; i < list.size(); i++) {
//                            if (i == 0) {
//                                square.setSquarePhoto1(list.get(i));
//                            } else if (i == 1) {
//                                square.setSquarePhoto2(list.get(i));
//                            } else if (i == 2) {
//                                square.setSquarePhoto3(list.get(i));
//                            } else if (i == 3) {
//                                square.setSquarePhoto4(list.get(i));
//                            } else if (i == 4) {
//                                square.setSquarePhoto5(list.get(i));
//                            }
//                        }
//                        square.save(new SaveListener() {
//                            @Override
//                            public void done(Object o, Object o2) {
//                            }
//                            @Override
//                            public void done(Object o, BmobException e) {
//                                if (e == null){
//                                    dialog.dismiss();
//                                    ToastUtil.show("发表成功");
//                                    EventBus.getDefault().post(square);
//                                    context.finish();
//                                } else {
//                                    ToastUtil.show("发表成功");
//                                }
//                            }
//
//                        });
//                    }
//                }
//            });
        }
    }
    private void chooseImg(int i){
        imgIndex = i;
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(context)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            BitmapUtil.startPicture(context);
                        } else {
                            checkCameraPermission();
                        }
                    }
                })
                .create().show();
    }
    public void checkCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context, permission)) {
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    ToastUtil.show("权限被关闭,请打开相机权限");
                    Intent intentSet = new Intent();
                    intentSet.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intentSet.setData(uri);
                    context.startActivity(intentSet);
                    return;
                }
            } else {
                imageFilePath = BitmapUtil.startCamera(context);
            }
        } else {
            imageFilePath = BitmapUtil.startCamera(context);
        }
    }
    private void setPath(){
        List<String> list = new ArrayList<>();
        if (bitmap1 != null){
            String s = BitmapUtil.compressImage(context,bitmap1);
            list.add(s);
        } if (bitmap2 != null){
            String s = BitmapUtil.compressImage(context,bitmap2);
            list.add(s);
        } if (bitmap3 != null){
            String s = BitmapUtil.compressImage(context,bitmap3);
            list.add(s);
        } if (bitmap4 != null){
            String s = BitmapUtil.compressImage(context,bitmap4);
            list.add(s);
        } if (bitmap5 != null){
            String s = BitmapUtil.compressImage(context,bitmap5);
            list.add(s);
        }
        if (list.size() > 0){
            pathArr = new String[list.size()];
            for (int i=0;i<list.size();i++){
                if (list.get(i) != null){
                    pathArr[i] = list.get(i);
                }
            }
        }
    }
    private void saveBitmap(Bitmap bitmap){
        switch (imgIndex){
            case R.id.iv_square_choose_photo1:
                bitmap1 = bitmap;
                ivChoose1.setImageBitmap(bitmap);
                break;
            case R.id.iv_square_choose_photo2:
                bitmap2 = bitmap;
                ivChoose2.setImageBitmap(bitmap);
                break;
            case R.id.iv_square_choose_photo3:
                bitmap3 = bitmap;
                ivChoose3.setImageBitmap(bitmap);
                break;
            case R.id.iv_square_choose_photo4:
                bitmap4 = bitmap;
                ivChoose4.setImageBitmap(bitmap);
                break;
            case R.id.iv_square_choose_photo5:
                bitmap5 = bitmap;
                ivChoose5.setImageBitmap(bitmap);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == context.RESULT_OK) {
            if (requestCode == BitmapUtil.PIC_PICTURE){//相册
                Bitmap b = BitmapUtil.getBitmapByPicture(context,data);
                saveBitmap(b);
            } else if (requestCode == BitmapUtil.PIC_CAMERA){//相机
                int dp = WindowUtils.px2dip(context, 1000);
                BitmapUtil.cropImage(context, imageFilePath, dp, dp);
            } else if (requestCode == BitmapUtil.PIC_CROP){//裁剪
                Bitmap b = BitmapUtil.getBitmapByCameraOrCrop(data);
                saveBitmap(b);
            }
        }
    }
}