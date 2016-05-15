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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.entity.Cook;
import cn.lankao.com.lovelankao.entity.Top;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class CookAdapter extends RecyclerView.Adapter<CookAdapter.MyViewHolder> {
    private Context context;
    private List<Cook> data;
    private String url;
    public CookAdapter(Context context,String url) {
        this.context = context;
        this.url = url;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }

    public void setData(List<Cook> data) {
        this.data = data;
    }
    public void addData(List<Cook> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_cook_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Cook cook = data.get(position);
        if (cook.getImg() != null){
            x.image().bind(holder.photo, "http://tnfs.tngou.net/image"+cook.getImg());
        }
        holder.tvTitle.setText(cook.getName());
        holder.tvDesc.setText(cook.getDescription());
        holder.tvFood.setText(cook.getFood());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "菜谱详情");
                intent.putExtra(CommonCode.INTENT_SETTING_URL, url+cook.getId());
                intent.putExtra(CommonCode.INTENT_SHARED_DESC,cook.getDescription());
                if (cook.getImg() != null){
                    intent.putExtra(CommonCode.INTENT_SHARED_IMG,"http://tnfs.tngou.net/image"+cook.getImg());
                } else {
                    intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView photo;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvFood;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_cook_content);
            photo = (ImageView) view.findViewById(R.id.iv_cook_item_photo);
            tvTitle = (TextView) view.findViewById(R.id.tv_cook_item_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_cook_item_desc);
            tvFood = (TextView) view.findViewById(R.id.tv_cook_item_food);
        }
    }
}