package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.PhotoViewPagerActivity;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivityController{
    private SquareActivity context;
    private Square square;
    private List<String> list;
    private List<Comment> listComment;
    public SquareActivityController(SquareActivity context){
        this.context = context;
        list = new ArrayList<>();
        listComment = new ArrayList<>();
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
        if (!TextUtil.isNull(square.getUserPhoto())){
            userImg = square.getUserPhoto();
        } else {
            userImg = CommonCode.APP_ICON;
        }
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        Drawable drawable;
        if (!TextUtil.isNull(nickname) && !TextUtil.isNull(square.getLikeUsers()) && square.getLikeUsers().contains(nickname)){
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc);
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_square_liketimes);
        }
//        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME,"");
//        holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimes));
//        if (!TextUtil.isNull(nickname) && !TextUtil.isNull(square.getLikeUsers()) && square.getLikeUsers().contains(nickname)){
//            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimesc));
//        } else {
//            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimes));
//        }
        list.clear();
        setPhoto(square.getSquarePhoto1(), context.ivPhoto1);
        setPhoto(square.getSquarePhoto2(), context.ivPhoto2);
        setPhoto(square.getSquarePhoto3(), context.ivPhoto3);
        setPhoto(square.getSquarePhoto4(), context.ivPhoto4);
        setPhoto(square.getSquarePhoto5(), context.ivPhoto5);
        setPhoto(square.getSquarePhoto6(), context.ivPhoto6);
        setPhotoPager(square.getSquarePhoto1(), context.ivPhoto1);
        setPhotoPager(square.getSquarePhoto2(), context.ivPhoto2);
        setPhotoPager(square.getSquarePhoto3(), context.ivPhoto3);
        setPhotoPager(square.getSquarePhoto4(), context.ivPhoto4);
        setPhotoPager(square.getSquarePhoto5(), context.ivPhoto5);
        setPhotoPager(square.getSquarePhoto6(), context.ivPhoto6);
        if (square.getSquarePhoto1() != null){
            context.setVisibility(context.llPhoto1);
        }
        if (square.getSquarePhoto4() != null){
            context.setVisibility(context.llPhoto2);
        }
        context.setData(square, userImg, drawable);
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("postId",square.getObjectId());
        query.order("-createdAt");
        query.setLimit(100);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    listComment = list;
                    setComment();
                    square.setCommentTimes(list.size());
                    square.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                        }
                    });
                }
            }
        });
    }
    private void setComment(){
        context.clearCommentLL();
        context.setCommentTimes(listComment.size()+"");
        for (int i=0;i<listComment.size();i++){
            Comment comment = listComment.get(i);
            if (comment != null){
                context.setComment(comment);
            }
        }
    }
    private void setPhoto(BmobFile file,ImageView iv) {
        if (file != null){
            final String imgUrl = file.getFileUrl();
            context.setIvPhoto(iv, imgUrl);
            list.add(imgUrl);
        }
    }
    private void setPhotoPager(BmobFile file,ImageView iv){
        if (file != null){
            final String imgUrl = file.getFileUrl();
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                    intent.putStringArrayListExtra(CommonCode.INTENT_COMMON_OBJ, (ArrayList<String>) list);
                    intent.putExtra(CommonCode.INTENT_COMMON_STRING, imgUrl);
                    context.startActivity(intent);
                }
            });
        }
    }
    public void checkComment(String last){
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (TextUtil.isNull(nickname)){
            context.showToast("请先登录");
            context.toLogin();
        } else {
            if (square != null){
                context.toComment(square.getObjectId(),last);
            }
        }
    }
    public void onLikeClick(){
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (TextUtil.isNull(nickname)){
            context.showToast("请先登录");
            context.toLogin();
            return;
        }
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
    public void onResult(int resultCode, Intent data) {
        if (resultCode == CommonCode.INTENT_COMMON_ACTIVITY_CODE && data != null){
            Comment comment = (Comment) data.getSerializableExtra((CommonCode.INTENT_COMMON_OBJ));
            if (comment != null){
                listComment.add(0,comment);
                setComment();
            }
        }
    }
}