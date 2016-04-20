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
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.adapter.MyAdapter;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;

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
        view.findViewById(R.id.ll_mainfrm_header_chihewanle).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_women).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_offer).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_zulin).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_friend).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_hunqing).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_fangchan).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_service).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_jingcailankao).setOnClickListener(this);
        view.findViewById(R.id.ll_mainfrm_header_other).setOnClickListener(this);

        view.findViewById(R.id.ll_mainfrm_header_mingqi).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_tehui).setOnClickListener(this);
        view.findViewById(R.id.tv_mainfrm_tuiian).setOnClickListener(this);
    }

    private void initData() {
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.addWhereEqualTo("advIndex",CommonCode.ADVERT_INDEX);
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
        switch (id) {
            case R.id.ll_mainfrm_header_mingqi:
                toAdvert(CommonCode.ADVERT_MINGQI, "名企名商");
                break;
            case R.id.tv_mainfrm_tehui:
                toAdvert(CommonCode.ADVERT_TEHUI, "特惠不打烊");
                break;
            case R.id.tv_mainfrm_tuiian:
                toAdvert(CommonCode.ADVERT_TUIJIAN, "精品推荐");
                break;
            case R.id.ll_mainfrm_header_chihewanle:
                toAdvert(CommonCode.ADVERT_CHIHEWANLE,context.getString(R.string.text_mainfrm_chihewanle));
                break;
            case R.id.ll_mainfrm_header_women:
                toAdvert(CommonCode.ADVERT_WOMEN,context.getString(R.string.text_mainfrm_women));
                break;
            case R.id.ll_mainfrm_header_offer:
                toAdvert(CommonCode.ADVERT_OFFER,context.getString(R.string.text_mainfrm_offer));
                break;
            case R.id.ll_mainfrm_header_zulin:
                toAdvert(CommonCode.ADVERT_ZULIN,context.getString(R.string.text_mainfrm_zulin));
                break;
            case R.id.ll_mainfrm_header_friend:
                toAdvert(CommonCode.ADVERT_FRIEND,context.getString(R.string.text_mainfrm_friend));
                break;
            case R.id.ll_mainfrm_header_hunqing:
                toAdvert(CommonCode.ADVERT_HUNQING,context.getString(R.string.text_mainfrm_hunqing));
                break;
            case R.id.ll_mainfrm_header_fangchan:
                toAdvert(CommonCode.ADVERT_FANGCHAN,context.getString(R.string.text_mainfrm_fangchan));
                break;
            case R.id.ll_mainfrm_header_service:
                toAdvert(CommonCode.ADVERT_SERVICE,context.getString(R.string.text_mainfrm_service));
                break;
            case R.id.ll_mainfrm_header_jingcailankao:
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, "精彩兰考");
                intent.putExtra(CommonCode.INTENT_SETTING_URL, PrefUtil.getString(CommonCode.SP_SET_JCLKURL,""));
                context.startActivity(intent);
                break;
            case R.id.ll_mainfrm_header_other:
                toAdvert(CommonCode.ADVERT_OTHER,context.getString(R.string.text_mainfrm_other));
                break;
            default:
                break;
        }
    }
    private void toAdvert(int code,String title){
        Intent intent = new Intent(context, AdvertDetailActivity.class);
        intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, title);
        intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, code);
        context.startActivity(intent);
    }
}
