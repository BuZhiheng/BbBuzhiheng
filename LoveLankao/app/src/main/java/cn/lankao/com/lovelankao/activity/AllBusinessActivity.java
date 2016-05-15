package cn.lankao.com.lovelankao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.lankao.com.lovelankao.viewcontroller.AllBusinessActivityController;
/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class AllBusinessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AllBusinessActivityController(this);
    }
}
