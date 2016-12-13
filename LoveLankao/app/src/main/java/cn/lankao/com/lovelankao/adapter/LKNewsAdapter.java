package cn.lankao.com.lovelankao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsMsgActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.entity.Cook;
import cn.lankao.com.lovelankao.entity.LanKaoNews;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class LKNewsAdapter extends RecyclerView.Adapter<LKNewsAdapter.MyViewHolder> {
    private Context context;
    private List<LanKaoNews> data;
    public LKNewsAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }

    public void setData(List<LanKaoNews> data) {
        this.data = data;
    }
    public void addData(List<LanKaoNews> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_lknews_item, parent, false));

        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final LanKaoNews news = data.get(position);
        holder.photo.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_common_defult));
        if (news.getNewsImg() != null){
            x.image().bind(holder.photo,news.getNewsImg());
        }
        holder.tvTitle.setText(news.getNewsTitle());
        holder.tvTime.setText(news.getNewsTime());
        holder.tvFrom.setText(news.getNewsFrom());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LKNewsMsgActivity.class);
                intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, news);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout ll;
        ImageView photo;
        TextView tvTitle;
        TextView tvTime;
        TextView tvFrom;
        public MyViewHolder(View view) {
            super(view);
            ll = (FrameLayout) view.findViewById(R.id.fl_lknews_content);
            photo = (ImageView) view.findViewById(R.id.iv_lknews_item_photo);
            tvTitle = (TextView) view.findViewById(R.id.tv_lknews_item_title);
            tvTime = (TextView) view.findViewById(R.id.tv_lknews_item_time);
            tvFrom = (TextView) view.findViewById(R.id.tv_lknews_item_from);
        }
    }
}