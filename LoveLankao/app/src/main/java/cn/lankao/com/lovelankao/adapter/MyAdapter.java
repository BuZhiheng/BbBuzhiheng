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

import com.baidu.mapapi.model.LatLng;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.MapUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<AdvertNormal> data;
//    private ImageOptions options;
    public MyAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
//        options = new ImageOptions.Builder()
//                .setRadius(DensityUtil.dip2px(5))
//                .build();
    }

    public void setData(List<AdvertNormal> data) {
        this.data = data;
    }
    public void addData(List<AdvertNormal> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_main_items, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AdvertNormal advert = data.get(position);
        if (advert.getAdvPhoto() == null){
        }else{
            x.image().bind(holder.photo, advert.getAdvPhoto().getFileUrl(context));
        }
        if (advert.getAdvClicked() == null){
            holder.tvPoints.setText("点击量:0");
        } else {
            holder.tvPoints.setText("点击量:" + advert.getAdvClicked());
        }
        if (advert.getAdvPrice() != null){
            holder.tvAverage.setText("¥"+advert.getAdvPrice());
        }
        if (advert.getAdvLat() != null && advert.getAdvLng() != null){
            LatLng latLng1 = new LatLng(advert.getAdvLat(),advert.getAdvLng());
            LatLng latLng2 = new LatLng(PrefUtil.getFloat(CommonCode.SP_LOCATION_LAT,0),PrefUtil.getFloat(CommonCode.SP_LOCATION_LNG,0));

            holder.tvDistance.setText(MapUtil.getDistance(latLng1,latLng2));
        }
        holder.tvTitle.setText(advert.getTitle());
        holder.tvTitleContent.setText(advert.getTitleContent());
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvertNormal advertNormal = data.get(position);
                if(advertNormal.getAdvClicked() != null){
                    advertNormal.setAdvClicked(advertNormal.getAdvClicked() + 1);
                } else {
                    advertNormal.setAdvClicked(1);
                }
                advertNormal.update(context);
                Intent intent = new Intent(context, AdvertMsgActivity.class);
                intent.putExtra("data",data.get(position));
                context.startActivity(intent);
                holder.tvPoints.setText("点击量:" + advertNormal.getAdvClicked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        ImageView photo;
        TextView tvTitle;
        TextView tvAverage;
        TextView tvPoints;
        TextView tvDistance;
        TextView tvTitleContent;

        public MyViewHolder(View view) {
            super(view);
            frameLayout = (FrameLayout) view.findViewById(R.id.fl_mainfrm_content);
            photo = (ImageView) view.findViewById(R.id.iv_mainfrm_item_photo);
            tvTitle = (TextView) view.findViewById(R.id.tv_mainfrm_item_title);
            tvTitleContent = (TextView) view.findViewById(R.id.tv_mainfrm_item_titlecontent);
            tvPoints = (TextView) view.findViewById(R.id.tv_mainfrm_item_points);
            tvDistance = (TextView) view.findViewById(R.id.tv_mainfrm_item_distance);
            tvAverage = (TextView) view.findViewById(R.id.tv_mainfrm_item_average);
        }
    }
}