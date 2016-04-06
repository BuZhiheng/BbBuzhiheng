package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.entity.MyUser;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by dell on 2016/4/6.
 */
public class MineFragmentController implements View.OnClickListener {
    private MyUser myUser;
    private Context context;
    private View view;
    private TextView tvNickName;
    private TextView tvPhone;
    public MineFragmentController(Context context,View view){
        this.context = context;
        this.view = view;
        myUser = (MyUser) BmobUser.getCurrentUser(context);
        initView();
    }

    private void initView() {
        view.findViewById(R.id.ll_minefrm_user_msg).setOnClickListener(this);
        tvNickName = (TextView) view.findViewById(R.id.tv_minefrm_nickname);
        tvPhone = (TextView) view.findViewById(R.id.tv_minefrm_phone);
        if (myUser != null){
            tvNickName.setText(myUser.getNickName());
            tvPhone.setText(myUser.getUsername());
            ToastUtil.show(myUser.getPoint()+"");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ll_minefrm_user_msg:
                if (myUser == null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    myUser.setPoint(myUser.getPoint()+1);
                    myUser.update(context);
                }
                break;
        }
    }
}
