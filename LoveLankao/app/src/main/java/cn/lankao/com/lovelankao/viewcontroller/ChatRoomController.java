package cn.lankao.com.lovelankao.viewcontroller;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.adapter.ChatRoomAdapter;
import cn.lankao.com.lovelankao.entity.ChatRoom;
import cn.lankao.com.lovelankao.utils.ToastUtil;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoomController implements ChatRoomActivity.ChatRoomHolder, View.OnClickListener {
    private ChatRoomActivity context;
    private RecyclerView rvChat;
    private BmobRealTimeData realTimeData;
    private ChatRoomAdapter adapter;
    private EditText etContent;

    public ChatRoomController(ChatRoomActivity context){
        this.context = context;
        initView();
    }

    private void initView() {
        realTimeData = new BmobRealTimeData();
        adapter = new ChatRoomAdapter(context);
        context.findViewById(R.id.btn_chat_send).setOnClickListener(this);
        etContent = (EditText) context.findViewById(R.id.et_chat_content);
        rvChat = (RecyclerView) context.findViewById(R.id.rv_chat_room);
        rvChat.setLayoutManager(new LinearLayoutManager(context));
        rvChat.setAdapter(adapter);

        realTimeData.start(context, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                realTimeData.subTableUpdate("ChatRoom");
                getChat();
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                Gson gson = new Gson();
                ChatRoom chatRoom = null;
                try {
                    chatRoom = gson.fromJson(jsonObject.get("data").toString(), ChatRoom.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.addData(chatRoom);
                adapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(adapter.getItemCount());
                ToastUtil.show(chatRoom.getChatContent());
            }
        });
    }

    private void getChat() {
        BmobQuery<ChatRoom> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<ChatRoom>() {
            @Override
            public void onSuccess(List<ChatRoom> list) {
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onDestory() {
        if (realTimeData.isConnected()){
            realTimeData.unsubTableUpdate("ChatRoom");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_chat_send:
                sendMsg();
                break;
            default:break;
        }
    }

    private void sendMsg() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatContent(etContent.getText().toString());
        chatRoom.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                etContent.setText("");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("发送失败");
            }
        });
    }
}
