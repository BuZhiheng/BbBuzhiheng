package cn.lankao.com.lovelankao.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import java.text.DecimalFormat;
import java.util.Random;
import cn.lankao.com.lovelankao.LApplication;
/**
 * Created by buzhiheng on 2016/8/11.
 */
public class WindowUtils {
    public static int getWindowsWidth(){
        WindowManager wm = (WindowManager) LApplication.getCtx()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    public static int getWindowsHeight(){
        WindowManager wm = (WindowManager) LApplication.getCtx()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
    public static int px2dip(float pxValue) {
        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        float scale = LApplication.getCtx().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = LApplication.getCtx().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(LApplication.getCtx().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
    public static int getAppVersionCode() {
        int versionCode;
        try {
            // ---get the package info---
            PackageManager pm = LApplication.getCtx().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(LApplication.getCtx().getPackageName(), 0);
            versionCode = pi.versionCode;
            return versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return 1;
    }
    public static int getRandom(){
        Random random = new Random(10000);
        return random.nextInt();
    }
    public static String getBaiduDistance(double dis){
        DecimalFormat df = new DecimalFormat("#.00");
        if (dis <= 1000){
            return df.format(dis)+"m";
        } else {
            float d = (float) (dis/1000);
            if (d > 10000){
                return "";
            } else {
                return df.format(d) + "km";
            }
        }
    }
}