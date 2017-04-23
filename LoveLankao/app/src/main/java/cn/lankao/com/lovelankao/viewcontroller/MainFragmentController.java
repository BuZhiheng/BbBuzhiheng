package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.AllBusinessActivity;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.CommonCode;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MainFragmentController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private View view;
    private SwipeRefreshLayout refresh;
    private RecyclerView rvShop;
    private RecyclerViewHeader header;
    private MyAdapter adapter;
    public MainFragmentController(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        rvShop = (RecyclerView) view.findViewById(R.id.rv_mainfrm_shop);
        rvShop.setLayoutManager(new LinearLayoutManager(context));
        header = (RecyclerViewHeader) view.findViewById(R.id.rv_mainfrm_header);
        header.attachTo(rvShop);
        adapter = new MyAdapter(context);
        rvShop.setAdapter(adapter);
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
        view.findViewById(R.id.fl_mainfrm_more).setOnClickListener(this);
    }
    private void initData() {
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.order("-advClicked");
        query.setLimit(50);
        query.addWhereEqualTo("advIndex",CommonCode.ADVERT_INDEX);
        query.findObjects(new FindListener<AdvertNormal>() {
            @Override
            public void done(List<AdvertNormal> list, BmobException e) {
                if (e == null){
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
                Intent intentLbs = new Intent(context, LBSActivity.class);
                context.startActivity(intentLbs);
                break;
            case R.id.fl_mainfrm_more:
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
        adapter.setData(list);
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void onRefresh() {
        initData();
    }
}
