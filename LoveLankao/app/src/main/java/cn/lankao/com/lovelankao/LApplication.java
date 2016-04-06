package cn.lankao.com.lovelankao;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class LApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Bmob.initialize(this, "fe7893d2bc42ed427a178367a0e1d6b6");
        x.Ext.init(this);
        SDKInitializer.initialize(this);
    }

    public static Context getCtx(){
        return context;
    }
}
