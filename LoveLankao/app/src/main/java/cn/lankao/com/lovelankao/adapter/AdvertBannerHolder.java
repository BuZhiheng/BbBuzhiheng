package cn.lankao.com.lovelankao.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bigkoo.convenientbanner.holder.Holder;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.lankao.com.lovelankao.activity.PhotoViewPagerActivity;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
/**
 * Created by buzhiheng on 2016/12/1.
 */
public class AdvertBannerHolder implements Holder<String> {
    private ImageView iv;
    private TextView tvIndex;
    private List<String> list;
    public AdvertBannerHolder(List<String> list,TextView tvIndex){
        this.tvIndex = tvIndex;
        this.list = list;
    }
    @Override
    public View createView(Context context) {
        iv = new ImageView(context);
        return iv;
    }
    @Override
    public void UpdateUI(final Context context, final int position, final String d) {
        final String s = list.get(position);
        x.image().bind(iv, s, BitmapUtil.getOptionCommon());
        tvIndex.setText(position+1+"/"+list.size());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                intent.putStringArrayListExtra(CommonCode.INTENT_COMMON_OBJ, (ArrayList<String>) list);
                intent.putExtra(CommonCode.INTENT_COMMON_STRING, s);
                context.startActivity(intent);
            }
        });
    }
}