package cn.lankao.com.lovelankao.viewcontroller;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.adapter.LKNewsAdapter;
import cn.lankao.com.lovelankao.entity.LanKaoNews;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class LKNewsActivityController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private LKNewsActivity context;
    private RecyclerView rv;
    private SwipeRefreshLayout refresh;
    private LKNewsAdapter adapter;
    private ProgressDialog dialog;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public LKNewsActivityController(LKNewsActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        BmobQuery<LanKaoNews> query = new BmobQuery<>();
        if (isRefresh){
            query.setLimit(CommonCode.RV_ITEMS_COUT);
            query.setSkip(0);
        }else{
            query.setLimit(cout);
            query.setSkip(0);
        }
        query.order("-createdAt");
        query.addWhereNotEqualTo("newsType","1");
        query.findObjects(context, new FindListener<LanKaoNews>() {
            @Override
            public void onSuccess(List<LanKaoNews> list) {
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
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
                refresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        context.setContentView(R.layout.activity_lknews);
        context.findViewById(R.id.iv_lknews_back).setOnClickListener(this);
        adapter = new LKNewsAdapter(context);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_lknews_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rv = (RecyclerView) context.findViewById(R.id.rv_lknews_activity);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new OnRvScrollListener(){
            @Override
            public void toBottom() {
                if (canLoadMore){
                    isRefresh = false;
                    canLoadMore = false;
                    cout += CommonCode.RV_ITEMS_COUT;
                    initData();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        cout = CommonCode.RV_ITEMS_COUT;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_lknews_back:
                context.finish();
                break;
        }
    }
}
