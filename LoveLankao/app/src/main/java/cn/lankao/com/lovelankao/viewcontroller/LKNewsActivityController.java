package cn.lankao.com.lovelankao.viewcontroller;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.adapter.LKNewsAdapter;
import cn.lankao.com.lovelankao.entity.LanKaoNews;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class LKNewsActivityController implements SwipeRefreshLayout.OnRefreshListener {
    private LKNewsActivity context;
    private RecyclerView rv;
    private SwipeRefreshLayout refresh;
    private LKNewsAdapter adapter;
    public LKNewsActivityController(LKNewsActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        BmobQuery<LanKaoNews> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<LanKaoNews>() {
            @Override
            public void onSuccess(List<LanKaoNews> list) {
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initView() {
        context.setContentView(R.layout.activity_lknews);
        adapter = new LKNewsAdapter(context);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_lknews_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rv = (RecyclerView) context.findViewById(R.id.rv_lknews_activity);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
