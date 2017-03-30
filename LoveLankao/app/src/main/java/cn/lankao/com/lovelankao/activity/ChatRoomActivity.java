package cn.lankao.com.lovelankao.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.ChatRoomController;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoomActivity extends AppCompatActivity {
    private ChatRoomController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        controller = new ChatRoomController(this);
    }
    @Override
    protected void onDestroy() {
        controller.onDestory();
        super.onDestroy();
    }
}