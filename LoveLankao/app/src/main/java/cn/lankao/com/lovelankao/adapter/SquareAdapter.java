package cn.lankao.com.lovelankao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.entity.ChatRoom;
import cn.lankao.com.lovelankao.entity.Square;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.MyViewHolder>{
    private Context context;
    private List<Square> data;
    public SquareAdapter(Context context){
        this.context = context;
        x.view().inject((Activity) context);
        data = new ArrayList<>();
    }
    public void addData(Square data){
        this.data.add(data);
    }
    public void setData(List<Square> data){
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.activity_square_items, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Square square = data.get(position);
        holder.tvNickname.setText(square.getNickName());
        holder.tvContent.setText(square.getSquareContent());
        holder.tvTime.setText(square.getCreatedAt());
        if (square.getSquarePhoto() != null){
            x.image().bind(holder.ivPhoto,square.getSquarePhoto().getFileUrl(context));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname;
        TextView tvContent;
        TextView tvTime;
        ImageView ivPhoto;
        public MyViewHolder(View view) {
            super(view);
            tvNickname = (TextView) view.findViewById(R.id.tv_square_item_nickname);
            tvContent = (TextView) view.findViewById(R.id.tv_square_item_content);
            tvTime = (TextView) view.findViewById(R.id.tv_square_item_time);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_square_item_photo);
        }
    }
}
