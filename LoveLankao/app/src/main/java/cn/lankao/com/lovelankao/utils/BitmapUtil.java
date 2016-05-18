package cn.lankao.com.lovelankao.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by BuZhiheng on 2016/4/5.
 */
public class BitmapUtil {
    public static void compressImage(Bitmap image,Context context) {
        if(image == null){
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩,把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int option = 50;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length/1024 > 100) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%,把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, option, baos);
            if(option == 20){
                break;
            }
            option --;
        }
        String path = context.getCacheDir().toString()+"/"+System.currentTimeMillis()+".jpg";
        File file = new File(path);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            baos.writeTo(fos);
            fos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(e.getMessage());
        }
        return;
    }
}
