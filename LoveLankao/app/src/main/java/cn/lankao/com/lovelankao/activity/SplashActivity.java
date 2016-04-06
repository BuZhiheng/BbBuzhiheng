package cn.lankao.com.lovelankao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;

import cn.lankao.com.lovelankao.MainActivity;
import cn.lankao.com.lovelankao.R;
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
        MyLocationClient.locSingle(new MyLocationClient.MyLocationListener() {
            @Override
            public void onLocSuccess(BDLocation bdLocation) {
                PrefUtil.putString(CommonCode.SP_ADDRESS,bdLocation.getAddrStr());
                PrefUtil.putFloat(CommonCode.SP_LAT, (float) bdLocation.getLatitude());
                PrefUtil.putFloat(CommonCode.SP_LNG, (float) bdLocation.getLongitude());
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        },2000);
    }
}
