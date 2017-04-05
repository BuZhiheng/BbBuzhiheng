package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.activity.CommentActivity;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.MyUser;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
/**
 * Created by buzhiheng on 2016/12/15.
 */
public class CommentActivityController {
    private CommentActivity view;
    private Intent intent;
    private Comment comment;
    public CommentActivityController(CommentActivity view){
        this.view = view;
        intent = view.getIntent();
        comment = (Comment) intent.getSerializableExtra(CommonCode.INTENT_COMMON_OBJ);
        if (comment != null && !TextUtil.isNull(comment.getLastUserContent())){
            view.setTvLast(comment.getLastUserContent());
        }
    }
    public void comment(EditText etContent){
        String content = etContent.getText().toString();
        if (TextUtil.isNull(content)){
            view.showToast("请输入内容");
            return;
        }
        if (TextUtil.isNull(comment.getPostId())){
            view.showToast("数据有误");
            return;
        }
        view.showDialog();
        comment.setContent(content);
        String photo = PrefUtil.getString(CommonCode.SP_USER_PHOTO,"");
        if (!TextUtil.isNull(photo)){
            comment.setUserPhotoUrl(photo);
        }
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME,"");
        comment.setUsername(nickname);
        comment.setUserId(PrefUtil.getString(CommonCode.SP_USER_USERID,""));
        comment.setUserType(PrefUtil.getString(CommonCode.SP_USER_USERTYPE,""));
        comment.save(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null){
                    view.commentSuccess(comment);
                    setCommentTime();
                } else {
                    view.showToast(e.getMessage());
                }
            }
        });
    }
    private void setCommentTime() {
        if (CommonCode.INTENT_COMMENT_FROM_SQUARE.equals(comment.getCommentFrom())){
            Square square = new Square();
            square.increment("commentTimes");
            square.update(comment.getPostId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                }
            });
            if (!TextUtil.isNull(comment.getPostUserId())){
                MyUser user = new MyUser();
                user.setCommentMsg(CommonCode.USER_MSG_POST_COMMENT);
                user.update(comment.getPostUserId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
            }
            if (!TextUtil.isNull(comment.getLastUserId())){
                MyUser user = new MyUser();
                user.setCommentMsg(CommonCode.USER_MSG_POST_COMMENT);
                user.update(comment.getLastUserId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
            }
        }
    }
}