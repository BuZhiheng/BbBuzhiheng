package cn.lankao.com.lovelankao.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import com.baidu.location.BDLocation;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.entity.Setting;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.MyLocationClient;
import cn.lankao.com.lovelankao.utils.PrefUtil;

/**
 * Created by BuZhiheng on 2016/4/6.
 */
public class SplashActivity extends AppCompatActivity{
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initLocation();
        initSetting();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }, 2000);
    }

    private void initSetting() {
        BmobQuery<Setting> query = new BmobQuery<>();
        query.addWhereEqualTo("setType",1);
        query.findObjects(this, new FindListener<Setting>() {
            @Override
            public void onSuccess(List<Setting> list) {
                if (list != null && list.size() > 0){
                    PrefUtil.putString(CommonCode.SP_SET_PARTNERURL,list.get(0).getSetPartnerUrl());
                    PrefUtil.putString(CommonCode.SP_SET_ABOUTUSURL,list.get(0).getSetAboutusUrl());
                    PrefUtil.putString(CommonCode.SP_SET_JCLKURL,list.get(0).getSetJCLKUrl());
                }
            }
            @Override
            public void onError(int i, String s) {
            }
        });
    }

    private void initLocation() {
        MyLocationClient.locSingle(new MyLocationClient.MyLocationListener() {
            @Override
            public void onLocSuccess(BDLocation bdLocation) {
                PrefUtil.putString(CommonCode.SP_LOCATION_ADDRESS,bdLocation.getAddrStr());
                PrefUtil.putFloat(CommonCode.SP_LOCATION_LAT, (float) bdLocation.getLatitude());
                PrefUtil.putFloat(CommonCode.SP_LOCATION_LNG, (float) bdLocation.getLongitude());
            }
        });
    }
}
