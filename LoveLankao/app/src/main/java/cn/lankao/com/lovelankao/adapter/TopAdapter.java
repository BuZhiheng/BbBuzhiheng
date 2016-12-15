package cn.lankao.com.lovelankao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.model.Top;
import cn.lankao.com.lovelankao.model.CommonCode;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class TopAdapter extends RecyclerView.Adapter<TopAdapter.MyViewHolder> {
    private Context context;
    private List<Top> data;
    public TopAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }

    public void setData(List<Top> data) {
        this.data = data;
    }
    public void addData(List<Top> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_top_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Top news = data.get(position);
        if (news.getImg() != null){
            x.image().bind(holder.photo, "http://tnfs.tngou.net/image"+news.getImg());
        }
        holder.tvTitle.setText(news.getTitle());
        holder.tvFrom.setText("文章来自:" + news.getFromname());
        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "文章详情");
                intent.putExtra(CommonCode.INTENT_SETTING_URL, news.getFromurl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl;
        ImageView photo;
        TextView tvTitle;
        TextView tvFrom;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_top_content);
            photo = (ImageView) view.findViewById(R.id.iv_top_item_photo);
            tvTitle = (TextView) view.findViewById(R.id.tv_top_item_title);
            tvFrom = (TextView) view.findViewById(R.id.tv_top_item_from);
        }
    }
}