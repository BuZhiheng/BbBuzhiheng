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
import org.xutils.image.ImageOptions;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsMsgActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.model.JuheApiResult;
import cn.lankao.com.lovelankao.model.LanKaoNews;
import cn.lankao.com.lovelankao.model.MainService;
import cn.lankao.com.lovelankao.model.ReadNews;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by BuZhiheng on 2016/5/13.
 */
public class IndexAdapter {
    private final String urlRead = "http://v.juhe.cn/toutiao/index?type=top&key=7a20bb53e95c5a8b6694109b65774692&ps=" + CommonCode.RV_ITEMS_COUT+"&pno="+1;;
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
        optionService = BitmapUtil.getOptionCommonRadius();
        optionHead = BitmapUtil.getOptionCommon();
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
        query.findObjects(new FindListener<MainService>() {
            @Override
            public void done(List<MainService> list, BmobException e) {
                if (e == null){
                    setService(list);
                    if (listener != null){
                        listener.success();
                    }
                }
            }
        });
        //加载兰考新闻
        BmobQuery<LanKaoNews> queryNews = new BmobQuery<>();
        queryNews.setLimit(CommonCode.RV_ITEMS_COUT20);
        queryNews.order("-newsTime");
        queryNews.findObjects(new FindListener<LanKaoNews>() {
            @Override
            public void done(List<LanKaoNews> list, BmobException e) {
                if (e == null){
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
//                ToastUtil.show(lkNews.size()+"");
                    convenientBanner.notifyDataSetChanged();
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
                                JsonElement list = res.getResult().getAsJsonObject().getAsJsonArray("data");
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
                x.image().bind(holder.ivPhoto,advert.getFile().getFileUrl(),optionService);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE,advert.getTitle());
                    intent.putExtra(CommonCode.INTENT_SETTING_URL,advert.getUrl());
                    intent.putExtra(CommonCode.INTENT_SHARED_DESC,advert.getTitle());
                    if (advert.getFile() != null){
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG,advert.getFile().getFileUrl());
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
                x.image().bind(holder.photo,news.getNewsImg(),optionHead);
            }
            holder.tvTitle.setText(news.getNewsTitle());
            holder.tvTime.setText(news.getNewsTime());
//            holder.tvFrom.setText("来自:"+news.getNewsFrom());
            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtil.isNull(news.getNewsFromUrl())){
                        Intent intent = new Intent(context, LKNewsMsgActivity.class);
                        intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, news);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra(CommonCode.INTENT_ADVERT_TITLE,"文章详情");
                        intent.putExtra(CommonCode.INTENT_SETTING_URL,news.getNewsFromUrl());
                        intent.putExtra(CommonCode.INTENT_SHARED_DESC,news.getNewsContent());
                        if (news.getNewsPhoto() != null){
                            intent.putExtra(CommonCode.INTENT_SHARED_IMG,news.getNewsPhoto().getFileUrl());
                        } else {
                            intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                        }
                        context.startActivity(intent);
                    }
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
            if (news.getThumbnail_pic_s() != null){
                x.image().bind(holder.photo, news.getThumbnail_pic_s());
            }
            holder.tvTitle.setText(news.getTitle());
            holder.tvSource.setText(news.getAuthor_name());
            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "文章详情");
                    intent.putExtra(CommonCode.INTENT_SETTING_URL, news.getUrl());
                    intent.putExtra(CommonCode.INTENT_SHARED_DESC,news.getTitle());
                    if (news.getThumbnail_pic_s() != null){
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG,news.getThumbnail_pic_s());
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
                x.image().bind(iv, data.getNewsImg(),optionHead);
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
                        intent.putExtra(CommonCode.INTENT_SHARED_IMG, data.getNewsPhoto().getFileUrl());
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
