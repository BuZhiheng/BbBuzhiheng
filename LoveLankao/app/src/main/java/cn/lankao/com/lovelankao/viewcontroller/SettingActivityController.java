package cn.lankao.com.lovelankao.viewcontroller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.SettingActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class SettingActivityController implements View.OnClickListener ,SettingActivity.SettingHolder{
    private SettingActivity context;
    private ImageView photo;
    private Uri imageFilePath;
    private ProgressDialog dialog;
    public SettingActivityController(SettingActivity context){
        this.context = context;
        x.view().inject(context);
        initView();
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        context.findViewById(R.id.btn_setting_zhuxiao).setOnClickListener(this);
        context.findViewById(R.id.btn_setting_exit).setOnClickListener(this);
        context.findViewById(R.id.iv_setting_back).setOnClickListener(this);
        photo = (ImageView) context.findViewById(R.id.iv_setting_photo);
        photo.setOnClickListener(this);
        x.image().bind(photo, PrefUtil.getString(CommonCode.SP_USER_PHOTO, CommonCode.APP_ICON), BitmapUtil.getOptionRadius());
    }
    private void saveBitmap(Bitmap bitmap){
        if (bitmap == null){
            return;
        }
        dialog.show();
        final String userId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
        String s = BitmapUtil.compressImage(context,bitmap);
        final BmobFile file = new BmobFile(new File(s));
        file.upload(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                final MyUser myUser = new MyUser();
                myUser.setPhoto(file);
                myUser.update(context,userId,new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        ToastUtil.show("上传成功");
                        PrefUtil.putString(CommonCode.SP_USER_PHOTO, file.getFileUrl(context));
                        x.image().bind(photo, file.getFileUrl(context), BitmapUtil.getOptionRadius());
                        myUser.setNickName(CommonCode.SP_USER_PHOTO);//user frm 界面更新头像
                        EventBus.getDefault().post(myUser);
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        dialog.dismiss();
                        ToastUtil.show(msg);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
                ToastUtil.show(s);
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_setting_photo:
                CharSequence[] items = {"相册", "相机"};
                new AlertDialog.Builder(context)
                        .setTitle("选择图片来源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    BitmapUtil.startPicture(context);
                                } else {
                                    imageFilePath = BitmapUtil.startCamera(context);
                                }
                            }
                        })
                        .create().show();
                break;
            case R.id.btn_setting_zhuxiao:
                MyUser user = new MyUser();
                user.setNickName("未登录");
                PrefUtil.clear();
                context.finish();
                EventBus.getDefault().post(user);
                break;
            case R.id.btn_setting_exit:
                MyUser userExit = new MyUser();
                //username设置包名,退出程序
                userExit.setNickName(context.getPackageName());
                context.finish();
                EventBus.getDefault().post(userExit);
                break;
            case R.id.iv_setting_back:
                context.finish();
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
