package cn.lankao.com.lovelankao.widget;
import android.app.ProgressDialog;
import android.content.Context;
/**
 * Created by BuZhiheng on 2016/4/20.
 */
public class ProDialog {
    private static ProgressDialog dialog;
    private ProDialog(){
    }
    public static ProgressDialog getProDialog(Context context){
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请稍后...");
        dialog.setCancelable(true);
        return dialog;
    }
}