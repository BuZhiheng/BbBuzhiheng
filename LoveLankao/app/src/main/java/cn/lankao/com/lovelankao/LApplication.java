package cn.lankao.com.lovelankao;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class LApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        x.Ext.init(this);
        SDKInitializer.initialize(this);
    }

    public static Context getCtx(){
        return context;
    }
}
