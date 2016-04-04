package cn.lankao.com.lovelankao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.SquareSendActivityController;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareSendActivity extends AppCompatActivity{
    private SquareSendActivityController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_send);
        controller = new SquareSendActivityController(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onActivityResult(requestCode, resultCode, data);
    }

    public interface SquareSendHolder{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
