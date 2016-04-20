package cn.lankao.com.lovelankao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.viewcontroller.PicShowActivityController;

/**
 * Created by BuZhiheng on 2016/4/20.
 */
public class PicShowActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PicShowActivityController(this);
    }
}
