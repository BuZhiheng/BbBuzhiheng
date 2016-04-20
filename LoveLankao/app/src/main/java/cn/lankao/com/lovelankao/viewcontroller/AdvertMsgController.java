package cn.lankao.com.lovelankao.viewcontroller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;

import org.xutils.x;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.activity.PicShowActivity;
import cn.lankao.com.lovelankao.activity.ShopLocationActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.MapUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertMsgController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private AdvertMsgActivity context;
    private AdvertNormal advertNormal;
    private SwipeRefreshLayout refresh;
    private ImageView ivPhoto;
    private ImageView ivCall;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvAverge;
    private TextView tvActivite;
    private TextView tvPoints;
    private TextView tvTitleCenter;
    private TextView tvAddress;
    private TextView tvDistance;
    private TextView tvContentMsg;
    private TextView tvPinglun;
    private LinearLayout layoutBottom;
    private LinearLayout layoutAddress;
    private Intent intent;
    private ProgressDialog dialog;
    public AdvertMsgController(AdvertMsgActivity context) {
        this.context = context;
        x.view().inject(context);
        initView();
        intent = context.getIntent();
        if (intent != null) {
            advertNormal = (AdvertNormal) intent.getSerializableExtra("data");
            if(advertNormal != null){
                initData();
            }
        }
    }
    private void initData(){
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.getObject(context, advertNormal.getObjectId(), new GetListener<AdvertNormal>() {
            @Override
            public void onSuccess(AdvertNormal advert) {
                advertNormal = advert;
                refreshData();
                refresh.setRefreshing(false);
                dialog.dismiss();
            }
            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        context.findViewById(R.id.iv_advertmsg_back).setOnClickListener(this);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_advertmsg_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        ivPhoto = (ImageView) context.findViewById(R.id.iv_advertdetail_photo);
        ivCall = (ImageView) context.findViewById(R.id.iv_advertdetail_call);
        tvTitle = (TextView) context.findViewById(R.id.tv_advertdetail_title);
        tvContent = (TextView) context.findViewById(R.id.tv_advertdetail_content);
        tvAverge = (TextView) context.findViewById(R.id.tv_advertdetail_average);
        tvActivite = (TextView) context.findViewById(R.id.tv_advertdetail_activite);
        tvPoints = (TextView) context.findViewById(R.id.tv_advertdetail_points);
        tvTitleCenter = (TextView) context.findViewById(R.id.tv_advertdetail_title_center);
        tvAddress = (TextView) context.findViewById(R.id.tv_advertdetail_address);
        tvDistance = (TextView) context.findViewById(R.id.tv_advertdetail_distance);
        tvContentMsg = (TextView) context.findViewById(R.id.tv_advertdetail_content_msg);
        tvPinglun = (TextView) context.findViewById(R.id.tv_advertdetail_new_pinglun);
        layoutBottom = (LinearLayout) context.findViewById(R.id.ll_advertmsg_bottom);
        layoutAddress = (LinearLayout) context.findViewById(R.id.ll_advertdetail_address);
        layoutAddress.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
    }

    private void refreshData() {
        if (advertNormal.getAdvPhoto() != null) {
            x.image().bind(ivPhoto, advertNormal.getAdvPhoto().getFileUrl(context));
        }
        if (advertNormal.getAdvClicked() == null) {
            tvPoints.setText("已点击:0次");
        } else {
            tvPoints.setText("已点击:" + advertNormal.getAdvClicked() + "次");
        }
        if(advertNormal.getAdvPhoneNumber() == null ||  "".equals(advertNormal.getAdvPhoneNumber())){
            ivCall.setVisibility(View.GONE);
        }
        LatLng latLng1 = new LatLng(advertNormal.getAdvLat(),advertNormal.getAdvLng());
        LatLng latLng2 = new LatLng(PrefUtil.getFloat(CommonCode.SP_LOCATION_LAT, 0),PrefUtil.getFloat(CommonCode.SP_LOCATION_LNG,0));
        tvDistance.setText(MapUtil.getDistance(latLng1, latLng2));
        tvTitle.setText(advertNormal.getTitle());
        tvContent.setText(advertNormal.getTitleContent());
        tvAverge.setText(advertNormal.getAdvPrice());
        tvActivite.setText(advertNormal.getAdvActivity());
        tvTitleCenter.setText(advertNormal.getTitle());
        tvAddress.setText(advertNormal.getAdvAddress());
        tvContentMsg.setText(advertNormal.getAdvContent());
        tvPinglun.setText(advertNormal.getAdvNewPinglun());
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.addWhereEqualTo("advVipType", CommonCode.ADVERT_TUIJIAN);
        query.findObjects(context, new FindListener<AdvertNormal>() {
            @Override
            public void onSuccess(List<AdvertNormal> list) {
                setBottom(list);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBottom(List<AdvertNormal> list) {
        layoutBottom.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final ViewHolder holder = new ViewHolder();
            final AdvertNormal advert = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_main_items, null);
            holder.photo = (ImageView) view.findViewById(R.id.iv_mainfrm_item_photo);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_mainfrm_item_title);
            holder.tvTitleContent = (TextView) view.findViewById(R.id.tv_mainfrm_item_titlecontent);
            holder.tvPoints = (TextView) view.findViewById(R.id.tv_mainfrm_item_points);
            holder.tvAverage = (TextView) view.findViewById(R.id.tv_mainfrm_item_average);
            holder.frameLayout = (FrameLayout) view.findViewById(R.id.fl_mainfrm_content);
            if (advert.getAdvPhoto() != null) {
                x.image().bind(holder.photo, advert.getAdvPhoto().getFileUrl(context));
            }
            if (advert.getAdvClicked() == null) {
                holder.tvPoints.setText("点击量:0");
            } else {
                holder.tvPoints.setText("点击量:" + advert.getAdvClicked());
            }
            if (advert.getAdvPrice() != null) {
                holder.tvAverage.setText("¥" + advert.getAdvPrice());
            }
            holder.tvTitle.setText(advert.getTitle());
            holder.tvTitleContent.setText(advert.getTitleContent());
            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advert.getAdvClicked() != null) {
                        advert.setAdvClicked(advert.getAdvClicked() + 1);
                    } else {
                        advert.setAdvClicked(1);
                    }
                    advert.update(context);
                    Intent intent = new Intent(context, AdvertMsgActivity.class);
                    intent.putExtra("data", advert);
                    context.startActivity(intent);
                    holder.tvPoints.setText("点击量:" + advert.getAdvClicked());
                }
            });
            layoutBottom.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layoutAddress) {
            Intent intent = new Intent(context, ShopLocationActivity.class);
            intent.putExtra("title",advertNormal.getTitle());
            intent.putExtra("lat",advertNormal.getAdvLat());
            intent.putExtra("lng",advertNormal.getAdvLng());
            context.startActivity(intent);
        } else if (v == ivCall) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + advertNormal.getAdvPhoneNumber()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        } else if(v.getId() == R.id.iv_advertmsg_back){
            context.finish();
        } else if(v.getId() == R.id.iv_advertdetail_photo){
            if (advertNormal == null){
                return;
            }
            Intent intent = new Intent(context, PicShowActivity.class);
            intent.putExtra(CommonCode.INTENT_ADVERT_TYPE,advertNormal);
            context.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    class ViewHolder{
        FrameLayout frameLayout;
        ImageView photo;
        TextView tvTitle;
        TextView tvAverage;
        TextView tvPoints;
        TextView tvTitleContent;
    }
}
