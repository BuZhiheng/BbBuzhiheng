package cn.lankao.com.lovelankao.activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.Comment;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.viewcontroller.SquareActivityController;
/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class SquareActivity extends AppCompatActivity {
    private SquareActivityController controller;
    @Bind(R.id.iv_square_item_photo)
    ImageView ivPhoto;
    @Bind(R.id.tv_square_item_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_square_item_time)
    TextView tvTime;
    @Bind(R.id.tv_square_item_title)
    TextView tvTitle;
    @Bind(R.id.tv_square_item_content)
    TextView tvContent;
    @Bind(R.id.iv_square_item_photo1)
    public ImageView ivPhoto1;
    @Bind(R.id.iv_square_item_photo2)
    public ImageView ivPhoto2;
    @Bind(R.id.iv_square_item_photo3)
    public ImageView ivPhoto3;
    @Bind(R.id.iv_square_item_photo4)
    public ImageView ivPhoto4;
    @Bind(R.id.iv_square_item_photo5)
    public ImageView ivPhoto5;
    @Bind(R.id.iv_square_item_liketimes)
    ImageView ivLikeTimes;
    @Bind(R.id.tv_square_item_commenttimes)
    TextView tvCommentTimes;
    @Bind(R.id.tv_square_item_liketimes)
    TextView tvLikeTimes;
    @Bind(R.id.tv_square_item_clicktimes)
    TextView tvClickTimes;
    @Bind(R.id.ll_square_item_liketimes)
    LinearLayout llLikes;
    @Bind(R.id.ll_square_item_comment)
    LinearLayout llComment;
    private ImageOptions option;
    private ImageOptions optionPhoto;
    private Square square;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_msg);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        controller = new SquareActivityController(this);
    }
    public void setData(final Square square,String userImg,Drawable drawable){
        this.square = square;
        option = BitmapUtil.getOptionCommon();
        optionPhoto = BitmapUtil.getOptionByRadius(15);
        tvNickname.setText(square.getNickName());
        tvTime.setText(square.getCreatedAt());
        tvContent.setText(square.getSquareContent());
        tvCommentTimes.setText(square.getCommentTimes() == null ? "0" : square.getCommentTimes() + "");
        tvLikeTimes.setText(square.getLikeTimes() == null ? "0" : square.getLikeTimes() + "");
        tvClickTimes.setText(square.getClickTimes() == null ? "0" : square.getClickTimes() + "");
        if (square.getSquareTitle() == null){
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(square.getSquareTitle());
            tvTitle.setVisibility(View.VISIBLE);
        }
        x.image().bind(ivPhoto, userImg, BitmapUtil.getOptionCommonRadius());
        ivLikeTimes.setImageDrawable(drawable);
        llLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onLikeClick();
            }
        });
    }
    public void setLickIvTimes(Drawable drawable,String times){
        ivLikeTimes.setImageDrawable(drawable);
        tvLikeTimes.setText(times);
    }
    public void setIvPhoto(ImageView iv,String imgUrl){
        x.image().bind(iv, imgUrl, option);
        iv.setVisibility(View.VISIBLE);
    }
    public void showToast(String toast){
        ToastUtil.show(toast);
    }
    public void toLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_square_back:
                finish();
                break;
            case R.id.ll_square_item_commenttimes:
                controller.checkComment("");
                break;
        }
    }
    public void toComment(String id,String last){
        Intent intent = new Intent(this,CommentActivity.class);
        intent.putExtra(CommonCode.INTENT_COMMENT_POSTID,id);
        intent.putExtra(CommonCode.INTENT_COMMENT_LASTCONTENT,last);
        startActivityForResult(intent, CommonCode.INTENT_COMMON_ACTIVITY_CODE);
    }
    public void clearCommentLL(){
        llComment.removeAllViews();
    }
    public void setCommentTimes(String s){
        tvCommentTimes.setText(s);
    }
    public void setComment(final Comment comment){
        View view = LayoutInflater.from(this).inflate(R.layout.activity_square_comment,null);
        CommentHolder holder = new CommentHolder(view);
        x.image().bind(holder.ivPhoto, comment.getUserPhotoUrl(), optionPhoto);
        holder.tvNickname.setText(comment.getUsername());
        holder.tvTime.setText(comment.getCreatedAt());
        if (!TextUtil.isNull(comment.getLastUserContent())){
            holder.tvReComment.setText(comment.getLastUserContent());
            holder.tvReComment.setVisibility(View.VISIBLE);
        }
        holder.tvToReComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.checkComment("回复("+comment.getUsername()+"):"+comment.getContent());
            }
        });
        holder.tvComment.setText(comment.getContent());
        llComment.addView(view);
    }
    class CommentHolder{
        ImageView ivPhoto;
        TextView tvToReComment;
        TextView tvNickname;
        TextView tvTime;
        TextView tvReComment;
        TextView tvComment;
        public CommentHolder(View view){
            ivPhoto = (ImageView) view.findViewById(R.id.iv_square_comment_photo);
            tvToReComment = (TextView) view.findViewById(R.id.tv_square_comment_recomment);
            tvNickname = (TextView) view.findViewById(R.id.tv_square_comment_nickname);
            tvTime = (TextView) view.findViewById(R.id.tv_square_comment_time);
            tvReComment = (TextView) view.findViewById(R.id.tv_square_comment_recontent);
            tvComment = (TextView) view.findViewById(R.id.tv_square_comment_content);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onResult(resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        controller.initData(getIntent());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}