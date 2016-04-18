package cn.lankao.com.lovelankao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.viewcontroller.ReadActivityController;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class ReadWeixinActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ReadActivityController(this);
    }
}
