package cn.lankao.com.lovelankao.viewcontroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.CookActivity;
import cn.lankao.com.lovelankao.adapter.CookAdapter;
import cn.lankao.com.lovelankao.entity.Cook;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class CookActivityController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    /**
     * 小吃菜谱,健康食物公用此类
     * */
    private final String URL_COOK = "http://www.tngou.net/api/cook/list?rows=";
    private final String URL_FOOD = "http://www.tngou.net/api/food/list?id=1&rows=";
    private final String TO_URL_COOK = "http://www.tngou.net/cook/show/";
    private final String TO_URL_FOOD = "http://www.tngou.net/food/show/";

    private CookActivity context;
    private RecyclerView rv;
    private SwipeRefreshLayout refresh;
    private TextView tvTitle;
    private CookAdapter adapter;
    private ProgressDialog dialog;
    private String url = URL_COOK;
    private String toUrl = TO_URL_COOK;
    private int page = 1;

    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public CookActivityController(CookActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {//http://www.tngou.net/api/food/list
        String finalUrl;
        if (isRefresh){
            finalUrl = url+CommonCode.RV_ITEMS_COUT+"&page="+1;

        }else{
            finalUrl = url+CommonCode.RV_ITEMS_COUT+"&page="+page;
        }
        OkHttpUtil.getApi(finalUrl)
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
                                JsonElement tngou = res.getTngou();
                                List<Cook> data = GsonUtil.jsonToList(tngou, Cook.class);
                                if (isRefresh){
                                    adapter.setData(data);
                                }else{
                                    adapter.addData(data);
                                }
                                canLoadMore = true;
                                adapter.notifyDataSetChanged();
                                refresh.setRefreshing(false);
                                dialog.dismiss();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        context.setContentView(R.layout.activity_cook);
        context.findViewById(R.id.iv_cook_back).setOnClickListener(this);
        tvTitle = (TextView) context.findViewById(R.id.tv_cookact_title);
        Intent intent = context.getIntent();
        if (intent != null){
            if ((CommonCode.INTENT_COOK).equals(intent.getStringExtra(CommonCode.INTENT_COOK_OR_FOOD))){
                url = URL_COOK;
                toUrl = TO_URL_COOK;
                tvTitle.setText("小吃菜谱");
            }else{
                url = URL_FOOD;
                toUrl = TO_URL_FOOD;
                tvTitle.setText("健康饮食");
            }
        }
        adapter = new CookAdapter(context,toUrl);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_cook_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rv = (RecyclerView) context.findViewById(R.id.rv_cook_activity);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                if (canLoadMore){
                    isRefresh = false;
                    canLoadMore = false;
                    page ++;
                    initData();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cook_back:
                context.finish();
                break;
        }
    }
}

