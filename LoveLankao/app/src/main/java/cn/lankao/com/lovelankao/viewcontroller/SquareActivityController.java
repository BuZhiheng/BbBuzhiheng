package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivityController implements View.OnClickListener{
    private SquareActivity context;
    private ImageView ivPhoto;
    private TextView tvNickname;
    private TextView tvTime;
    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivPhoto1,ivPhoto2,ivPhoto3,ivPhoto4,ivPhoto5;
    private ImageView ivLikeTimes;
    private TextView tvCommentTimes;
    private TextView tvLikeTimes;
    private TextView tvClickTimes;
    private LinearLayout llLikes;
    private int cout = CommonCode.RV_ITEMS_COUT;
    private boolean isRefresh = true;
    public SquareActivityController(SquareActivity context){
        this.context = context;
        initView();
        initData();
        initComment();
    }
    private void initView() {
        ivPhoto = (ImageView) context.findViewById(R.id.iv_square_item_photo);
        ivPhoto1 = (ImageView) context.findViewById(R.id.iv_square_item_photo1);
        ivPhoto2 = (ImageView) context.findViewById(R.id.iv_square_item_photo2);
        ivPhoto3 = (ImageView) context.findViewById(R.id.iv_square_item_photo3);
        ivPhoto4 = (ImageView) context.findViewById(R.id.iv_square_item_photo4);
        ivPhoto5 = (ImageView) context.findViewById(R.id.iv_square_item_photo5);
        ivLikeTimes = (ImageView) context.findViewById(R.id.iv_square_item_liketimes);
        tvNickname = (TextView) context.findViewById(R.id.tv_square_item_nickname);
        tvTime = (TextView) context.findViewById(R.id.tv_square_item_time);
        tvTitle = (TextView) context.findViewById(R.id.tv_square_item_title);
        tvContent = (TextView) context.findViewById(R.id.tv_square_item_content);
        tvCommentTimes = (TextView) context.findViewById(R.id.tv_square_item_commenttimes);
        tvLikeTimes = (TextView) context.findViewById(R.id.tv_square_item_liketimes);
        tvClickTimes = (TextView) context.findViewById(R.id.tv_square_item_clicktimes);
        llLikes = (LinearLayout) context.findViewById(R.id.ll_square_item_liketimes);
    }
    private void initData(){
        Intent intent = context.getIntent();
        if (intent == null || intent.getSerializableExtra(CommonCode.INTENT_COMMON_OBJ) == null){
            return;
        }
        final Square square = (Square) intent.getSerializableExtra(CommonCode.INTENT_COMMON_OBJ);
        tvNickname.setText(square.getNickName());
        tvTime.setText(square.getCreatedAt());
        tvContent.setText(square.getSquareContent());
        tvCommentTimes.setText(square.getCommentTimes() == null ? "0" : square.getCommentTimes() + "");
        tvLikeTimes.setText(square.getLikeTimes() == null ? "0" : square.getLikeTimes() + "");
        tvClickTimes.setText(square.getClickTimes()==null ? "0" : square.getClickTimes()+"");
        if (square.getSquareTitle() == null){
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(square.getSquareTitle());
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (square.getUserPhoto() != null){
            x.image().bind(ivPhoto, square.getUserPhoto(), BitmapUtil.getOptionRadius());
        } else {
            x.image().bind(ivPhoto, CommonCode.APP_ICON, BitmapUtil.getOptionRadius());
        }
        if (square.getSquarePhoto1() != null){
            x.image().bind(ivPhoto1, square.getSquarePhoto1().getFileUrl(context), BitmapUtil.getOptionCommon());
            ivPhoto1.setVisibility(View.VISIBLE);
        } if (square.getSquarePhoto2() != null){
            x.image().bind(ivPhoto2, square.getSquarePhoto2().getFileUrl(context), BitmapUtil.getOptionCommon());
            ivPhoto2.setVisibility(View.VISIBLE);
        } if (square.getSquarePhoto3() != null){
            x.image().bind(ivPhoto3, square.getSquarePhoto3().getFileUrl(context), BitmapUtil.getOptionCommon());
            ivPhoto3.setVisibility(View.VISIBLE);
        } if (square.getSquarePhoto4() != null){
            x.image().bind(ivPhoto4, square.getSquarePhoto4().getFileUrl(context), BitmapUtil.getOptionCommon());
            ivPhoto4.setVisibility(View.VISIBLE);
        } if (square.getSquarePhoto5() != null){
            x.image().bind(ivPhoto5, square.getSquarePhoto5().getFileUrl(context), BitmapUtil.getOptionCommon());
            ivPhoto5.setVisibility(View.VISIBLE);
        }
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
            ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimes));
        } else {
            ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimesc));
        }
        llLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
                    final int like = square.getLikeTimes()==null?1:square.getLikeTimes()+1;
                    String likeUsers = square.getLikeUsers()==null?nickname:nickname+","+square.getLikeUsers();
                    square.setLikeTimes(like);
                    square.setLikeUsers(likeUsers);
                    ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc));
                    tvLikeTimes.setText(like + "");
                    square.update(context);
                }
            }
        });
    }
    private void initComment() {
        BmobQuery<Square> query = new BmobQuery<>();
        if (isRefresh){
            query.setLimit(CommonCode.RV_ITEMS_COUT);
            query.setSkip(0);
        }else{
            query.setLimit(cout);
            query.setSkip(0);
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_square_back:
                context.finish();
                break;
        }
    }
    public void onDestroy(){
    }
}
