package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ActiveActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MainFragmentController implements View.OnClickListener{
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewHeader header;
    private MyAdapter adapter;
    private LinearLayout layoutConpon;
    private TextView tvOpen;
    private TextView tvTuijian;
    public MainFragmentController(Context context,View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
    }
    private void initView() {
        adapter = new MyAdapter(context);
        header = RecyclerViewHeader.fromXml(context, R.layout.fragment_rv_header);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main_frm);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        header.attachTo(recyclerView);
        layoutConpon = (LinearLayout) view.findViewById(R.id.ll_mainfrm_header_conpon);
        tvOpen = (TextView) view.findViewById(R.id.tv_minfrm_open);
        tvTuijian = (TextView) view.findViewById(R.id.tv_minfrm_tuiian);
        layoutConpon.setOnClickListener(this);
        tvOpen.setOnClickListener(this);
        tvTuijian.setOnClickListener(this);
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
        if (v == layoutConpon){
            Intent intent = new Intent(context, ActiveActivity.class);
            context.startActivity(intent);
        } else if (v == tvOpen){
            Intent intent = new Intent(context, ActiveActivity.class);
            context.startActivity(intent);
        } else if (v == tvTuijian){
            Intent intent = new Intent(context, ActiveActivity.class);
            context.startActivity(intent);
        }
    }
}
