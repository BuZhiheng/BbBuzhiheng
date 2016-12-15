package cn.lankao.com.lovelankao.viewcontroller;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;
/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertDetailController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private AdvertDetailActivity context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private MyAdapter adapter;
    private TextView tvTitle;

    private ProgressDialog dialog;

    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public AdvertDetailController(AdvertDetailActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        adapter = new MyAdapter(context);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_advertdetail_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        recyclerView = (RecyclerView) context.findViewById(R.id.rv_advert_act);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                if (canLoadMore) {
                    isRefresh = false;
                    canLoadMore = false;
                    cout += CommonCode.RV_ITEMS_COUT;
                    initData();
                }
            }
        });
        tvTitle = (TextView) context.findViewById(R.id.tv_advertlist_title);
        context.findViewById(R.id.iv_advertdetail_back).setOnClickListener(this);
    }
    private void initData() {
        Intent intent = context.getIntent();
        if(intent != null){
            tvTitle.setText(intent.getStringExtra(CommonCode.INTENT_ADVERT_TITLE));
            getAdvert(intent.getIntExtra(CommonCode.INTENT_ADVERT_TYPE,0));
        }
    }
    private void getAdvert(int type){
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        if (type < 100){//一般广告
            query.addWhereEqualTo("advType",type);
        } else if (type == 100){//主页20条
            query.addWhereEqualTo("advIndex",type);
        } else if (type >= 1000){//vip广告或者推送（1005）
            query.addWhereEqualTo("advVipType",type);
        }
        if (isRefresh){
            query.setLimit(CommonCode.RV_ITEMS_COUT);
            query.setSkip(0);
        }else{
            query.setLimit(cout);
            query.setSkip(0);
        }
        query.order("-advClicked");//按点击次数倒序排序
        query.findObjects(new FindListener<AdvertNormal>() {
            @Override
            public void done(List<AdvertNormal> list, BmobException e) {
                if (e == null){
                    adapter.setData(list);
                    if (list == null || list.size() == 0){
                        ToastUtil.show("空空如也!");
                    }else{
                        if (cout > list.size()){//请求个数大于返回个数,加载完毕,不能加载更多了
                            canLoadMore = false;
                        }else{
                            canLoadMore = true;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                    dialog.dismiss();
                } else {
                    ToastUtil.show(e.getMessage());
                    refresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_advertdetail_back:
                context.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        cout = CommonCode.RV_ITEMS_COUT;
        initData();
    }
}
