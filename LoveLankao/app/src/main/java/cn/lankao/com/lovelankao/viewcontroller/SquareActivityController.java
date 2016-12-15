package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivityController{
    private SquareActivity context;
    private Square square;
    public SquareActivityController(SquareActivity context){
        this.context = context;
        initComment();
    }
    public void initData(Intent intent){
        if (intent == null || intent.getSerializableExtra(CommonCode.INTENT_COMMON_OBJ) == null){
            return;
        }
        square = (Square) intent.getSerializableExtra(CommonCode.INTENT_COMMON_OBJ);
        if (square == null){
            return;
        }
        String userImg;
        if (square.getUserPhoto() != null){
            userImg = square.getUserPhoto();
        } else {
            userImg = CommonCode.APP_ICON;
        }
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        Drawable drawable;
        if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_square_liketimes);
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc);
        }
        if (square.getSquarePhoto1() != null){
            context.setIvPhoto(context.ivPhoto1,square.getSquarePhoto1().getFileUrl());
        } if (square.getSquarePhoto2() != null){
            context.setIvPhoto(context.ivPhoto2,square.getSquarePhoto2().getFileUrl());
        } if (square.getSquarePhoto3() != null){
            context.setIvPhoto(context.ivPhoto3, square.getSquarePhoto3().getFileUrl());
        } if (square.getSquarePhoto4() != null){
            context.setIvPhoto(context.ivPhoto4, square.getSquarePhoto4().getFileUrl());
        } if (square.getSquarePhoto5() != null){
            context.setIvPhoto(context.ivPhoto5,square.getSquarePhoto5().getFileUrl());
        }
        context.setData(square, userImg, drawable);
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("postId",square.getObjectId());
        query.order("-createdAt");
        query.setLimit(50);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    context.clearCommentLL();
                    for (int i=0;i<list.size();i++){
                        Comment comment = list.get(i);
                        if (comment != null){
                            context.setComment(comment);
                        }
                    }
                }
            }
        });
    }
    public void checkComment(String last){
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (TextUtil.isNull(nickname)){
            context.showToast("请去登录");
            context.toLogin();
        } else {
            if (square != null){
                context.toComment(square.getObjectId(),last);
            }
        }
    }
    public void onLikeClick(){
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
            final int like = square.getLikeTimes()==null?1:square.getLikeTimes()+1;
            String likeUsers = square.getLikeUsers()==null?nickname:nickname+","+square.getLikeUsers();
            square.setLikeTimes(like);
            square.setLikeUsers(likeUsers);
            context.setLickIvTimes(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc),like + "");
            square.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {

                }
            });
        }
    }
    private void initComment() {
        BmobQuery<Square> query = new BmobQuery<>();
        query.setLimit(CommonCode.RV_ITEMS_COUT);
        query.setSkip(0);
        query.order("-createdAt");
        query.findObjects(new FindListener<Square>() {
            @Override
            public void done(List<Square> list, BmobException e) {

            }
        });
    }
    public void onResult(int resultCode, Intent data) {
        if (resultCode == CommonCode.INTENT_COMMON_ACTIVITY_CODE && data != null){
            if (!TextUtil.isNull(data.getStringExtra(CommonCode.INTENT_COMMENT_POSTID))){
                Integer time = square.getCommentTimes();
                if (time != null){
                    time = time + 1;
                    square.setCommentTimes(time);
                } else {
                    time = 1;
                    square.setCommentTimes(time);
                }
                final Integer finalTime = time;
                square.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            context.setCommentTimes(finalTime+"");
                        }
                    }
                });
            }
        }
    }
}