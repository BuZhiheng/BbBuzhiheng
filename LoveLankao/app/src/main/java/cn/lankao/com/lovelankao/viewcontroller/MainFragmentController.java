package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ActiveActivity;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MainFragmentController implements View.OnClickListener {
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewHeader header;
    private MyAdapter adapter;

    public MainFragmentController(Context context, View view) {
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
        view.findViewById(R.id.ll_mainfrm_header_conpon).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_meishi).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_gouwu).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_jiudian).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_xiuxian).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_liren).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_hunqing).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_xiyu).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_meijia).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_ktv).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_other).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_open).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_tuiian).setOnClickListener(this);
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
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.tv_mainfrm_open:
                intent = new Intent(context, ActiveActivity.class);
                context.startActivity(intent);
                break;
            case R.id.tv_mainfrm_tuiian:
                intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
            case R.id.ll_mainfrm_header_conpon:
                intent = new Intent(context, ChatRoomActivity.class);
                context.startActivity(intent);
                break;
            case R.id.ll_mainfrm_header_meishi:
                toAdvert(CommonCode.ADVERT_MEISHI,"美食");
                break;
            case R.id.ll_mainfrm_header_gouwu:
                toAdvert(CommonCode.ADVERT_GOUWU,"购物");
                break;
            case R.id.ll_mainfrm_header_jiudian:
                toAdvert(CommonCode.ADVERT_JIUDIAN,"酒店");
                break;
            case R.id.ll_mainfrm_header_xiuxian:
                toAdvert(CommonCode.ADVERT_XIUXIAN,"休闲娱乐");
                break;
            case R.id.ll_mainfrm_header_liren:
                toAdvert(CommonCode.ADVERT_LIREN,"丽人");
                break;
            case R.id.ll_mainfrm_header_hunqing:
                toAdvert(CommonCode.ADVERT_HUNQING,"婚庆");
                break;
            case R.id.ll_mainfrm_header_xiyu:
                toAdvert(CommonCode.ADVERT_XIYU,"洗浴");
                break;
            case R.id.ll_mainfrm_header_meijia:
                toAdvert(CommonCode.ADVERT_MEIJIA,"美甲");
                break;
            case R.id.ll_mainfrm_header_ktv:
                toAdvert(CommonCode.ADVERT_KTV,"KTV");
                break;
            case R.id.ll_mainfrm_header_other:
                break;
            default:
                break;
        }
    }
    private void toAdvert(int code,String title){
        Intent intent = new Intent(context, AdvertDetailActivity.class);
        intent.putExtra("type", code);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
