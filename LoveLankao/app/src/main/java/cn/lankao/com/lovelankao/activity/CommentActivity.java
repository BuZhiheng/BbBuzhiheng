package cn.lankao.com.lovelankao.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.viewcontroller.CommentActivityController;
import cn.lankao.com.lovelankao.widget.ProDialog;

/**
 * Created by buzhiheng on 2016/12/15.
 */
public class CommentActivity extends AppCompatActivity {
    private CommentActivityController controller;
    @Bind(R.id.et_comment_content)
    EditText etContent;
    @Bind(R.id.tv_comment_last)
    TextView tvLast;
    private Dialog commentDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        controller = new CommentActivityController(this);
        commentDialog = ProDialog.getProDialog(this);
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("评论");
    }
    public void showToast(String toast){
        ToastUtil.show(toast);
    }
    public void commentSuccess(){
        Intent intent = new Intent();
        intent.putExtra(CommonCode.INTENT_COMMENT_POSTID,CommonCode.INTENT_COMMENT_POSTID);
        setResult(CommonCode.INTENT_COMMON_ACTIVITY_CODE, intent);
        showToast("评论成功");
        finish();
    }
    public void showDialog(){
        commentDialog.show();
    }
    public void setTvLast(String last){
        tvLast.setText(last);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_titlebar_back:
                finish();
                break;
            case R.id.btn_comment_send:
                controller.comment(etContent);
                break;
        }
    }
}