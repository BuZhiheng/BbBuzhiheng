package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.lankao.com.lovelankao.activity.CommentActivity;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
/**
 * Created by buzhiheng on 2016/12/15.
 */
public class CommentActivityController {
    private CommentActivity view;
    private Intent intent;
    private String last;
    public CommentActivityController(CommentActivity view){
        this.view = view;
        intent = view.getIntent();
        last = intent.getStringExtra(CommonCode.INTENT_COMMENT_LASTCONTENT);
        if (!TextUtil.isNull(last)){
            view.setTvLast(last);
        }
    }
    public void comment(EditText etContent){
        String content = etContent.getText().toString();
        if (TextUtil.isNull(content)){
            view.showToast("请输入内容");
            return;
        }
        String postId = intent.getStringExtra(CommonCode.INTENT_COMMENT_POSTID);
        if (TextUtil.isNull(postId)){
            view.showToast("数据有误");
            return;
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        if (!TextUtil.isNull(last)){
            comment.setLastUserContent(last);
        }
        String photo = PrefUtil.getString(CommonCode.SP_USER_PHOTO,"");
        if (!TextUtil.isNull(photo)){
            comment.setUserPhotoUrl(photo);
        }
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME,"");
        comment.setUsername(nickname);
        view.showDialog();
        comment.save(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null){
                    view.commentSuccess();
                } else {
                    view.showToast(e.getMessage());
                }
            }
        });
    }
}