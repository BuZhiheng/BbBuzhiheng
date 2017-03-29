package cn.lankao.com.lovelankao.viewcontroller;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.lankao.com.lovelankao.activity.MainActivity;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.AdvertDetailActivity;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.SettingActivity;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
/**
 * Created by BuZhiheng on 2016/4/6.
 */
public class MineFragmentController implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView tvNickName;
    private TextView tvPhone;
    private TextView tvJifen;
    private ImageView photo;
    public MineFragmentController(Context context, View view) {
        this.context = context;
        this.view = view;
        x.view().inject((Activity) context);
        EventBus.getDefault().register(this);
        initView();
        initUser();
    }
    private void initUser() {
        String userid = PrefUtil.getString(CommonCode.SP_USER_USERID,"");
        if (!TextUtil.isNull(userid)){
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.getObject(userid, new QueryListener<MyUser>() {
                @Override
                public void done(MyUser user, BmobException e) {
                    PrefUtil.putString(CommonCode.SP_USER_USERID, user.getObjectId());
                    PrefUtil.putString(CommonCode.SP_USER_USERMOBILE, user.getMobile());
                    PrefUtil.putString(CommonCode.SP_USER_NICKNAME, user.getNickName());
                    PrefUtil.putString(CommonCode.SP_USER_USERTYPE, user.getUserType());
                    if (user.getPhoto() != null){
                        PrefUtil.putString(CommonCode.SP_USER_PHOTO, user.getPhoto().getFileUrl());
                    }
                    Integer point = user.getCoupon();
                    if (point == null){
                        PrefUtil.putInt(CommonCode.SP_USER_POINT, 0);
                    }else{
                        int p = point;
                        PrefUtil.putInt(CommonCode.SP_USER_POINT, p);
                    }
                    onEventMainThread(user);
                }
            });
        }
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
        photo = (ImageView) view.findViewById(R.id.iv_minefrm_photo);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_minefrm_user_msg:
                if (PrefUtil.getString(CommonCode.SP_USER_USERMOBILE, null) == null) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, SettingActivity.class);
                    context.startActivity(intent);
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
                if (PrefUtil.getString(CommonCode.SP_USER_USERID,null) == null){
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
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
            //nickname等于app包名,退出程序
            ((MainActivity)context).finish();
        } else if(CommonCode.SP_USER_PHOTO.equals(user.getNickName())){
            //nickname等于CommonCode.SP_USER_PHOTO,更新头像
            if (user.getPhoto() != null){
                x.image().bind(photo,user.getPhoto().getFileUrl(), BitmapUtil.getOptionCommonRadius());
            } else {
                x.image().bind(photo,CommonCode.APP_ICON,BitmapUtil.getOptionCommonRadius());
            }
        } else {
            //登陆成功,更新user frm 界面
            tvNickName.setText(user.getNickName());
            tvPhone.setText(user.getMobile());
            if (user.getCoupon() != null){
                tvJifen.setText(user.getCoupon()+"");
            } else {
                tvJifen.setText("0");
            }
            if (user.getPhoto() != null){
                x.image().bind(photo, user.getPhoto().getFileUrl(), BitmapUtil.getOptionCommonRadius());
            } else {
                x.image().bind(photo,CommonCode.APP_ICON,BitmapUtil.getOptionCommonRadius());
            }
        }
    }
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}