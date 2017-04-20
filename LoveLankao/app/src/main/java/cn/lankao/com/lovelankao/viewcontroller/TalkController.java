package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
//import cn.lankao.com.lovelankao.activity.SquareMsgActivity;
import cn.lankao.com.lovelankao.activity.SquareSendActivity;
import cn.lankao.com.lovelankao.adapter.SquareAdapter;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
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
    private TextView tvMsg;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public TalkController(Context context,View view){
        EventBus.getDefault().register(this);
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        adapter = new SquareAdapter(context);
        tvMsg = (TextView) view.findViewById(R.id.tv_squarefrm_msg);
        tvMsg.setOnClickListener(this);
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
        onRefresh();
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
        query.findObjects(new FindListener<Square>() {
            @Override
            public void done(List<Square> list, BmobException e) {
                if (e == null) {
                    adapter.setData(list);
                    if (list == null || list.size() == 0) {
                        ToastUtil.show("空空如也!");
                    } else {
                        if (cout > list.size()) {//请求个数大于返回个数,加载完毕,不能加载更多了
                            canLoadMore = false;
                        } else {
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
        int id = v.getId();
        switch (id){
            case R.id.iv_talkfrm_send:
                Intent intentSend = new Intent(context, SquareSendActivity.class);
                context.startActivity(intentSend);
                break;
            case R.id.tv_squarefrm_msg:
                String userId = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
                if (TextUtil.isNull(userId)){
                    return;
                }
                MyUser user = new MyUser();
                user.setCommentMsg(CommonCode.USER_MSG_POST_INIT);
                user.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
//                            Intent intent = new Intent(context, SquareMsgActivity.class);
//                            context.startActivity(intent);
//                            tvMsg.setVisibility(View.GONE);
                        }
                    }
                });
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(Square s){
        onRefresh();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userMsg(MyUser user){
        if (CommonCode.USER_MSG_POST_COMMENT.equals(user.getCommentMsg())){
            tvMsg.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRefresh() {
        isRefresh = true;
        cout = CommonCode.RV_ITEMS_COUT;
        initData();
    }
}