package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.RegisterActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
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
            MyUser user = new MyUser();
            user.loginByAccount(context, un, pwd, new LogInListener<MyUser>() {
                @Override
                public void done(MyUser user, BmobException e) {
                    if (user != null) {
                        ToastUtil.show("登陆成功");
                        PrefUtil.putString(CommonCode.SP_USER_USERID, user.getObjectId());
                        PrefUtil.putString(CommonCode.SP_USER_USERNAME, user.getUsername());
                        PrefUtil.putString(CommonCode.SP_USER_NICKNAME, user.getNickName());
                        PrefUtil.putString(CommonCode.SP_USER_USERTYPE, user.getUserType());
                        Integer point = user.getPoint();
                        if (point == null){
                            PrefUtil.putInt(CommonCode.SP_USER_POINT, 0);
                        }else{
                            PrefUtil.putInt(CommonCode.SP_USER_POINT, point);
                        }
                        EventBus.getDefault().post(user);
                        context.finish();
                    } else{
                        ToastUtil.show(e.getMessage());
                    }
                }
            });
        } else if (v == tvRegister) {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
    }
    private boolean checkPhone(String phone){
        if (phone == null || "".equals(phone)){
            return false;
        }else if (phone.length() != 11 || phone.contains(" ")){
            return false;
        }
        return true;
    }
}
