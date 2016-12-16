package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AllBusinessActivity;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class AllBusinessActivityController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private AllBusinessActivity context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private MyAdapter adapter;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public AllBusinessActivityController(AllBusinessActivity context){
        this.context = context;
        initView();
        initData();
    }
    private void initView() {
        adapter = new MyAdapter(context);
        context.setContentView(R.layout.activity_allbusiess);
        context.findViewById(R.id.iv_allact_tomap).setOnClickListener(this);
        context.findViewById(R.id.iv_allact_back).setOnClickListener(this);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_allact);
        refresh.setOnRefreshListener(this);
        recyclerView = (RecyclerView) context.findViewById(R.id.rv_allact);
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
    }
    private void initData() {
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
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
                } else {
                    ToastUtil.show(e.getMessage());
                    refresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_allact_tomap:
                Intent intent = new Intent(context, LBSActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_allact_back:
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
