package cn.lankao.com.lovelankao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.SettingActivityController;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class SettingActivity extends AppCompatActivity{
    private SettingActivityController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        controller = new SettingActivityController(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onActivityResult(requestCode, resultCode, data);
    }

    public interface SettingHolder{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
