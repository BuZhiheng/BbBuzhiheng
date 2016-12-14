package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
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
            context.setIvPhoto(context.ivPhoto1,square.getSquarePhoto1().getFileUrl(context));
        } if (square.getSquarePhoto2() != null){
            context.setIvPhoto(context.ivPhoto2,square.getSquarePhoto2().getFileUrl(context));
        } if (square.getSquarePhoto3() != null){
            context.setIvPhoto(context.ivPhoto3, square.getSquarePhoto3().getFileUrl(context));
        } if (square.getSquarePhoto4() != null){
            context.setIvPhoto(context.ivPhoto4, square.getSquarePhoto4().getFileUrl(context));
        } if (square.getSquarePhoto5() != null){
            context.setIvPhoto(context.ivPhoto5,square.getSquarePhoto5().getFileUrl(context));
        }
        context.setData(square,userImg,drawable);
    }
    public void onLikeClick(){
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
            final int like = square.getLikeTimes()==null?1:square.getLikeTimes()+1;
            String likeUsers = square.getLikeUsers()==null?nickname:nickname+","+square.getLikeUsers();
            square.setLikeTimes(like);
            square.setLikeUsers(likeUsers);
            context.setLickIvTimes(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc),like + "");
            square.update(context);
        }
    }
    private void initComment() {
        BmobQuery<Square> query = new BmobQuery<>();
        query.setLimit(CommonCode.RV_ITEMS_COUT);
        query.setSkip(0);
        query.order("-createdAt");
        query.findObjects(context, new FindListener<Square>() {
            @Override
            public void onSuccess(List<Square> list) {
            }
            @Override
            public void onError(int i, String s) {
                ToastUtil.show(s);
            }
        });
    }
}
