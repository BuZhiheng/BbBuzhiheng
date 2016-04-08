package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class AdvertDetailController implements View.OnClickListener {
    private AdvertDetailActivity context;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private TextView tvTitle;
    public AdvertDetailController(AdvertDetailActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initView() {
        adapter = new MyAdapter(context);
        recyclerView = (RecyclerView) context.findViewById(R.id.rv_advert_act);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        tvTitle = (TextView) context.findViewById(R.id.tv_advertlist_title);
        context.findViewById(R.id.iv_advertdetail_back).setOnClickListener(this);
    }
    private void initData() {
        Intent intent = context.getIntent();
        if(intent != null){
            tvTitle.setText(intent.getStringExtra(CommonCode.INTENT_ADVERT_TITLE));
            getAdvert(intent.getIntExtra(CommonCode.INTENT_ADVERT_TYPE,0));
        }
    }
    private void getAdvert(int type){
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        if (type < 100){//一般广告
            query.addWhereEqualTo("advType",type);
        } else if (type == 100){//主页20条
            query.addWhereEqualTo("advIndex",type);
        } else if (type >= 1000){//vip广告或者推送（1005）
            query.addWhereEqualTo("advVipType",type);
        }
        query.findObjects(context, new FindListener<AdvertNormal>() {
            @Override
            public void onSuccess(List<AdvertNormal> list) {
                adapter.setData(list);
                adapter.notifyDataSetChanged();
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
            case R.id.iv_advertdetail_back:
                context.finish();
                break;
        }
    }
}
