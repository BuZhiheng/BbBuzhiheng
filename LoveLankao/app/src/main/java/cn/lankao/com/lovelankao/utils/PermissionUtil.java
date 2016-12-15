package cn.lankao.com.lovelankao.utils;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
/**
 * Created by buzhiheng on 2016/10/20.
 */
public class PermissionUtil {
    public static boolean checkNoPermission(Context context,String permission){
        //没有权限,返回true
        int per = ActivityCompat.checkSelfPermission(context,
                permission);
        if (per != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public static boolean checkDismissPermissionWindow(Context context,String permission){
        //没有权限,用户点击不在提示,返回true
        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                permission)) {
            return true;
        }
        return false;
    }
}