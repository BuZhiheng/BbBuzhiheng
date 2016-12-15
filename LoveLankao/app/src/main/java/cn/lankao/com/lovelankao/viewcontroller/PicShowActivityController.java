package cn.lankao.com.lovelankao.viewcontroller;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.PicShowActivity;
import cn.lankao.com.lovelankao.model.AdvertNormal;
import cn.lankao.com.lovelankao.model.CommonCode;
/**
 * Created by BuZhiheng on 2016/4/20.
 */
public class PicShowActivityController implements View.OnClickListener {
    private PicShowActivity context;
    private ImageView iv0,iv1,iv2,iv3,iv4,iv5;
    public PicShowActivityController(PicShowActivity context) {
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        Intent intent = context.getIntent();
        if (intent != null){
            AdvertNormal advert = (AdvertNormal) intent.getSerializableExtra(CommonCode.INTENT_ADVERT_TYPE);
            if (advert != null){
                ImageOptions imageOptions =new ImageOptions.Builder()
                        .setCrop(false)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        .setLoadingDrawableId(R.drawable.ic_common_defult)//加载中默认显示图片
                        .build();
                if (advert.getAdvPhoto() != null){
                    x.image().bind(iv0,advert.getAdvPhoto().getFileUrl(),imageOptions);
                }else{
                    iv0.setVisibility(View.GONE);
                }
                if (advert.getAdvPhoto1() != null){
                    x.image().bind(iv1,advert.getAdvPhoto1().getFileUrl(),imageOptions);
                }else{
                    iv1.setVisibility(View.GONE);
                }
                if (advert.getAdvPhoto2() != null){
                    x.image().bind(iv2,advert.getAdvPhoto2().getFileUrl(),imageOptions);
                }else{
                    iv2.setVisibility(View.GONE);
                }
                if (advert.getAdvPhoto3() != null){
                    x.image().bind(iv3,advert.getAdvPhoto3().getFileUrl(),imageOptions);
                }else{
                    iv3.setVisibility(View.GONE);
                }
                if (advert.getAdvPhoto4() != null){
                    x.image().bind(iv4,advert.getAdvPhoto4().getFileUrl(),imageOptions);
                }else{
                    iv4.setVisibility(View.GONE);
                }
                if (advert.getAdvPhoto5() != null){
                    x.image().bind(iv5,advert.getAdvPhoto5().getFileUrl(),imageOptions);
                }else{
                    iv5.setVisibility(View.GONE);
                }
            }
        }
    }
    private void initView() {
        context.setContentView(R.layout.activity_pic_show);
        x.view().inject(context);
        iv0 = (ImageView) context.findViewById(R.id.iv_picshow_0);
        iv1 = (ImageView) context.findViewById(R.id.iv_picshow_1);
        iv2 = (ImageView) context.findViewById(R.id.iv_picshow_2);
        iv3 = (ImageView) context.findViewById(R.id.iv_picshow_3);
        iv4 = (ImageView) context.findViewById(R.id.iv_picshow_4);
        iv5 = (ImageView) context.findViewById(R.id.iv_picshow_5);
        context.findViewById(R.id.iv_picshow_back).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_picshow_back:
                context.finish();
                break;
        }
    }
}
