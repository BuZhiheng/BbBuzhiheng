package cn.lankao.com.lovelankao.activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.entity.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
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
    private ImageOptions option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_msg);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        controller = new SquareActivityController(this);
        controller.initData(getIntent());
    }
    public void setData(final Square square,String userImg,Drawable drawable){
        option = BitmapUtil.getOptionCommon();
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
        x.image().bind(ivPhoto, userImg, BitmapUtil.getOptionRadius());
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
        x.image().bind(iv, imgUrl,option);
        iv.setVisibility(View.VISIBLE);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_square_back:
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}