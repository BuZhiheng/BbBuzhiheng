package cn.lankao.com.lovelankao.viewcontroller;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.HorizontalScrollView;
import org.xutils.x;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.activity.CookActivity;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.activity.LKNewsActivity;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.adapter.IndexAdapter;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.Setting;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
import cn.lankao.com.lovelankao.widget.MyDialog;
/**
 * Created by BuZhiheng on 2016/5/11.
 */
public class IndexFragmentController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private Context context;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private IndexAdapter adapter;
    public IndexFragmentController(Context context, View view){
        this.context = context;
        this.view = view;
        initView();
        initData();
        initSetting();
    }
    private void initView() {
        x.view().inject((Activity) context);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_index_frm);
        refreshLayout.setOnRefreshListener(this);
        view.findViewById(R.id.fl_indexfrm_more_chat).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_news).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_menu).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_eat).setOnClickListener(this);
        view.findViewById(R.id.fl_indexfrm_more_jock).setOnClickListener(this);
    }
    private void initData() {
        adapter = new IndexAdapter(context, view, new IndexAdapter.OnReloadListener() {
            @Override
            public void success() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void initSetting() {
        BmobQuery<Setting> query = new BmobQuery<>();
        query.addWhereEqualTo("setType", 1);
        query.findObjects(new FindListener<Setting>() {
            @Override
            public void done(List<Setting> list, BmobException e) {
                if (list != null && list.size() > 0) {
                    Setting setting = list.get(0);
                    PrefUtil.putString(CommonCode.SP_SET_PARTNERURL, setting.getSetPartnerUrl());
                    PrefUtil.putString(CommonCode.SP_SET_ABOUTUSURL, setting.getSetAboutusUrl());
                    PrefUtil.putString(CommonCode.SP_SET_JCLKURL, setting.getSetJCLKUrl());
                    if (WindowUtils.getAppVersionCode()<setting.getAndroidVersionCode()){
                        MyDialog.getAlertDialog(context, "发现新版本", setting.getAndroidUpdateLog(), false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(CommonCode.APP_URL);
                                intent.setData(content_url);
                                context.startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    public void onRefresh() {
        adapter.reload();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_indexfrm_more_news:
                Intent intentNews = new Intent(context, LKNewsActivity.class);
                context.startActivity(intentNews);
                break;
            case R.id.fl_indexfrm_more_chat:
                Intent intentChat = new Intent(context, ChatRoomActivity.class);
                context.startActivity(intentChat);
                break;
            case R.id.fl_indexfrm_more_menu:
                Intent intentMenu = new Intent(context, CookActivity.class);
                intentMenu.putExtra(CommonCode.INTENT_COOK_OR_FOOD,CommonCode.INTENT_COOK);
                context.startActivity(intentMenu);
                break;
            case R.id.fl_indexfrm_more_eat:
                Intent intentEat = new Intent(context, CookActivity.class);
                intentEat.putExtra(CommonCode.INTENT_COOK_OR_FOOD,CommonCode.INTENT_FOOD);
                context.startActivity(intentEat);
                break;
            case R.id.fl_indexfrm_more_jock:
                Intent intentJock = new Intent(context, JockActivity.class);
                context.startActivity(intentJock);
                break;
            default:
                break;
        }
    }
}