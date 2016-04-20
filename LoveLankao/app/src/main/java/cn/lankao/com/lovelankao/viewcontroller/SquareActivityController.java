package cn.lankao.com.lovelankao.viewcontroller;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.activity.SquareSendActivity;
import cn.lankao.com.lovelankao.adapter.SquareAdapter;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivityController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SquareActivity context;
    private RecyclerView rvSquare;
    private SwipeRefreshLayout refresh;
    private SquareAdapter adapter;
    private ProgressDialog dialog;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public SquareActivityController(SquareActivity context){
        this.context = context;
        EventBus.getDefault().register(this);
        initView();
        initData();
    }
    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        adapter = new SquareAdapter(context);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_square_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rvSquare = (RecyclerView) context.findViewById(R.id.rv_square_room);
        rvSquare.setLayoutManager(new LinearLayoutManager(context));
        rvSquare.setAdapter(adapter);
        rvSquare.addOnScrollListener(new OnRvScrollListener(){
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
        context.findViewById(R.id.iv_square_send).setOnClickListener(this);
        context.findViewById(R.id.iv_square_back).setOnClickListener(this);
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
        query.order("-createdAt");
        query.findObjects(context, new FindListener<Square>() {
            @Override
            public void onSuccess(List<Square> list) {
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
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_square_send:
                Intent intent = new Intent(context, SquareSendActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_square_back:
                context.finish();
                break;
        }
    }
    @Subscribe
    public void onEventMainThread(Square square){
        onRefresh();
    }
    public void onDestroy(){
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        cout = CommonCode.RV_ITEMS_COUT;
        initData();
    }
}
