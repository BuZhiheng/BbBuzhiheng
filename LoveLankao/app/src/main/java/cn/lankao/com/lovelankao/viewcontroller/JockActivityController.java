package cn.lankao.com.lovelankao.viewcontroller;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonElement;

import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.adapter.JockAdapter;
import cn.lankao.com.lovelankao.entity.Jock;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class JockActivityController implements SwipeRefreshLayout.OnRefreshListener {
    private JockActivity context;
    private RecyclerView rv;
    private SwipeRefreshLayout refresh;
    private JockAdapter adapter;
    private List<Jock> data;
    public JockActivityController(JockActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtil.getApi("http://japi.juhe.cn/joke/content/list.from?key=da46a2a9e5d5a3bfefb5694bfa0e04c1&pagesize=20&sort=asc&time=0000000000")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                JuheApiResult res = GsonUtil.jsonToObject(s, JuheApiResult.class);
                if (res.getError_code() == 0){
                    try{
                        JsonElement list = res.getResult().getAsJsonObject().getAsJsonArray("data");
                        List<Jock> data = GsonUtil.jsonToList(list,Jock.class);
                        adapter.setData(data);
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }catch (Exception e){
                    }
                }
            }
        });
    }

    private void initView() {
        adapter = new JockAdapter(context);
        context.setContentView(R.layout.activity_jock);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_jock_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rv = (RecyclerView) context.findViewById(R.id.rv_activity_jock);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
