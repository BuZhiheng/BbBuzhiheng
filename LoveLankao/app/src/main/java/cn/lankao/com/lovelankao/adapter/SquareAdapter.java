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
import cn.lankao.com.lovelankao.entity.Square;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.MyViewHolder> {
    private Context context;
    private List<Square> data;

    public SquareAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void addData(Square data) {
        this.data.add(data);
    }

    public void setData(List<Square> data) {
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
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Square square = data.get(position);
        holder.tvNickname.setText(square.getNickName());
        holder.tvContent.setText(square.getSquareContent());
        holder.tvTime.setText(square.getCreatedAt());
        holder.tvUserType.setText(square.getSquareUserType());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname;
        TextView tvUserType;
        TextView tvContent;
        TextView tvTime;

        public MyViewHolder(View view) {
            super(view);
            tvNickname = (TextView) view.findViewById(R.id.tv_square_item_nickname);
            tvUserType = (TextView) view.findViewById(R.id.tv_square_item_usertype);
            tvContent = (TextView) view.findViewById(R.id.tv_square_item_content);
            tvTime = (TextView) view.findViewById(R.id.tv_square_item_time);
        }
    }
}
