package cn.lankao.com.lovelankao.widget;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
/**
 * Created by BuZhiheng on 2016/12/19.
 */
public class MyDialog {
    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;
    private MyDialog(){
    }
    public static ProgressDialog getNoticeDialog(Context context,String notice){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(notice);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
    public static AlertDialog getAlertDialog(Context context,String title,String msg,boolean cancelAble,DialogInterface.OnClickListener listener) {
        /*
        这里使用了 android.support.v7.app.AlertDialog.Builder
        可以直接在头部写 import android.support.v7.app.AlertDialog
        那么下面就可以写成 AlertDialog.Builder
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(cancelAble);
        alertDialog = builder.show();
        return alertDialog;
    }
}