package cn.lankao.com.lovelankao.viewcontroller;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.activity.CookActivity;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.activity.TopActivity;
import cn.lankao.com.lovelankao.adapter.IndexAdapter;
import cn.lankao.com.lovelankao.utils.CommonCode;
/**
 * Created by BuZhiheng on 2016/5/11.
 */
public class IndexFragmentController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private Context context;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private HorizontalScrollView scrollView;
    private IndexAdapter adapter;
    public IndexFragmentController(Context context, View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        x.view().inject((Activity) context);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.scv_indexfrm_service);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_index_frm);
        refreshLayout.setOnRefreshListener(this);
        view.findViewById(R.id.fl_indexfrm_more_news).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_read).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_chat).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_menu).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_eat).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_jock).setOnClickListener(this);
    }
    private void initData() {
        adapter = new IndexAdapter(context, view, new IndexAdapter.OnReloadListener() {
            @Override
            public void success() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onRefresh() {
        adapter.reload();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_indexfrm_more_news:
                Intent intentNews = new Intent(context, LKNewsActivity.class);
                context.startActivity(intentNews);
                break;
            case R.id.fl_indexfrm_more_read:
                Intent intentRead = new Intent(context, ReadWeixinActivity.class);
                context.startActivity(intentRead);
                break;
            case R.id.fl_indexfrm_more_chat:
                Intent intentChat = new Intent(context, ChatRoomActivity.class);
                context.startActivity(intentChat);
                break;
            case R.id.fl_indexfrm_more_menu:
                Intent intentMenu = new Intent(context, CookActivity.class);
                intentMenu.putExtra(CommonCode.INTENT_COOK_OR_FOOD,CommonCode.INTENT_COOK);
                context.startActivity(intentMenu);
                break;
            case R.id.fl_indexfrm_more_eat:
                Intent intentEat = new Intent(context, CookActivity.class);
                intentEat.putExtra(CommonCode.INTENT_COOK_OR_FOOD,CommonCode.INTENT_FOOD);
                context.startActivity(intentEat);
                break;
            case R.id.fl_indexfrm_more_jock:
                Intent intentJock = new Intent(context, JockActivity.class);
                context.startActivity(intentJock);
                break;
            default:
                break;
        }
    }
}