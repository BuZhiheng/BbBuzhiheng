package cn.lankao.com.lovelankao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell on 2016/4/5.
 */
public class BitmapUtil {
    public static String compressImage(Context context,Bitmap bm) throws FileNotFoundException {
        return null;
    }
    public static String saveBitmapFile(Bitmap bitmap){
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String str = Environment.getExternalStorageDirectory().toString()+File.separator+"dong/image/"+name+".jpg";
        File file=new File(str);//将要保存图片的路径
        FileOutputStream ops;
        try {
            ops = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            baos.writeTo(ops);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
