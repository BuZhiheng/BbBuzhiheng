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

import org.xutils.image.ImageOptions;
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
import cn.lankao.com.lovelankao.entity.Comment;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.MapUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
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
    private LinearLayout layoutComment;
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
        initComment();
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
                ToastUtil.show(s);
                refresh.setRefreshing(false);
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
        layoutComment = (LinearLayout) context.findViewById(R.id.ll_advertmsg_comment);

        layoutAddress = (LinearLayout) context.findViewById(R.id.ll_advertdetail_address);
        layoutAddress.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
    }

    private void refreshData() {
        if (advertNormal.getAdvPhoto() != null) {
            ImageOptions imageOptions =new ImageOptions.Builder()
                    .setCrop(false)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.ic_common_defult)//加载中默认显示图片
                    .build();
            x.image().bind(ivPhoto, advertNormal.getAdvPhoto().getFileUrl(context),imageOptions);
        }
        if (advertNormal.getAdvClicked() == null) {
            tvPoints.setText("已点击:0次");
        } else {
            tvPoints.setText("已点击:" + advertNormal.getAdvClicked() + "次");
        }
        if(advertNormal.getAdvPhoneNumber() == null){
            ivCall.setVisibility(View.GONE);
        }
        if (advertNormal.getAdvLat() != null && advertNormal.getAdvLng() != null){
            LatLng latLng1 = new LatLng(advertNormal.getAdvLat(),advertNormal.getAdvLng());
            LatLng latLng2 = new LatLng(PrefUtil.getFloat(CommonCode.SP_LOCATION_LAT, 0),PrefUtil.getFloat(CommonCode.SP_LOCATION_LNG,0));
            tvDistance.setText(MapUtil.getDistance(latLng1, latLng2));
        }
        tvTitle.setText(advertNormal.getTitle());
        tvContent.setText(advertNormal.getTitleContent());
        tvAverge.setText(advertNormal.getAdvPrice());

        tvActivite.setText(advertNormal.getAdvActivity());
        tvTitleCenter.setText(advertNormal.getTitle());
        tvAddress.setText(advertNormal.getAdvAddress());
        tvContentMsg.setText(advertNormal.getAdvContent());
        tvPinglun.setText(advertNormal.getAdvRemark());
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
    private void initComment(){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("postId",advertNormal.getObjectId());
        query.findObjects(context, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> list) {
                setComment(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void setComment(List<Comment> list){
        if (list == null || list.size() == 0){
            return;
        }
        layoutComment.removeAllViews();
        for (int i=0;i<list.size();i++){
            ViewHolderComment holder = new ViewHolderComment();
            Comment comm = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_advertmsg_comment_item,null);
            holder.tvUsername = (TextView) view.findViewById(R.id.tv_advert_comment_username);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_advert_comment_content);
            holder.tvUsername.setText(comm.getUsername());
            holder.tvContent.setText(comm.getContent());
            layoutComment.addView(view);
        }
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
    class ViewHolderComment{
        TextView tvUsername;
        TextView tvContent;
    }
}
