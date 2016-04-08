package cn.lankao.com.lovelankao.viewcontroller;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SettingActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.PrefUtil;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class SettingActivityController implements View.OnClickListener {
    private SettingActivity context;
    public SettingActivityController(SettingActivity context){
        this.context = context;
        initView();
    }

    private void initView() {
        context.findViewById(R.id.btn_setting_zhuxiao).setOnClickListener(this);
        context.findViewById(R.id.btn_setting_exit).setOnClickListener(this);
        context.findViewById(R.id.iv_setting_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_setting_zhuxiao:
                MyUser user = new MyUser();
                user.setNickName("未登录");
                PrefUtil.clear();
                context.finish();
                EventBus.getDefault().post(user);
                break;
            case R.id.btn_setting_exit:
                MyUser userExit = new MyUser();
                userExit.setNickName(context.getPackageName());
                context.finish();
                EventBus.getDefault().post(userExit);
                break;
            case R.id.iv_setting_back:
                context.finish();
                break;
        }
    }
}
