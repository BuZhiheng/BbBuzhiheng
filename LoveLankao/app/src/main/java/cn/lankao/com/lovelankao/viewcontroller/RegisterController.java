package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.RegisterActivity;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.JuheApiResult;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.IntegerUtils;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import rx.Subscriber;
/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class RegisterController implements View.OnClickListener {
    private static int refreshCodeButton = 0x001;
    private int setCodeBtnCanClick = 0x002;
    private String SMS_URL = "http://v.juhe.cn/sms/send?key=cb56555dd8eda40257f8eda6b7d8c88c&tpl_id=25454&tpl_value=%23code%23%3d";//+code&mobile=151
    private RegisterActivity context;
    private EditText nickname;
    private EditText username;
    private EditText password;
    private EditText passwordSure;
    private EditText etCode;
    private Button btnRegister;
    private Button btnCode;
    private TextView tvCancle;
    private LinearLayout llNickname;
    private int time;
    private String strFrom = "注册";
    private boolean fromForget = false;
    private String userId = "";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:
                    btnCode.setText(time+"s");
                    btnCode.setClickable(false);
                    break;
                case 0x002:
                    btnCode.setText("重新发送");
                    btnCode.setClickable(true);
                    break;
            }
        }
    };
    public RegisterController(RegisterActivity context){
        this.context = context;
        initView();
        Intent intent = context.getIntent();
        if (intent.getIntExtra(CommonCode.INTENT_COMMON_STRING,-1) == CommonCode.INTENT_COMMON_INT0){
            llNickname.setVisibility(View.GONE);
            context.findViewById(R.id.iv_register_nickname_slid).setVisibility(View.GONE);
            fromForget = true;
            strFrom = "找回密码";
            btnRegister.setText(strFrom);
        }
    }
    private void initView() {
        nickname = (EditText) context.findViewById(R.id.et_register_nickname);
        username = (EditText) context.findViewById(R.id.et_register_username);
        password = (EditText) context.findViewById(R.id.et_register_password);
        passwordSure = (EditText) context.findViewById(R.id.et_register_password_sure);
        etCode = (EditText) context.findViewById(R.id.et_register_code);
        btnRegister = (Button) context.findViewById(R.id.btn_register_register);
        btnCode = (Button) context.findViewById(R.id.btn_register_getcode);
        tvCancle = (TextView) context.findViewById(R.id.tv_register_cancle);
        llNickname = (LinearLayout) context.findViewById(R.id.ll_register_nickname);
        btnRegister.setOnClickListener(this);
        btnCode.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register_cancle:
                context.finish();
                break;
            case R.id.btn_register_register:
                register();
                break;
            case R.id.btn_register_getcode:
                checkMobile();
                break;
        }
    }
    private void checkMobile() {
        final String phone = username.getText().toString();
        if (TextUtil.isNull(phone)){
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TextUtil.strEX(phone,TextUtil.EX_PHONE)){
            ToastUtil.show("手机号输入有误");
            return;
        }
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("mobile", phone);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (fromForget) {
                        if (list == null || list.size() == 0) {
                            ToastUtil.show("该手机号不存在,请去注册");
                        } else {
                            userId = list.get(0).getObjectId();
                            getCode(phone);
                        }
                    } else {
                        if (list != null && list.size() > 0) {
                            ToastUtil.show("该手机号已被注册,请登录或者找回密码");
                        } else {
                            getCode(phone);
                        }
                    }
                }
            }
        });
    }
    private void getCode(final String phone) {
        final int code = IntegerUtils.randomCode();
        String finalUrl = SMS_URL+code+"&mobile="+phone;
        OkHttpUtil.get(finalUrl, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onNext(String s) {
                JuheApiResult res = GsonUtil.jsonToObject(s, JuheApiResult.class);
                if (res.getError_code() == 0) {
                    //发送成功
                    ToastUtil.show("短信发送成功");
                    PrefUtil.putString(CommonCode.SP_REGISTER_CODE, code + "");
                    PrefUtil.putString(CommonCode.SP_REGISTER_CODE_PHONE, phone);
                    OkHttpUtil.executor.execute(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try {
                                                            for (int i = 60; i > 0; i--) {
                                                                time = i;
                                                                handler.sendEmptyMessage(refreshCodeButton);
                                                                Thread.sleep(1000);
                                                            }
                                                            handler.sendEmptyMessage(setCodeBtnCanClick);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                    );
                } else {
                    ToastUtil.show("短信发送失败");
                }
            }
        });
    }
    private void register(){
        if (fromForget){
            forgetPsw();
            return;
        }
        String nickName = nickname.getText().toString();
        String phone = username.getText().toString();
        String pwd = password.getText().toString();
        String pwdConfirm = passwordSure.getText().toString();
        String code = etCode.getText().toString();
        if (TextUtil.isNull(phone)){
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TextUtil.strEX(phone,TextUtil.EX_PHONE)){
            ToastUtil.show("手机号输入有误");
            return;
        }
        if(TextUtil.isNull(pwd)){
            ToastUtil.show("请输入密码");
            return;
        }
        if(TextUtil.isNull(pwdConfirm)){
            ToastUtil.show("请确认密码");
            return;
        }
        if(!pwd.equals(pwdConfirm)){
            ToastUtil.show("两次输入密码不一致");
            return;
        }
        if(TextUtil.isNull(nickName)) {
            ToastUtil.show("请输入昵称");
            return;
        }
        if (TextUtil.isNull(code)){
            ToastUtil.show("请输入验证码");
            return;
        }
        String preCode = PrefUtil.getString(CommonCode.SP_REGISTER_CODE,"");
        String prePhone = PrefUtil.getString(CommonCode.SP_REGISTER_CODE_PHONE,"");
        if (!(phone.equals(prePhone) && preCode.equals(code))){
            ToastUtil.show("验证码有误");
            return;
        }
        MyUser user = new MyUser();
        user.setNickName(nickName);
        user.setMobile(phone);
        user.setPassWord(pwd);
        user.save(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    ToastUtil.show("注册成功");
                    context.finish();
                } else {
                    ToastUtil.show(e.getMessage());
                }
            }
        });
    }
    private void forgetPsw(){
        String phone = username.getText().toString();
        String pwd = password.getText().toString();
        String pwdConfirm = passwordSure.getText().toString();
        String code = etCode.getText().toString();
        if (TextUtil.isNull(phone)){
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TextUtil.strEX(phone,TextUtil.EX_PHONE)){
            ToastUtil.show("手机号输入有误");
            return;
        }
        if(TextUtil.isNull(pwd)){
            ToastUtil.show("请输入密码");
            return;
        }
        if(TextUtil.isNull(pwdConfirm)){
            ToastUtil.show("请确认密码");
            return;
        }
        if(!pwd.equals(pwdConfirm)){
            ToastUtil.show("两次输入密码不一致");
            return;
        }
        if (TextUtil.isNull(code)){
            ToastUtil.show("请输入验证码");
            return;
        }
        String preCode = PrefUtil.getString(CommonCode.SP_REGISTER_CODE,"");
        String prePhone = PrefUtil.getString(CommonCode.SP_REGISTER_CODE_PHONE,"");
        if (!(phone.equals(prePhone) && preCode.equals(code))){
            ToastUtil.show("验证码有误");
            return;
        }
        MyUser user = new MyUser();
        user.setMobile(phone);
        user.setPassWord(pwd);
        user.update(userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtil.show("操作成功");
                    context.finish();
                } else {
                    ToastUtil.show(e.getMessage());
                }
            }
        });
    }
}