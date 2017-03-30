package cn.lankao.com.lovelankao.viewcontroller;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.adapter.ChatRoomAdapter;
import cn.lankao.com.lovelankao.model.ChatRoom;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoomController implements View.OnClickListener {
    private ChatRoomActivity context;
    private RecyclerView rvChat;
    private BmobRealTimeData realTimeData;
    private ChatRoomAdapter adapter;
    private EditText etContent;
    private ImageView ivNewMsg;
    private ProgressDialog dialog;
    private boolean isBottom = false;
    public ChatRoomController(ChatRoomActivity context){
        this.context = context;
        initView();
    }
    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        realTimeData = new BmobRealTimeData();
        adapter = new ChatRoomAdapter(context);
        context.findViewById(R.id.btn_chat_send).setOnClickListener(this);
        context.findViewById(R.id.iv_chatroom_back).setOnClickListener(this);
        etContent = (EditText) context.findViewById(R.id.et_chat_content);
        ivNewMsg = (ImageView) context.findViewById(R.id.iv_chatroom_newmsg);
        ivNewMsg.setOnClickListener(this);
        rvChat = (RecyclerView) context.findViewById(R.id.rv_chat_room);
        rvChat.setLayoutManager(new LinearLayoutManager(context));
        rvChat.setAdapter(adapter);
        rvChat.addOnScrollListener(new OnRvScrollListener(){
            @Override
            public void toBottom() {
                isBottom = true;
                ivNewMsg.setVisibility(View.GONE);
            }
            @Override
            public void toMid() {
                isBottom = false;
            }
        });
        realTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                realTimeData.subTableUpdate("ChatRoom");
                //连接成功聊天室,加载历史记录
                initChatRoomHis();
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
                if (TextUtil.isNull(chatRoom.getNickName())){
                    return;
                }
                adapter.addData(chatRoom);
                adapter.notifyDataSetChanged();
                if (isBottom){
                    //如果在最底部就滚动到最底部
                    rvChat.smoothScrollToPosition(adapter.getItemCount());
                } else {
                    ivNewMsg.setVisibility(View.VISIBLE);
                }
                String myId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
                if (TextUtil.isNull(myId) || !myId.equals(chatRoom.getUserId())){
                    WindowUtils.showVoice(context);
                }
                dialog.dismiss();
            }
        });
    }
    private void initChatRoomHis() {
        BmobQuery<ChatRoom> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(100);
        query.findObjects(new FindListener<ChatRoom>() {
            @Override
            public void done(List<ChatRoom> list, BmobException e) {
                if (list != null){
                    Collections.reverse(list);
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                //加载历史记录完毕以后,发送本人加入聊天室信息
                sendMsg(1);
            }
        });
    }
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
            case R.id.iv_chatroom_newmsg:
                rvChat.smoothScrollToPosition(adapter.getItemCount());
            default:break;
        }
    }
    private void sendMsg(int isFirst) {
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "游客");
        String type = PrefUtil.getString(CommonCode.SP_USER_USERTYPE,"");
        String userId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatUserType(type);
        chatRoom.setUserId(userId);
        if(isFirst == 1){
            //发送本人加入聊天室
            chatRoom.setNickName(nickname);
            chatRoom.setChatContent("欢迎  " + nickname + "  进入聊天室!");
        } else {
            //发送内容
            String content = etContent.getText().toString();
            if (TextUtil.isNull(content)){
                ToastUtil.show("请输入内容");
                return;
            }
            chatRoom.setNickName(nickname);
            chatRoom.setChatContent(content);
        }
        chatRoom.save(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null){
                    etContent.setText("");
                }
            }
        });
    }
}