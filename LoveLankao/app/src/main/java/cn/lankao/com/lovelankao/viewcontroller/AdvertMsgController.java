package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertMsgController {
    private AdvertMsgActivity context;
    private AdvertNormal advertNormal;
    private ImageView ivPhoto;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvAverge;
    private TextView tvActivite;
    private TextView tvPoints;
    private TextView tvTitleCenter;
    private TextView tvAddress;
    private TextView tvContentMsg;
    private TextView tvPinglun;

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
        tvTitle = (TextView) context.findViewById(R.id.tv_advertdetail_title);
        tvContent = (TextView) context.findViewById(R.id.tv_advertdetail_content);
        tvAverge = (TextView) context.findViewById(R.id.tv_advertdetail_average);
        tvActivite = (TextView) context.findViewById(R.id.tv_advertdetail_activite);
        tvPoints = (TextView) context.findViewById(R.id.tv_advertdetail_points);
        tvTitleCenter = (TextView) context.findViewById(R.id.tv_advertdetail_title_center);
        tvAddress = (TextView) context.findViewById(R.id.tv_advertdetail_address);
        tvContentMsg = (TextView) context.findViewById(R.id.tv_advertdetail_content_msg);
        tvPinglun = (TextView) context.findViewById(R.id.tv_advertdetail_new_pinglun);
    }

    private void initData() {
        if(advertNormal.getAdvPhoto() != null){
            x.image().bind(ivPhoto,advertNormal.getAdvPhoto().getFileUrl(context));
        }
        if (advertNormal.getAdvClicked() == null){
            tvPoints.setText("已点击:0次");
        }else {
            tvPoints.setText("已点击:"+advertNormal.getAdvClicked()+"次");
        }
        tvTitle.setText(advertNormal.getTitle());
        tvContent.setText(advertNormal.getTitleContent());
        tvAverge.setText(advertNormal.getAdvPrice());
        tvActivite.setText(advertNormal.getAdvActivity());
        tvTitleCenter.setText(advertNormal.getTitle());
        tvAddress.setText(advertNormal.getAdvAddress());
        tvContentMsg.setText(advertNormal.getAdvContent());
        tvPinglun.setText(advertNormal.getAdvNewPinglun());
    }
}
