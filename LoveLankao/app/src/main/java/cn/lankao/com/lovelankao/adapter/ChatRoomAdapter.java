package cn.lankao.com.lovelankao.adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.ChatRoom;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {
    private Context context;
    public static List<ChatRoom> data = new ArrayList<>();
    public ChatRoomAdapter(Context context) {
        this.context = context;
    }
    public void addData(ChatRoom data) {
        this.data.add(data);
    }
    public void setData(List<ChatRoom> data) {
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
    public int getItemCount() {
        return data.size();
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatRoom chat = data.get(position);
        holder.tvNickname.setText(chat.getNickName());
        holder.tvContent.setText(chat.getChatContent());
        holder.tvTime.setText(chat.getCreatedAt());
        holder.tvUserType.setText(chat.getChatUserType());
        String myId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
        if (!TextUtil.isNull(myId) && myId.equals(chat.getUserId())){
            holder.tvContent.setTextColor(ContextCompat.getColor(context,R.color.color_black_text));
        } else {
            holder.tvContent.setTextColor(ContextCompat.getColor(context,R.color.color_green));
        }
        holder.tvUserType.setText(TextUtil.getVipString(chat.getChatUserType()));
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname;
        TextView tvUserType;
        TextView tvContent;
        TextView tvTime;
        public MyViewHolder(View view) {
            super(view);
            tvNickname = (TextView) view.findViewById(R.id.tv_chat_item_nickname);
            tvUserType = (TextView) view.findViewById(R.id.tv_chat_item_usertype);
            tvContent = (TextView) view.findViewById(R.id.tv_chat_item_content);
            tvTime = (TextView) view.findViewById(R.id.tv_chat_item_time);
        }
    }
}