package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.lankao.com.lovelankao.MainActivity;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.SettingActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by BuZhiheng on 2016/4/6.
 */
public class MineFragmentController implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView tvNickName;
    private TextView tvPhone;
    private TextView tvJifen;

    public MineFragmentController(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        view.findViewById(R.id.ll_minefrm_user_msg).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_needpartner).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_tuijian).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_mypartner).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_aboutus).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_setting).setOnClickListener(this);

        tvNickName = (TextView) view.findViewById(R.id.tv_minefrm_nickname);
        tvPhone = (TextView) view.findViewById(R.id.tv_minefrm_phone);
        tvJifen = (TextView) view.findViewById(R.id.tv_minefrm_jifen);


        tvNickName.setText(PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "未登录"));
        tvPhone.setText(PrefUtil.getString(CommonCode.SP_USER_USERNAME, ""));
        tvJifen.setText(PrefUtil.getInt(CommonCode.SP_USER_POINT, 0)+"");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_minefrm_user_msg:
                if (PrefUtil.getString(CommonCode.SP_USER_USERNAME, null) == null) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {

                }
                break;
            case R.id.fl_minefrm_needpartner:
                toWebView(PrefUtil.getString(CommonCode.SP_SET_PARTNERURL, ""), "我要合作");
                break;
            case R.id.fl_minefrm_tuijian:
                toAdvert(CommonCode.ADVERT_TUIJIAN, "今日推荐");
                break;
            case R.id.fl_minefrm_mypartner:
                toAdvert(CommonCode.ADVERT_HEZUO, "合作商家");
                break;
            case R.id.fl_minefrm_aboutus:
                toWebView(PrefUtil.getString(CommonCode.SP_SET_ABOUTUSURL, ""), "关于我们");
                break;
            case R.id.fl_minefrm_setting:
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void toAdvert(int code, String title) {
        Intent intent = new Intent(context, AdvertDetailActivity.class);
        intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, title);
        intent.putExtra(CommonCode.INTENT_ADVERT_TYPE, code);
        context.startActivity(intent);
    }

    private void toWebView(String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, title);
        intent.putExtra(CommonCode.INTENT_SETTING_URL, url);
        context.startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(MyUser user) {
        if (user == null){
            return;
        }
        if (context.getPackageName().equals(user.getNickName())){
            ((MainActivity)context).finish();
        }else{
            tvNickName.setText(user.getNickName());
            tvPhone.setText(user.getUsername());
            if (user.getPoint() != null){
                tvJifen.setText(user.getPoint());
            }else{
                tvJifen.setText("0");
            }
        }
    }
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
