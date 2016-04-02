package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.listener.SaveListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.RegisterActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
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
            if ("".equals(un)){
                ToastUtil.show("请输入账号");
                return;
            }else if("".equals(pwd)){
                ToastUtil.show("请输入密码");
                return;
            }
            MyUser user = new MyUser();
            user.setUsername(un);
            user.setPassword(pwd);
            user.login(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtil.show("登陆成功");
                    context.finish();
                }
                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.show("用户名或密码错误");
                }
            });
        } else if(v == tvRegister){
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
    }
}
