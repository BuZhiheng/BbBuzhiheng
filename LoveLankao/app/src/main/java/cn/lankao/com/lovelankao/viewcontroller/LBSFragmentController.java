package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
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

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class LBSFragmentController implements View.OnClickListener{
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ImageView imageView;
    public LBSFragmentController(Context context,View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        adapter = new MyAdapter(context);
        imageView = (ImageView) view.findViewById(R.id.iv_lbsfrm_map);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lbs_frm);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        imageView.setOnClickListener(this);
    }
    private void initData() {
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
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
        if(v == imageView){
            Intent intent = new Intent(context, LBSActivity.class);
            context.startActivity(intent);
        }
    }
}
