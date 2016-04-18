package cn.lankao.com.lovelankao.viewcontroller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonElement;

import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.adapter.ReadAdapter;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.entity.ReadNews;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class ReadActivityController {
    private ReadWeixinActivity context;
    private RecyclerView rv;
    private ReadAdapter adapter;
    public ReadActivityController(ReadWeixinActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtil.getApi("http://v.juhe.cn/weixin/query?key=8853be3881b48cb96d20ba3c347640cd&ps=100")
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
                        JuheApiResult res = GsonUtil.jsonToObject(s,JuheApiResult.class);
                        if (res.getError_code() == 0){
                            try{
                                JsonElement list = res.getResult().getAsJsonObject().getAsJsonArray("list");
                                List<ReadNews> data = GsonUtil.jsonToList(list,ReadNews.class);
                                adapter.setData(data);
                                adapter.notifyDataSetChanged();
                            }catch (Exception e){
                            }
                        }
                    }
                });
    }

    private void initView() {
        context.setContentView(R.layout.activity_read_weixin);
        adapter = new ReadAdapter(context);
        rv = (RecyclerView) context.findViewById(R.id.rv_read_activity);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }
}
