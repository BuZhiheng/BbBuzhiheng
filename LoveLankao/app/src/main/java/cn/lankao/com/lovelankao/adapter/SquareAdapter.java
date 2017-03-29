package cn.lankao.com.lovelankao.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.CommentActivity;
import cn.lankao.com.lovelankao.activity.LoginActivity;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.MyViewHolder> {
    private Context context;
    private List<Square> data;
    public SquareAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void addData(Square data) {
        this.data.add(data);
    }
    public void setData(List<Square> data) {
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.activity_square_item, parent, false));
        return holder;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Square square = data.get(position);
        if (square.getObjectId() == null){
            return;
        }
        holder.tvNickname.setText(square.getNickName());
        holder.tvTime.setText(square.getCreatedAt());
        holder.tvLikeTimes.setText(square.getLikeTimes() == null ? "0" : square.getLikeTimes() + "");
        holder.tvClickTimes.setText(square.getClickTimes() == null ? "0" : square.getClickTimes() + "");
        if (square.getSquareTitle() == null){
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setText(square.getSquareTitle());
            holder.tvTitle.setVisibility(View.VISIBLE);
        }
        holder.tvUserType.setText(TextUtil.getVipString(square.getSquareUserType()));
        String content = square.getSquareContent();
        if (content != null && content.length() > 100){
            content = content.substring(0,80)+"...(全文)";
        }
        holder.tvContent.setText(content);
        if (!TextUtil.isNull(square.getUserPhoto())){
            x.image().bind(holder.ivPhoto, square.getUserPhoto(), BitmapUtil.getOptionCommonRadius());
        } else {
            x.image().bind(holder.ivPhoto, CommonCode.APP_ICON, BitmapUtil.getOptionCommonRadius());
        }
        holder.llPhoto.setVisibility(View.VISIBLE);
        if (square.getSquarePhoto1() != null){
            //            params.width = width/3;
//            params.leftMargin = 10;
//            holder.ivPhoto1.setLayoutParams(params);
            x.image().bind(holder.ivPhoto1, square.getSquarePhoto1().getFileUrl(), BitmapUtil.getOptionCommon());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.llPhoto.getLayoutParams();
            params.height = holder.ivPhoto1.getWidth();
            holder.llPhoto.setLayoutParams(params);
            if (square.getSquarePhoto2() == null){
                holder.ivPhoto2.setImageDrawable(null);
            } else {
                x.image().bind(holder.ivPhoto2, square.getSquarePhoto2().getFileUrl(), BitmapUtil.getOptionCommon());
//                holder.ivPhoto2.setLayoutParams(params);
            }
            if (square.getSquarePhoto3() == null){
//                holder.ivPhoto3.setVisibility(View.GONE);
                holder.ivPhoto3.setImageDrawable(null);
            } else {
                x.image().bind(holder.ivPhoto3, square.getSquarePhoto3().getFileUrl(), BitmapUtil.getOptionCommon());
//                holder.ivPhoto3.setLayoutParams(params);
            }
        }
        if (square.getSquarePhoto1() == null){
            holder.llPhoto.setVisibility(View.GONE);
        }
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME,"");
        holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimes));
        if (!TextUtil.isNull(nickname) && !TextUtil.isNull(square.getLikeUsers()) && square.getLikeUsers().contains(nickname)){
            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimesc));
        } else {
            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimes));
        }
        if (square.getCommentTimes() == null){
            holder.tvCommentTimes.setText("评论 0");
        } else {
            holder.tvCommentTimes.setText("评论 " + square.getCommentTimes());
        }
        holder.llLikeTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()){
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)) {
                    String likeUsers = square.getLikeUsers() == null ? nickname : nickname + "," + square.getLikeUsers();
                    square.setLikeUsers(likeUsers);
                    holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc));
                    final int like = square.getLikeTimes() == null ? 1 : square.getLikeTimes() + 1;
                    holder.tvLikeTimes.setText(like + "");
                    square.increment("likeTimes");
                    square.update(square.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                        }
                    });
                    square.setLikeTimes(like);
                } else {
                    ToastUtil.show("您已经点过赞了");
                }
            }
        });
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                square.increment("clickTimes");
                square.update(square.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                final int click = square.getClickTimes() == null ? 1 : square.getClickTimes() + 1;
                holder.tvClickTimes.setText(click + "");
                square.setClickTimes(click);
                Intent intent = new Intent(context, SquareActivity.class);
                intent.putExtra(CommonCode.INTENT_COMMON_OBJ, square);
                context.startActivity(intent);
            }
        });
        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()){
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(context,CommentActivity.class);
                intent.putExtra(CommonCode.INTENT_COMMENT_POSTID,square.getObjectId());
                intent.putExtra(CommonCode.INTENT_COMMENT_LASTCONTENT,"");
                intent.putExtra(CommonCode.INTENT_COMMENT_FROM_SQUARE,CommonCode.INTENT_COMMENT_FROM_SQUARE);
                context.startActivity(intent);
            }
        });
    }
    private boolean isLogin(){
        String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME, "");
        if (TextUtil.isNull(nickname)){
            ToastUtil.show("请先登录");
            return false;
        }
        return true;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvNickname;
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
        TextView tvUserType;
        ImageView ivPhoto1;
        ImageView ivPhoto2;
        ImageView ivPhoto3;
        ImageView ivLikeTimes;
        TextView tvCommentTimes;
        TextView tvLikeTimes;
        TextView tvClickTimes;
        LinearLayout llPhoto;
        LinearLayout llComment;
        LinearLayout llLikeTimes;
        LinearLayout llContent;
        public MyViewHolder(View view) {
            super(view);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_square_item_photo);
            ivPhoto1 = (ImageView) view.findViewById(R.id.iv_square_item_photo1);
            ivPhoto2 = (ImageView) view.findViewById(R.id.iv_square_item_photo2);
            ivPhoto3 = (ImageView) view.findViewById(R.id.iv_square_item_photo3);
            ivLikeTimes = (ImageView) view.findViewById(R.id.iv_square_item_liketimes);
            tvNickname = (TextView) view.findViewById(R.id.tv_square_item_nickname);
            tvTime = (TextView) view.findViewById(R.id.tv_square_item_time);
            tvTitle = (TextView) view.findViewById(R.id.tv_square_item_title);
            tvContent = (TextView) view.findViewById(R.id.tv_square_item_content);
            tvUserType = (TextView) view.findViewById(R.id.tv_square_item_usertype);
            tvLikeTimes = (TextView) view.findViewById(R.id.tv_square_item_liketimes);
            tvClickTimes = (TextView) view.findViewById(R.id.tv_square_item_clicktimes);
            tvCommentTimes = (TextView) view.findViewById(R.id.tv_square_item_commenttimes);
            llPhoto = (LinearLayout) view.findViewById(R.id.ll_square_item_photo);
            llLikeTimes = (LinearLayout) view.findViewById(R.id.ll_square_item_liketimes);
            llComment = (LinearLayout) view.findViewById(R.id.ll_square_item_comment);
            llContent = (LinearLayout) view.findViewById(R.id.ll_square_item_content);
        }
    }
}