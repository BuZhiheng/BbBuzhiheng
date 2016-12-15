package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.activity.AllBusinessActivity;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.CommonCode;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MainFragmentController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private View view;
    private SwipeRefreshLayout refresh;
    private LinearLayout layoutBottom;
    public MainFragmentController(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        layoutBottom = (LinearLayout) view.findViewById(R.id.ll_mainfrm_bottom);
        refresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_main_frm);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);

        view.findViewById(R.id.ll_mainfrm_header_chihewanle).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_women).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_offer).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_zulin).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_friend).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_hunqing).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_fangchan).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_service).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_jingcailankao).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_other).setOnClickListener(this);

        view.findViewById(R.id.ll_mainfrm_header_mingqi).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_tehui).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_tuiian).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_toall).setOnClickListener(this);
    }

    private void initData() {
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.order("-advClicked");
        query.addWhereEqualTo("advIndex",CommonCode.ADVERT_INDEX);
        query.findObjects(new FindListener<AdvertNormal>() {
            @Override
            public void done(List<AdvertNormal> list, BmobException e) {
                if (e == null){
                    refresh.setRefreshing(false);
                    setBottom(list);
                } else {
                    refresh.setRefreshing(false);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_mainfrm_toall:
                Intent intent = new Intent(context, AllBusinessActivity.class);
                context.startActivity(intent);
                break;
            case R.id.ll_mainfrm_header_mingqi:
                toAdvert(CommonCode.ADVERT_MINGQI, "名企名商");
                break;
            case R.id.tv_mainfrm_tehui:
                toAdvert(CommonCode.ADVERT_TEHUI, "特惠不打烊");
                break;
            case R.id.tv_mainfrm_tuiian:
                toAdvert(CommonCode.ADVERT_TUIJIAN, "精品推荐");
                break;
            case R.id.ll_mainfrm_header_chihewanle:
                toAdvert(CommonCode.ADVERT_CHIHEWANLE,context.getString(R.string.text_mainfrm_chihewanle));
                break;
            case R.id.ll_mainfrm_header_women:
                toAdvert(CommonCode.ADVERT_WOMEN,context.getString(R.string.text_mainfrm_women));
                break;
            case R.id.ll_mainfrm_header_offer:
                toAdvert(CommonCode.ADVERT_OFFER,context.getString(R.string.text_mainfrm_offer));
                break;
            case R.id.ll_mainfrm_header_zulin:
                toAdvert(CommonCode.ADVERT_ZULIN,context.getString(R.string.text_mainfrm_zulin));
                break;
            case R.id.ll_mainfrm_header_friend:
                toAdvert(CommonCode.ADVERT_FRIEND,context.getString(R.string.text_mainfrm_friend));
                break;
            case R.id.ll_mainfrm_header_hunqing:
                toAdvert(CommonCode.ADVERT_HUNQING,context.getString(R.string.text_mainfrm_hunqing));
                break;
            case R.id.ll_mainfrm_header_fangchan:
                toAdvert(CommonCode.ADVERT_FANGCHAN,context.getString(R.string.text_mainfrm_fangchan));
                break;
            case R.id.ll_mainfrm_header_service:
                toAdvert(CommonCode.ADVERT_SERVICE,context.getString(R.string.text_mainfrm_service));
                break;
            case R.id.ll_mainfrm_header_jingcailankao:
                toAdvert(CommonCode.ADVERT_JINGCAILANKAO, context.getString(R.string.text_mainfrm_jingcai));
                break;
            case R.id.ll_mainfrm_header_other:
                toAdvert(CommonCode.ADVERT_OTHER,context.getString(R.string.text_mainfrm_other));
                break;
            default:
                break;
        }
    }
    private void toAdvert(int code,String title){
        Intent intent = new Intent(context, AdvertDetailActivity.class);
        intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, title);
        intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, code);
        context.startActivity(intent);
    }
    private void setBottom(List<AdvertNormal> list) {
        if (list == null){
            return;
        }
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
