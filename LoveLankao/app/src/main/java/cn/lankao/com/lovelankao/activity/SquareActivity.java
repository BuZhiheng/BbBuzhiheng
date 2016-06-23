package cn.lankao.com.lovelankao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.SquareActivityController;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivity extends AppCompatActivity{
    private SquareActivityController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_msg);
        controller = new SquareActivityController(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }
}
