package cn.lankao.com.lovelankao;
import android.app.Application;
import android.content.Context;
import com.baidu.mapapi.SDKInitializer;
import org.xutils.x;
import cn.bmob.v3.Bmob;
/**
 * Created by BuZhiheng on 2016/3/30.
 * Dell/戴尔 灵越15(7559) Ins15P-2748
 */
public class LApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        x.Ext.init(this);
        SDKInitializer.initialize(this);
        //第一：默认初始化
        Bmob.initialize(this, "fe7893d2bc42ed427a178367a0e1d6b6");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("fe7893d2bc42ed427a178367a0e1d6b6")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }
    public static Context getCtx(){
        return context;
    }
}