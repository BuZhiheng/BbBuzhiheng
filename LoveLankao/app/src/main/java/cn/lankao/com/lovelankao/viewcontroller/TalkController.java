package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ChatRoomActivity;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.activity.ReadWeixinActivity;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.activity.TopActivity;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class TalkController implements View.OnClickListener {
    private Context context;
    private View view;
    public TalkController(Context context,View view){
        this.context = context;
        this.view = view;
        initView();
    }

    private void initView() {
        view.findViewById(R.id.fl_talkfrm_chatroom).setOnClickListener(this);
        view.findViewById(R.id.fl_talkfrm_square).setOnClickListener(this);
        view.findViewById(R.id.fl_talkfrm_read).setOnClickListener(this);
        view.findViewById(R.id.fl_talkfrm_jock).setOnClickListener(this);
        view.findViewById(R.id.fl_talkfrm_top).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fl_talkfrm_chatroom:
                Intent intentTalk = new Intent(context, ChatRoomActivity.class);
                context.startActivity(intentTalk);
                break;
            case R.id.fl_talkfrm_square:
                Intent intentSquare = new Intent(context, SquareActivity.class);
                context.startActivity(intentSquare);
                break;
            case R.id.fl_talkfrm_read:
                Intent intentRead = new Intent(context, ReadWeixinActivity.class);
                context.startActivity(intentRead);
                break;
            case R.id.fl_talkfrm_jock:
                Intent intentJock = new Intent(context, JockActivity.class);
                context.startActivity(intentJock);
                break;
            case R.id.fl_talkfrm_top:
                Intent intentTop = new Intent(context, TopActivity.class);
                context.startActivity(intentTop);
                break;
        }
    }
}
