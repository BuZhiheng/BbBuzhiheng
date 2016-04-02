package cn.lankao.com.lovelankao.viewcontroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertMsgController implements View.OnClickListener {
    private AdvertMsgActivity context;
    private AdvertNormal advertNormal;
    private ImageView ivPhoto;
    private ImageView ivCall;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvAverge;
    private TextView tvActivite;
    private TextView tvPoints;
    private TextView tvTitleCenter;
    private TextView tvAddress;
    private TextView tvContentMsg;
    private TextView tvPinglun;
    private LinearLayout layoutBottom;
    private LinearLayout layoutAddress;

    public AdvertMsgController(AdvertMsgActivity context) {
        this.context = context;
        x.view().inject(context);
        Intent intent = context.getIntent();
        if (intent != null) {
            advertNormal = (AdvertNormal) intent.getSerializableExtra("data");
        }
        initView();
        initData();
    }

    private void initView() {
        ivPhoto = (ImageView) context.findViewById(R.id.iv_advertdetail_photo);
        ivCall = (ImageView) context.findViewById(R.id.iv_advertdetail_call);
        tvTitle = (TextView) context.findViewById(R.id.tv_advertdetail_title);
        tvContent = (TextView) context.findViewById(R.id.tv_advertdetail_content);
        tvAverge = (TextView) context.findViewById(R.id.tv_advertdetail_average);
        tvActivite = (TextView) context.findViewById(R.id.tv_advertdetail_activite);
        tvPoints = (TextView) context.findViewById(R.id.tv_advertdetail_points);
        tvTitleCenter = (TextView) context.findViewById(R.id.tv_advertdetail_title_center);
        tvAddress = (TextView) context.findViewById(R.id.tv_advertdetail_address);
        tvContentMsg = (TextView) context.findViewById(R.id.tv_advertdetail_content_msg);
        tvPinglun = (TextView) context.findViewById(R.id.tv_advertdetail_new_pinglun);
        layoutBottom = (LinearLayout) context.findViewById(R.id.ll_advertmsg_bottom);
        layoutAddress = (LinearLayout) context.findViewById(R.id.ll_advertdetail_address);
        layoutAddress.setOnClickListener(this);
        ivCall.setOnClickListener(this);
    }

    private void initData() {
        if (advertNormal.getAdvPhoto() != null) {
            x.image().bind(ivPhoto, advertNormal.getAdvPhoto().getFileUrl(context));
        }
        if (advertNormal.getAdvClicked() == null) {
            tvPoints.setText("已点击:0次");
        } else {
            tvPoints.setText("已点击:" + advertNormal.getAdvClicked() + "次");
        }
        tvTitle.setText(advertNormal.getTitle());
        tvContent.setText(advertNormal.getTitleContent());
        tvAverge.setText(advertNormal.getAdvPrice());
        tvActivite.setText(advertNormal.getAdvActivity());
        tvTitleCenter.setText(advertNormal.getTitle());
        tvAddress.setText(advertNormal.getAdvAddress());
        tvContentMsg.setText(advertNormal.getAdvContent());
        tvPinglun.setText(advertNormal.getAdvNewPinglun());
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.addWhereEqualTo("advVipType", 1008);
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

        } else if (v == ivCall) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + advertNormal.getAdvPhoneNumber()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        }
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
