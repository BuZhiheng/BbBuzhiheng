package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
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

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivityController implements View.OnClickListener {
    private SquareActivity context;
    private RecyclerView rvSquare;
    private SquareAdapter adapter;
    public SquareActivityController(SquareActivity context){
        this.context = context;
        EventBus.getDefault().register(this);
        initView();
        initData();
    }
    private void initView() {
        adapter = new SquareAdapter(context);
        rvSquare = (RecyclerView) context.findViewById(R.id.rv_square_room);
        rvSquare.setLayoutManager(new LinearLayoutManager(context));
        rvSquare.setAdapter(adapter);
        rvSquare.setItemAnimator(new DefaultItemAnimator());
        context.findViewById(R.id.iv_square_send).setOnClickListener(this);
    }
    private void initData() {
        BmobQuery<Square> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<Square>() {
            @Override
            public void onSuccess(List<Square> list) {
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
            case R.id.iv_square_send:
                Intent intent = new Intent(context, SquareSendActivity.class);
                context.startActivity(intent);
                break;
        }
    }
    @Subscribe
    public void onEventMainThread(Square square){
        adapter.addData(square);
        adapter.notifyDataSetChanged();
        rvSquare.smoothScrollToPosition(adapter.getItemCount());
    }
    public void onDestroy(){
        EventBus.getDefault().unregister(this);
    }
}
