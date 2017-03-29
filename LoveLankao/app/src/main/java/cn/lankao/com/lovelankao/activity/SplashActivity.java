package cn.lankao.com.lovelankao.activity;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.MyLocationClient;
import cn.lankao.com.lovelankao.utils.PermissionUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }, 2000);
        checkPermission();
        ((TextView)findViewById(R.id.tv_splash_code)).setText("版本 "+WindowUtils.getAppVersionName());
    }
    public void checkPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(this, permission)) {
                String[] reqPer = new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                requestPermissions(reqPer, 100);
                if (PermissionUtil.checkDismissPermissionWindow(this,
                        permission)) {
                    ToastUtil.show("定位权限获取失败,请去设置中打开");
                    return;
                }
                ToastUtil.show("定位权限获取失败,请去设置中打开");
            } else {
                //App已经获取定位权限
                initLocation();
            }
        } else {
            //Android系统小于 6.0
            initLocation();
        }
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