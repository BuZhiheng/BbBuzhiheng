package cn.lankao.com.lovelankao.viewcontroller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonElement;

import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.activity.TopActivity;
import cn.lankao.com.lovelankao.adapter.ReadAdapter;
import cn.lankao.com.lovelankao.adapter.TopAdapter;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.entity.ReadNews;
import cn.lankao.com.lovelankao.entity.Top;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class TopActivityController {
    private TopActivity context;
    private RecyclerView rv;
    private TopAdapter adapter;
    public TopActivityController(TopActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtil.getApi("http://www.tngou.net/api/top/list?rows=50")
                .subscribeOn(Schedulers.io())// 在非UI线程中执行getUser
                .observeOn(AndroidSchedulers.mainThread())// 在UI线程中执行结果
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
                        if (res != null) {
                            try {
                                JsonElement list = res.getTngou();
                                List<Top> data = GsonUtil.jsonToList(list, Top.class);
                                adapter.setData(data);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }

    private void initView() {
        context.setContentView(R.layout.activity_top);
        adapter = new TopAdapter(context);
        rv = (RecyclerView) context.findViewById(R.id.rv_top_activity);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }
}
