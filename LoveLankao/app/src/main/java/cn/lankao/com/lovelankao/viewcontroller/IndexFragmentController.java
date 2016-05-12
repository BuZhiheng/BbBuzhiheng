package cn.lankao.com.lovelankao.viewcontroller;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.JsonElement;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.activity.LKNewsMsgActivity;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.activity.TopActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.entity.LanKaoNews;
import cn.lankao.com.lovelankao.entity.MainService;
import cn.lankao.com.lovelankao.entity.ReadNews;
import cn.lankao.com.lovelankao.entity.Top;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/5/11.
 */
public class IndexFragmentController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final String urlHots = "http://www.tngou.net/api/top/list?rows=" + CommonCode.RV_ITEMS_COUT+"&page="+1;
    private final String urlRead = "http://v.juhe.cn/weixin/query?key=8853be3881b48cb96d20ba3c347640cd&ps=" + CommonCode.RV_ITEMS_COUT+"&pno="+1;;
    private Context context;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private List<String> networkImages;
    private String[] images = {
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };
    private LinearLayout llMenus;
    private LinearLayout llNews;
    private LinearLayout llHots;
    private LinearLayout llRead;

    private List<MainService> service;
    public IndexFragmentController(Context context, View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        x.view().inject((Activity) context);
        service = new ArrayList<>();
        networkImages= Arrays.asList(images);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_index_frm);
        refreshLayout.setOnRefreshListener(this);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.banner_indexfrm);
        llMenus = (LinearLayout) view.findViewById(R.id.ll_indexfrm_service);
        llNews = (LinearLayout) view.findViewById(R.id.ll_indexfrm_news);
        llHots = (LinearLayout) view.findViewById(R.id.ll_indexfrm_hots);
        llRead = (LinearLayout) view.findViewById(R.id.ll_indexfrm_read);
        view.findViewById(R.id.fl_indexfrm_more_news).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_hots).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_read).setOnClickListener(this);
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, networkImages)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        convenientBanner.startTurning(8000);
    }
    private void initData() {
        //加载服务菜单
        BmobQuery<MainService> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<MainService>() {
            @Override
            public void onSuccess(List<MainService> list) {
                setService(list);
                refreshLayout.setRefreshing(false);
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
                setNews(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
            }
        });
        //加载热点新闻
        OkHttpUtil.getApi(urlHots)
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
                        if (res != null) {
                            try {
                                JsonElement tngou = res.getTngou();
                                List<Top> data = GsonUtil.jsonToList(tngou, Top.class);
                                setHots(data);
                            } catch (Exception e) {
                            }
                        }
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
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_indexfrm_item_title);
            holder.tvTitle.setText(advert.getTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE,advert.getTitle());
                    intent.putExtra(CommonCode.INTENT_SETTING_URL,advert.getUrl());
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
    private void setHots(List<Top> data){
        llHots.removeAllViews();
        for (int i=0;i<data.size();i++){
            final ViewHolder holder = new ViewHolder();
            final Top news = data.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_top_item, null);
            holder.fl = (FrameLayout) view.findViewById(R.id.fl_top_content);
            holder.photo = (ImageView) view.findViewById(R.id.iv_top_item_photo);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_top_item_title);
            holder.tvFrom = (TextView) view.findViewById(R.id.tv_top_item_from);
            if (news.getImg() != null){
                x.image().bind(holder.photo, "http://tnfs.tngou.net/image"+news.getImg());
            }
            holder.tvTitle.setText(news.getTitle());
            holder.tvFrom.setText(news.getFromname());
            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "文章详情");
                    intent.putExtra(CommonCode.INTENT_SETTING_URL, news.getFromurl());
                    context.startActivity(intent);
                }
            });
            llHots.addView(view);
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
                    context.startActivity(intent);
                }
            });
            llRead.addView(view);
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_indexfrm_more_news:
                Intent intentNews = new Intent(context, LKNewsActivity.class);
                context.startActivity(intentNews);
                break;
            case R.id.fl_indexfrm_more_hots:
                Intent intentHots = new Intent(context, TopActivity.class);
                context.startActivity(intentHots);
                break;
            case R.id.fl_indexfrm_more_read:
                Intent intentRead = new Intent(context, ReadWeixinActivity.class);
                context.startActivity(intentRead);
                break;
            default:
                break;
        }
    }

    class ViewHolder{
        LinearLayout linearLayout;
        TextView tvTitle;

        FrameLayout fl;
        ImageView photo;
        TextView tvTime;
        TextView tvFrom;
        TextView tvSource;
    }
    public class ImageHolderView implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            x.image().bind(imageView, data);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击事件
                    Toast.makeText(view.getContext(), "点击了第" + (position + 1) + "图片", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
