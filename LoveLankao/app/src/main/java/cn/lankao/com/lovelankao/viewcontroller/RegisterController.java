package cn.lankao.com.lovelankao.viewcontroller;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.listener.SaveListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.RegisterActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class RegisterController implements View.OnClickListener {
    private RegisterActivity context;
    private EditText nickname;
    private EditText username;
    private EditText password;
    private EditText passwordSure;
    private Button btnRegister;
    private TextView tvCancle;
    public RegisterController(RegisterActivity context){
        this.context = context;
        initView();
    }

    private void initView() {
        nickname = (EditText) context.findViewById(R.id.et_register_nickname);
        username = (EditText) context.findViewById(R.id.et_register_username);
        password = (EditText) context.findViewById(R.id.et_register_password);
        passwordSure = (EditText) context.findViewById(R.id.et_register_password_sure);
        btnRegister = (Button) context.findViewById(R.id.btn_register_register);
        tvCancle = (TextView) context.findViewById(R.id.tv_register_cancle);
        btnRegister.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister){
            String nn = nickname.getText().toString();
            String un = username.getText().toString();
            String pwd = password.getText().toString();
            String pwds = passwordSure.getText().toString();
            if (checkPhone(un) == false){
                ToastUtil.show("请正确输入手机号");
                return;
            }else if("".equals(pwd)){
                ToastUtil.show("请输入密码");
                return;
            }else if("".equals(pwds)){
                ToastUtil.show("请确认密码");
                return;
            }else if(!pwd.equals(pwds)){
                ToastUtil.show("两次输入密码不一致");
                return;
            }else if("".equals(nn)) {
                ToastUtil.show("请输入昵称");
                return;
            }
            MyUser user = new MyUser();
            user.setNickName(nn);
            user.setUsername(un);
            user.setPassword(pwd);
            user.signUp(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtil.show("注册成功");
                    context.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.show("用户名已经存在");
                }
            });
        } else if (v == tvCancle){
            context.finish();
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
