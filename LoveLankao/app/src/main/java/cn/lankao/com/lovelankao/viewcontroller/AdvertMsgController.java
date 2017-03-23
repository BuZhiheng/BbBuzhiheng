package cn.lankao.com.lovelankao.viewcontroller;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.activity.AllBusinessActivity;
import cn.lankao.com.lovelankao.activity.CommentActivity;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.PicShowActivity;
import cn.lankao.com.lovelankao.activity.ShopLocationActivity;
import cn.lankao.com.lovelankao.adapter.AdvertBannerHolder;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.utils.MapUtil;
import cn.lankao.com.lovelankao.utils.PermissionUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.ProDialog;
/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertMsgController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private AdvertMsgActivity context;
    private AdvertNormal advertNormal;
    private SwipeRefreshLayout refresh;
    private ConvenientBanner banner;
    private ImageView ivCall;
    private TextView tvIndex;
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
    private TextView tvMap;
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
        query.getObject(advertNormal.getObjectId(), new QueryListener<AdvertNormal>() {
            @Override
            public void done(AdvertNormal advert, BmobException e) {
                if (e == null){
                    advertNormal = advert;
                    refreshData();
                    refresh.setRefreshing(false);
                    dialog.dismiss();
                } else {
                    refresh.setRefreshing(false);
                    dialog.dismiss();
                }
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
        banner = (ConvenientBanner) context.findViewById(R.id.banner_advertdetail_photo);
        ivCall = (ImageView) context.findViewById(R.id.iv_advertdetail_call);
        tvIndex = (TextView) context.findViewById(R.id.tv_advertdetail_imgindex);
        tvTitle = (TextView) context.findViewById(R.id.tv_advertdetail_title);
        tvContent = (TextView) context.findViewById(R.id.tv_advertdetail_content);
        tvAverge = (TextView) context.findViewById(R.id.tv_advertdetail_average);
        tvActivite = (TextView) context.findViewById(R.id.tv_advertdetail_activite);
        tvPoints = (TextView) context.findViewById(R.id.tv_advertdetail_points);
        tvTitleCenter = (TextView) context.findViewById(R.id.tv_advertdetail_title_center);
        tvAddress = (TextView) context.findViewById(R.id.tv_advertdetail_address);
        tvDistance = (TextView) context.findViewById(R.id.tv_advertdetail_distance);
        tvContentMsg = (TextView) context.findViewById(R.id.tv_advertdetail_content_msg);
        tvMap = (TextView) context.findViewById(R.id.tv_advertdetail_map);
        tvPinglun = (TextView) context.findViewById(R.id.tv_advertdetail_new_pinglun);
        layoutBottom = (LinearLayout) context.findViewById(R.id.ll_advertmsg_bottom);
        layoutComment = (LinearLayout) context.findViewById(R.id.ll_advertmsg_comment);

        layoutAddress = (LinearLayout) context.findViewById(R.id.ll_advertdetail_address);
        layoutAddress.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        context.findViewById(R.id.tv_advertdetail_tocomment).setOnClickListener(this);
    }
    private void refreshData() {
        if (advertNormal != null) {
            final List<String> banners = new ArrayList<>();
            if (advertNormal.getAdvPhoto()!=null){
                banners.add(advertNormal.getAdvPhoto().getFileUrl());
            }
            if (advertNormal.getAdvPhoto1()!=null){
                banners.add(advertNormal.getAdvPhoto1().getFileUrl());
            }
            if (advertNormal.getAdvPhoto2()!=null){
                banners.add(advertNormal.getAdvPhoto2().getFileUrl());
            }
            if (advertNormal.getAdvPhoto3()!=null){
                banners.add(advertNormal.getAdvPhoto3().getFileUrl());
            }
            if (advertNormal.getAdvPhoto4()!=null){
                banners.add(advertNormal.getAdvPhoto4().getFileUrl());
            }
            if (advertNormal.getAdvPhoto5()!=null){
                banners.add(advertNormal.getAdvPhoto5().getFileUrl());
            }
            banner.setPages(
                    new CBViewHolderCreator<AdvertBannerHolder>() {
                        @Override
                        public AdvertBannerHolder createHolder() {
                            return new AdvertBannerHolder(banners,tvIndex);
                        }
                    }, banners)
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
            banner.startTurning(3500);
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
        query.findObjects(new FindListener<AdvertNormal>() {
            @Override
            public void done(List<AdvertNormal> list, BmobException e) {
                if (e == null) {
                    setBottom(list);
                } else {
                    ToastUtil.show(e.getMessage());
                }
            }
        });
    }
    private void initComment(){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("postId",advertNormal.getObjectId());
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    setComment(list);
                } else {
                    ToastUtil.show(e.getMessage());
                }
            }
        });
    }
    private void setComment(List<Comment> list){
        if (list == null || list.size() == 0){
            return;
        }
        layoutComment.removeAllViews();
        for (int i=0;i<list.size();i++){
            final Comment comment = list.get(i);
            final View view = LayoutInflater.from(context).inflate(R.layout.activity_square_comment, null);
            CommentHolder holder = new CommentHolder(view);
            if (TextUtil.isNull(comment.getUserPhotoUrl())){
                x.image().bind(holder.ivPhoto, CommonCode.APP_ICON, BitmapUtil.getOptionByRadius(15));
            } else {
                x.image().bind(holder.ivPhoto, comment.getUserPhotoUrl(), BitmapUtil.getOptionByRadius(15));
            }
            holder.tvNickname.setText(comment.getUsername());
            holder.tvTime.setText(comment.getCreatedAt());
            if (!TextUtil.isNull(comment.getLastUserContent())){
                holder.tvReComment.setText(comment.getLastUserContent());
                holder.tvReComment.setVisibility(View.VISIBLE);
            }
            holder.tvToReComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toComment("回复(" + comment.getUsername() + "):" + comment.getContent());
                }
            });
            holder.tvComment.setText(comment.getContent());
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
                x.image().bind(holder.photo, advert.getAdvPhoto().getFileUrl());
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
                    advert.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                        }
                    });
                    Intent intent = new Intent(context, AdvertMsgActivity.class);
                    intent.putExtra("data", advert);
                    context.startActivity(intent);
                    holder.tvPoints.setText("点击量:" + advert.getAdvClicked());
                }
            });
            layoutBottom.addView(view);
        }
    }
    public void checkCameraPermission() {
        String permission = Manifest.permission.CALL_PHONE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context, permission)) {
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    ToastUtil.show("权限被关闭,请打开电话权限");
                    Intent intentSet = new Intent();
                    intentSet.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intentSet.setData(uri);
                    context.startActivity(intentSet);
                    return;
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + advertNormal.getAdvPhoneNumber()));
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + advertNormal.getAdvPhoneNumber()));
            context.startActivity(intent);
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
            checkCameraPermission();
        } else if(v.getId() == R.id.iv_advertmsg_back){
            context.finish();
        } else if (v.getId() == R.id.tv_advertdetail_map){
            Intent intent = new Intent(context, LBSActivity.class);
            context.startActivity(intent);
        } else if (v.getId() == R.id.tv_advertdetail_tocomment){
            toComment("");
        }
    }
    private void toComment(String s) {
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (TextUtil.isNull(nickname)){
            ToastUtil.show("请先登录");
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context,CommentActivity.class);
            intent.putExtra(CommonCode.INTENT_COMMENT_POSTID, advertNormal.getObjectId());
            intent.putExtra(CommonCode.INTENT_COMMENT_LASTCONTENT,s);
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
    class CommentHolder{
        ImageView ivPhoto;
        TextView tvToReComment;
        TextView tvNickname;
        TextView tvTime;
        TextView tvReComment;
        TextView tvComment;
        public CommentHolder(View view){
            ivPhoto = (ImageView) view.findViewById(R.id.iv_square_comment_photo);
            tvToReComment = (TextView) view.findViewById(R.id.tv_square_comment_recomment);
            tvNickname = (TextView) view.findViewById(R.id.tv_square_comment_nickname);
            tvTime = (TextView) view.findViewById(R.id.tv_square_comment_time);
            tvReComment = (TextView) view.findViewById(R.id.tv_square_comment_recontent);
            tvComment = (TextView) view.findViewById(R.id.tv_square_comment_content);
        }
    }
}