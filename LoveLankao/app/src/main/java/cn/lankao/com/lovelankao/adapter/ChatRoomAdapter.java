package cn.lankao.com.lovelankao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.entity.ChatRoom;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder>{
    private Context context;
    private List<ChatRoom> data;
    public ChatRoomAdapter(Context context){
        this.context = context;
        data = new ArrayList<>();
    }
    public void addData(ChatRoom data){
        this.data.add(data);
    }
    public void setData(List<ChatRoom> data){
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.activity_chatroom_items, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatRoom chat = data.get(position);
        holder.tvNickname.setText(chat.getNickName());
        holder.tvContent.setText(chat.getChatContent());
        holder.tvTime.setText(chat.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname;
        TextView tvContent;
        TextView tvTime;
        public MyViewHolder(View view) {
            super(view);
            tvNickname = (TextView) view.findViewById(R.id.tv_chat_item_nickname);
            tvContent = (TextView) view.findViewById(R.id.tv_chat_item_content);
            tvTime = (TextView) view.findViewById(R.id.tv_chat_item_time);
        }
    }
}
