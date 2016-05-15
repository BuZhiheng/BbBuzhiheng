package cn.lankao.com.lovelankao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.JsonElement;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsMsgActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.entity.LanKaoNews;
import cn.lankao.com.lovelankao.entity.MainService;
import cn.lankao.com.lovelankao.entity.ReadNews;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/5/13.
 */
public class IndexAdapter {
    private final String urlRead = "http://v.juhe.cn/weixin/query?key=8853be3881b48cb96d20ba3c347640cd&ps=" + CommonCode.RV_ITEMS_COUT+"&pno="+1;;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private ImageOptions optionService;
    private ImageOptions optionHead;
    private List<LanKaoNews> lkNews;
    private Context context;
    private LinearLayout llMenus;
    private LinearLayout llNews;
    private LinearLayout llRead;
    private OnReloadListener listener;
    public IndexAdapter(Context context, View view, OnReloadListener listener){
        this.context = context;
        this.listener = listener;
        lkNews = new ArrayList<>();
        optionService = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(30))
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        optionHead = new ImageOptions.Builder()
                .setCrop(false)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
        llMenus = (LinearLayout) view.findViewById(R.id.ll_indexfrm_service);
        llNews = (LinearLayout) view.findViewById(R.id.ll_indexfrm_news);
        llRead = (LinearLayout) view.findViewById(R.id.ll_indexfrm_read);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.banner_indexfrm);
        //                自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, lkNews)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        convenientBanner.startTurning(5000);
        initData();
    }
    private void initData(){
        //加载服务菜单
        BmobQuery<MainService> query = new BmobQuery<>();
        query.order("index");
        query.findObjects(context, new FindListener<MainService>() {
            @Override
            public void onSuccess(List<MainService> list) {
                setService(list);
                if (listener != null){
                    listener.success();
                }
            }
            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
            }
        });
        //加载兰考新闻
        BmobQuery<LanKaoNews> queryNews = new BmobQuery<>();
        queryNews.setLimit(CommonCode.RV_ITEMS_COUT);
        queryNews.order("-createdAt");
        queryNews.findObjects(context, new FindListener<LanKaoNews>() {
            @Override
            public void onSuccess(List<LanKaoNews> list) {
                List<LanKaoNews> data = new ArrayList<>();
                lkNews.clear();
                for (int i = 0; i < list.size(); i++) {
                    LanKaoNews news = list.get(i);
                    if ("1".equals(news.getNewsType())) {
                        lkNews.add(news);
                    } else {
                        data.add(news);
                    }
                }
                setNews(data);
                convenientBanner.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
            }
        });
        //加载微信阅读
        OkHttpUtil.getApi(urlRead)
                .subscribeOn(Schedulers.io())// 在非UI线程中执行getUser
                .observeOn(AndroidSchedulers.mainThread())// 在UI线程中执行结果
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        JuheApiResult res = GsonUtil.jsonToObject(s, JuheApiResult.class);
                        if (res.getError_code() == 0) {
                            try {
                                JsonElement list = res.getResult().getAsJsonObject().getAsJsonArray("list");
                                List<ReadNews> data = GsonUtil.jsonToList(list, ReadNews.class);
                                setRead(data);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }
    private void setService(List<MainService> list) {
        llMenus.removeAllViews();
        for (int i=0;i<list.size();i++){
            final ViewHolder holder = new ViewHolder();
            final MainService advert = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_index_item, null);
            holder.linearLayout = (LinearLayout) view.findViewById(R.id.ll_indexfrm_item);
            holder.ivPhoto = (ImageView) view.findViewById(R.id.iv_indexfrm_item_service_title);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_indexfrm_item_title);
            holder.tvTitle.setText(advert.getTitle());
            if (advert.getFile() != null){
                x.image().bind(holder.ivPhoto,advert.getFile().getFileUrl(context),optionService);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE,advert.getTitle());
                    intent.putExtra(CommonCode.INTENT_SETTING_URL,advert.getUrl());
                    intent.putExtra(CommonCode.INTENT_SHARED_DESC,advert.getTitle());
                    if (advert.getFile() != null){
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG,advert.getFile().getFileUrl(context));
                    } else {
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                    }
                    context.startActivity(intent);
                }
            });
            llMenus.addView(view);
        }
    }
    private void setNews(List<LanKaoNews> list){
        llNews.removeAllViews();
        for (int i=0;i<list.size();i++){
            final ViewHolder holder = new ViewHolder();
            final LanKaoNews news = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_lknews_item, null);
            holder.fl = (FrameLayout) view.findViewById(R.id.fl_lknews_content);
            holder.photo = (ImageView) view.findViewById(R.id.iv_lknews_item_photo);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_lknews_item_title);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_lknews_item_time);
            holder.tvFrom = (TextView) view.findViewById(R.id.tv_lknews_item_from);
            if (news.getNewsImg() != null){
                x.image().bind(holder.photo,news.getNewsImg());
            }
            holder.tvTitle.setText(news.getNewsTitle());
            holder.tvTime.setText(news.getNewsTime());
//            holder.tvFrom.setText("来自:"+news.getNewsFrom());
            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LKNewsMsgActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, news);
                    context.startActivity(intent);
                }
            });
            llNews.addView(view);
        }
    }
    private void setRead(List<ReadNews> data){
        llRead.removeAllViews();
        for (int i=0;i<data.size();i++){
            final ViewHolder holder = new ViewHolder();
            final ReadNews news = data.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_read_item, null);
            holder.fl = (FrameLayout) view.findViewById(R.id.fl_read_content);
            holder.photo = (ImageView) view.findViewById(R.id.iv_readact_item_photo);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_readact_item_title);
            holder.tvSource = (TextView) view.findViewById(R.id.tv_readact_item_source);
            if (news.getFirstImg() != null){
                x.image().bind(holder.photo, news.getFirstImg());
            }
            holder.tvTitle.setText(news.getTitle());
            holder.tvSource.setText(news.getSource());
            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "文章详情");
                    intent.putExtra(CommonCode.INTENT_SETTING_URL, news.getUrl());
                    intent.putExtra(CommonCode.INTENT_SHARED_DESC,news.getTitle());
                    if (news.getFirstImg() != null){
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG,news.getFirstImg());
                    } else {
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                    }
                    context.startActivity(intent);
                }
            });
            llRead.addView(view);
        }
    }
    public class ImageHolderView implements Holder<LanKaoNews> {
        private View view;
        private FrameLayout fLayout;
        private ImageView iv;
        private TextView tv;
        private TextView tvIndex;
        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_index_head,null);
            fLayout = (FrameLayout) view.findViewById(R.id.fl_indexfrm_head);
            iv = (ImageView) view.findViewById(R.id.iv_indexfrm_headview_photo);
            tv = (TextView) view.findViewById(R.id.tv_indexfrm_headview_title);
            tvIndex = (TextView) view.findViewById(R.id.tv_indexfrm_headview_index);
            return view;
        }
        @Override
        public void UpdateUI(final Context context, final int position, final LanKaoNews data) {
            if (data.getNewsPhoto() != null){
                x.image().bind(iv, data.getNewsPhoto().getFileUrl(context),optionHead);
            }
            tv.setText(data.getNewsTitle());
            tvIndex.setText((position+1)+"/"+lkNews.size());
            fLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击事件
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "");
                    intent.putExtra(CommonCode.INTENT_SETTING_URL, data.getNewsFromUrl());
                    intent.putExtra(CommonCode.INTENT_SHARED_DESC,data.getNewsTitle());
                    if (data.getNewsPhoto() != null){
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG, data.getNewsPhoto().getFileUrl(context));
                    } else {
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                    }
                    context.startActivity(intent);
                }
            });
        }
    }
    public void reload(){
        initData();
    }
    public interface OnReloadListener{
        void success();
    }
    class ViewHolder{
        LinearLayout linearLayout;
        TextView tvTitle;
        FrameLayout fl;
        ImageView photo;
        ImageView ivPhoto;
        TextView tvTime;
        TextView tvFrom;
        TextView tvSource;
    }
}
