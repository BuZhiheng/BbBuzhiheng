package cn.lankao.com.lovelankao.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import cn.lankao.com.lovelankao.LApplication;

/**
 * Created by BuZhiheng on 2016/5/23.
 */
public class WindowUtils {
    static WindowManager wm = (WindowManager) LApplication.getCtx().getSystemService(Context.WINDOW_SERVICE);
    static DisplayMetrics dis = new DisplayMetrics();
    public static int getWindowWidth(){
        wm.getDefaultDisplay().getMetrics(dis);
        return dis.widthPixels;
    }
    public static int getWindowHeight(){
        wm.getDefaultDisplay().getMetrics(dis);
        return dis.heightPixels;
    }
    public static int px2dip(AppCompatActivity context,float pxValue) {
        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}