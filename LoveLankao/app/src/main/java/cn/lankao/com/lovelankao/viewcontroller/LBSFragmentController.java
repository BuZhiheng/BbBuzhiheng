package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class LBSFragmentController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private MyAdapter adapter;
    private ImageView imageView;

    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public LBSFragmentController(Context context,View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        adapter = new MyAdapter(context);
        imageView = (ImageView) view.findViewById(R.id.iv_lbsfrm_map);
        refresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_lbs_fragment);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lbs_frm);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnRvScrollListener() {
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
        imageView.setOnClickListener(this);
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
        query.findObjects(context, new FindListener<AdvertNormal>() {
            @Override
            public void onSuccess(List<AdvertNormal> list) {
                adapter.setData(list);
                if (cout > list.size()){//请求个数大于返回个数,加载完毕,不能加载更多了
                    canLoadMore = false;
                    ToastUtil.show("加载完毕!");
                }else{
                    canLoadMore = true;
                }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == imageView){
            Intent intent = new Intent(context, LBSActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        cout = CommonCode.RV_ITEMS_COUT;
        initData();
    }
}
