package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.RegisterActivity;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class LoginController implements View.OnClickListener {
    private LoginActivity context;
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private TextView tvRegister;
    public LoginController(LoginActivity context){
        this.context = context;
        initView();
    }

    private void initView() {
        username = (EditText) context.findViewById(R.id.et_login_username);
        password = (EditText) context.findViewById(R.id.et_login_password);
        btnLogin = (Button) context.findViewById(R.id.btn_login_login);
        tvRegister = (TextView) context.findViewById(R.id.tv_login_register);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            String un = username.getText().toString();
            String pwd = password.getText().toString();
            if (checkPhone(un) == false){
                ToastUtil.show("请正确输入手机号");
                return;
            }else if("".equals(pwd)){
                ToastUtil.show("请输入密码");
                return;
            }
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.addWhereEqualTo("mobile",un);
            query.addWhereEqualTo("passWord",pwd);
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if (e == null){
                        if (list != null && list.size() > 0){
                            MyUser user = list.get(0);
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
                                PrefUtil.putInt(CommonCode.SP_USER_POINT, point);
                            }
                            EventBus.getDefault().post(user);
                            context.finish();
                            ToastUtil.show("登陆成功");
                        } else {
                            ToastUtil.show("用户名或密码错误");
                        }
                    } else {
                        ToastUtil.show("登陆失败");
                    }
                }
            });
        } else if (v == tvRegister) {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
    }
    private boolean checkPhone(String phone){
        if (TextUtil.isNull(phone)){
            return false;
        }
        if (!TextUtil.strEX(phone, TextUtil.EX_PHONE)){
            return false;
        }
        return true;
    }
}
