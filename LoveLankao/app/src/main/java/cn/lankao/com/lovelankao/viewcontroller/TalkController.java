package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.activity.CookActivity;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.activity.SquareSendActivity;
import cn.lankao.com.lovelankao.activity.TopActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.adapter.SquareAdapter;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;

/**
 * Created by BuZhiheng on 2016/5/20.
 */
public class TalkController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private SquareAdapter adapter;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public TalkController(Context context,View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initData() {
        BmobQuery<Square> query = new BmobQuery<>();
        if (isRefresh){
            query.setLimit(CommonCode.RV_ITEMS_COUT);
            query.setSkip(0);
        }else{
            query.setLimit(cout);
            query.setSkip(0);
        }
        query.order("-createdAt");//按事件排序
        query.findObjects(context,new FindListener<Square>(){
            @Override
            public void onSuccess(List<Square> list) {
                adapter.setData(list);
                if (list == null || list.size() == 0){
                }else{
                    if (cout > list.size()){//请求个数大于返回个数,加载完毕,不能加载更多了
                        canLoadMore = false;
                    }else{
                        canLoadMore = true;
                    }
                }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
                refresh.setRefreshing(false);
            }
        });
    }
    private void initView() {
        adapter = new SquareAdapter(context);
        refresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_square_frm);
        refresh.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_square_frm);
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
        view.findViewById(R.id.iv_talkfrm_send).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_talkfrm_send:
                Intent intentSend = new Intent(context, SquareSendActivity.class);
                context.startActivity(intentSend);
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
