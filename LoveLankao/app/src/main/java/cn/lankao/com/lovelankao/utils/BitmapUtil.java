package cn.lankao.com.lovelankao.utils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.lankao.com.lovelankao.R;

/**
 * Created by BuZhiheng on 2016/4/5.
 * Desc 相机,图片处理
 *
 *  */
public class BitmapUtil {
    public static final int PIC_PICTURE = 0;
    public static final int PIC_CAMERA = 1;
    public static final int PIC_CROP = 2;
    public static Bitmap getBitmapByCameraOrCrop(Intent data){
        /**
         * 调起拍照或剪切图片功能之后
         * 在onActivityResult调用
         * 获取图片Bitmap
         * */
        Bitmap b;
        if (data != null){
            Bundle extras = data.getExtras();
            if (extras != null){
                b = (Bitmap) extras.get("data");
                return b;
            }
        }
        return null;
    }
    public static Bitmap getBitmapByPicture(AppCompatActivity context,Intent data){
        /**
         * 调起选取图片之后
         * 在onActivityResult调用
         * 获取图片Bitmap
         * */
//        Uri uri = data.getData();
//        ContentResolver cr = context.getContentResolver();
//        if (Build.VERSION.SDK_INT >= 19){
//            String id = uri.getLastPathSegment().split(":")[1];
//            final String[] imageColumns = {MediaStore.Images.Media.DATA};
//            Cursor c = cr.query(uri, imageColumns,
//                    MediaStore.Images.Media._ID + "=" + id, null, null);
//            if (c != null && c.moveToFirst()){
//                //这是获取的图片保存在sdcard中的位置
//                String tempPath = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                c.close();
//                if (tempPath != null){
//                    Bitmap b = BitmapFactory.decodeFile(tempPath);
//                    return b;
//                }
//            }
//        } else {
//            String[] projection = { MediaStore.MediaColumns.DATA };
//            Cursor c = cr.query(uri, projection, null, null, null);
//            if (c != null) {
//                int column_index = c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                c.moveToFirst();
//                String tempPath = c.getString(column_index);
//                c.close();
//                if (tempPath != null){
//                    Bitmap b = BitmapFactory.decodeFile(tempPath);
//                    return b;
//                }
//            }
//        }
//        return null;
        /**
         * 调起选取图片之后
         * 在onActivityResult调用
         * 获取图片Bitmap
         * */
        Uri uri = data.getData();
//        String u = uri.getPath();
//        u.replace("%3A",":");
//        uri = Uri.parse(u);
        ContentResolver cr = context.getContentResolver();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Cursor c = cr.query(uri, null, null, null, null);
//        if (c == null){
//            ToastUtil.show("图片路径有误,请选取本地图片");
//            return null;
//        }
//        c.moveToFirst();
//        //这是获取的图片保存在sdcard中的位置
//        String tempPath;
//        try{
//            tempPath = c.getString(c.getColumnIndex("_data"));
//        } catch (Exception e){
//            c.close();
//            return null;
//        }
//        if (tempPath != null){
//            Bitmap b = BitmapFactory.decodeFile(tempPath);
//            c.close();
//            return b;
//        }
        return null;
    }
    public static String compressImage(AppCompatActivity context,Bitmap image) {
        /**
         * 质量压缩,返回压缩后的图片路径String
         * */
        if(image == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩,把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int option = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length/1024 > 100) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%,把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, option, baos);
            if(option == 20){
                break;
            }
            option -= 5;
        }
        String path = context.getCacheDir().toString()+"/"+System.currentTimeMillis()+".jpg";
        File file = new File(path);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            baos.writeTo(fos);
            fos.close();
            baos.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(e.getMessage());
        }
        return null;
    }
    public static void startPicture(AppCompatActivity context){
        /**
         * 调起手机选取图片功能
         * */
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intent, PIC_PICTURE);
    }
    public static Uri startCamera(AppCompatActivity context){
        /**
         * 调起手机拍照功能,拍照完毕存储到Uri imageFilePath,返回存储地址
         * */
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.DISPLAY_NAME,
                "picture" + new Date().toString());
        values.put(MediaStore.Images.ImageColumns.DESCRIPTION, "this is a picture");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        Uri imageFilePath = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
        context.startActivityForResult(camera, PIC_CAMERA);
        return imageFilePath;
    }
    public static void cropImage(AppCompatActivity context,Uri imageFilePath,int width, int height) {
        /**
         * 根据图片Uri剪切图片,设置宽高
         * */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageFilePath, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);// 图像输出
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, BitmapUtil.PIC_CROP);
    }
    public static ImageOptions getOptionRadius(){
        /**
         * 联合xutils使用,设置图片圆角
         * dip2px ImageView宽度的一半
         * */
        return new ImageOptions.Builder()
                //如果ImageView宽高为60(设置ImageView一半30)
                .setRadius(DensityUtil.dip2px(30))
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_INSIDE)
                .setFailureDrawableId(R.drawable.ic_common_defult)
                .build();
    }
    public static ImageOptions getOptionCommon(){
        /**
         * 联合xutils使用,设置一般图片
         *
         * */
        return new ImageOptions.Builder()
                .setCrop(false)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
    }
    public static byte[] bitmapToByte(Bitmap bmp){
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bmp.recycle();//自由选择是否进行回收
        byte[] result = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
