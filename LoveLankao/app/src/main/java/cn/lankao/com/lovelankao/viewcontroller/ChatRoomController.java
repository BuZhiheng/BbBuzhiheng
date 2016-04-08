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

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.adapter.ChatRoomAdapter;
import cn.lankao.com.lovelankao.entity.ChatRoom;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
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
        context.findViewById(R.id.iv_chatroom_back).setOnClickListener(this);
        etContent = (EditText) context.findViewById(R.id.et_chat_content);
        rvChat = (RecyclerView) context.findViewById(R.id.rv_chat_room);
        rvChat.setLayoutManager(new LinearLayoutManager(context));
        rvChat.setAdapter(adapter);

        realTimeData.start(context, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                realTimeData.subTableUpdate("ChatRoom");
                sendMsg(1);
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
                sendMsg(0);
                break;
            case R.id.iv_chatroom_back:
                context.finish();
                break;
            default:break;
        }
    }

    private void sendMsg(int isFirst) {
        ChatRoom chatRoom = new ChatRoom();
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "游客");
        if(isFirst == 1){
            chatRoom.setNickName(nickname);
            chatRoom.setChatContent(nickname + "  加入聊天室!");
        }else{
            String content = etContent.getText().toString();
            if (content == null || "".equals(content)){
                ToastUtil.show("请输入内容");
                return;
            }
            chatRoom.setNickName(nickname);
            chatRoom.setChatContent(content);
        }
        String type = PrefUtil.getString(CommonCode.SP_USER_USERTYPE,"");
        if ("1000".equals(type)){
            chatRoom.setChatUserType("管理员");
        }else if ("1001".equals(type)){
            chatRoom.setChatUserType("VIP用户");
        } else{
            chatRoom.setChatUserType("");
        }
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
