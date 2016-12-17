package cn.lankao.com.lovelankao.activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bm.library.PhotoView;
import org.xutils.x;
import java.util.List;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.utils.TextUtil;
/**
 * Created by buzhiheng on 2016/12/16.
 */
public class PhotoViewPagerActivity extends AppCompatActivity {
    private ViewPager mPager;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewpage);
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra(CommonCode.INTENT_COMMON_OBJ);
        if (list == null || list.size() == 0){
            finish();
            return;
        }
        mPager = (ViewPager) findViewById(R.id.vp_photo);
//        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(PhotoViewPagerActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                x.image().bind(view, list.get(position), BitmapUtil.getOptionPhotoPage());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                container.addView(view);
                return view;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        String currImg = intent.getStringExtra(CommonCode.INTENT_COMMON_STRING);
        if (list.size() > 0){
            for (int i=0;i<list.size();i++){
                if ((!TextUtil.isNull(currImg)) && currImg.equals(list.get(i))){
                    mPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }
}