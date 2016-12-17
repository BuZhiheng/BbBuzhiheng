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
import android.widget.ImageView;
import org.greenrobot.eventbus.EventBus;
import org.xutils.x;
import java.io.File;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SettingActivity;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PermissionUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
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
        x.image().bind(photo, PrefUtil.getString(CommonCode.SP_USER_PHOTO, CommonCode.APP_ICON), BitmapUtil.getOptionCommonRadius());
    }
    private void saveBitmap(String path){
        dialog.show();
        final String userId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
        final BmobFile file = new BmobFile(new File(path));
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                final MyUser myUser = new MyUser();
                myUser.setPhoto(file);
                myUser.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        dialog.dismiss();
                        ToastUtil.show("上传成功");
                        PrefUtil.putString(CommonCode.SP_USER_PHOTO, file.getFileUrl());
                        x.image().bind(photo, file.getFileUrl(), BitmapUtil.getOptionCommonRadius());
                        myUser.setNickName(CommonCode.SP_USER_PHOTO);//user frm 界面更新头像
                        EventBus.getDefault().post(myUser);
                    }
                });
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
                                    checkCameraPermission();
//                                    imageFilePath = BitmapUtil.startCamera(context);
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
    public void checkCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context,permission)) {
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
                BitmapUtil.startCamera(context);
            }
        } else {
            BitmapUtil.startCamera(context);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == context.RESULT_OK) {
            if (requestCode == BitmapUtil.PIC_PICTURE){//相册
                Bitmap b = BitmapUtil.getBitmapByPicture(context,data);
                String path = BitmapUtil.compressImage(context, b);
                if (TextUtil.isNull(path)){
                    ToastUtil.show("相册选取失败,请拍照上传");
                    return;
                }
                saveBitmap(path);
            } else if (requestCode == BitmapUtil.PIC_CAMERA){//相机
                if (data.getData() != null|| data.getExtras() != null){ //防止没有返回结果
                    Bitmap photo = BitmapUtil.getBitmapByPicture(context,data);
                    String path = BitmapUtil.compressImage(context, photo);
                    if (TextUtil.isNull(path)){
                        ToastUtil.show("拍照上传失败,请去相册选择");
                        return;
                    }
                    saveBitmap(path);
                }
            }
        }
    }
}