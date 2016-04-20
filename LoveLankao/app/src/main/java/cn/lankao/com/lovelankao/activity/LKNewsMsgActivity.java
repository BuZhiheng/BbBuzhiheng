package cn.lankao.com.lovelankao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.viewcontroller.LKNewsMsgActivityController;

/**
 * Created by BuZhiheng on 2016/4/20.
 */
public class LKNewsMsgActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LKNewsMsgActivityController(this);
    }
}
