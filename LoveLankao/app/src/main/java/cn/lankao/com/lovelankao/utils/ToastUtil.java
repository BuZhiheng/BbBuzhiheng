package cn.lankao.com.lovelankao.utils;
import android.widget.Toast;
import cn.lankao.com.lovelankao.LApplication;
/**
 * Created by BuZhiheng on 2016/4/1.
 */
public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(LApplication.getCtx(), msg, Toast.LENGTH_SHORT).show();
    }
}
