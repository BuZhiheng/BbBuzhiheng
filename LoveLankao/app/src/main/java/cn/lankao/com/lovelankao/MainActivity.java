package cn.lankao.com.lovelankao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    private boolean canExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "fe7893d2bc42ed427a178367a0e1d6b6");
        new MainActivityController(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canExit) {
                finish();
                return true;
            }
            canExit = true;
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                        canExit = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
