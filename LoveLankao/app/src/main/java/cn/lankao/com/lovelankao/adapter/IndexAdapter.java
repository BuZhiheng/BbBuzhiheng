package cn.lankao.com.lovelankao.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
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
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private ImageOptions optionHead;
    private List<LanKaoNews> banners;
    private RecyclerView rvService;
    private RecyclerView rvNews;
    private RecyclerViewHeader header;
    private LKNewsAdapter adapter;
    private IndexServiceAdapter adapterService;
    private OnReloadListener listener;
    public IndexAdapter(Context context, View view, OnReloadListener listener){
        this.listener = listener;
        banners = new ArrayList<>();
        optionHead = BitmapUtil.getOptionCommon();
        rvService = (RecyclerView) view.findViewById(R.id.rv_indexfrm_service);
        rvNews = (RecyclerView) view.findViewById(R.id.rv_indexfrm_lknews);
        rvNews.setLayoutManager(new LinearLayoutManager(context));
        header = (RecyclerViewHeader) view.findViewById(R.id.rv_indexfrm_lknews_header);
        adapter = new LKNewsAdapter(context);
        rvNews.setAdapter(adapter);
        header.attachTo(rvNews);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvService.setLayoutManager(manager);
        adapterService = new IndexServiceAdapter(context);
        rvService.setAdapter(adapterService);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.banner_indexfrm);
        //                自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, banners)
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
                    adapterService.setData(list);
                    adapterService.notifyDataSetChanged();
                }
                if (listener != null){
                    listener.success();
                }
            }
        });
        //加载兰考新闻
        BmobQuery<LanKaoNews> queryNews = new BmobQuery<>();
        queryNews.setLimit(CommonCode.RV_ITEMS_COUT20);
        queryNews.order("-createdAt");
        queryNews.findObjects(new FindListener<LanKaoNews>() {
            @Override
            public void done(List<LanKaoNews> list, BmobException e) {
                if (e == null) {
                    List<LanKaoNews> data = new ArrayList<>();
                    banners.clear();
                    for (int i = 0; i < list.size(); i++) {
                        LanKaoNews news = list.get(i);
                        if ("1".equals(news.getNewsType())) {
                            banners.add(news);
                        } else {
                            data.add(news);
                        }
                    }
                    setNews(data);
                    convenientBanner.notifyDataSetChanged();
                }
            }
        });
    }
    private void setNews(List<LanKaoNews> list){
        adapter.setData(list);
        adapter.notifyDataSetChanged();
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
            x.image().bind(iv, data.getNewsImg(),optionHead);
            tv.setText(data.getNewsTitle());
            tvIndex.setText((position + 1) + "/" + banners.size());
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
}