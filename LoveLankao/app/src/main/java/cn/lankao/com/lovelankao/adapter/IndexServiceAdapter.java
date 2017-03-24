package cn.lankao.com.lovelankao.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.WebViewActivity;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.MainService;
import cn.lankao.com.lovelankao.utils.BitmapUtil;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class IndexServiceAdapter extends RecyclerView.Adapter<IndexServiceAdapter.MyViewHolder> {
    private Context context;
    private List<MainService> data;
    private String url;
    public IndexServiceAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }

    public void setData(List<MainService> data) {
        this.data = data;
    }
    public void addData(List<MainService> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_index_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MainService service = data.get(position);
        holder.tvTitle.setText(service.getTitle());
        if (service.getFile() != null){
            x.image().bind(holder.ivPhoto,service.getFile().getFileUrl(), BitmapUtil.getOptionByRadius(30));
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(CommonCode.INTENT_ADVERT_TITLE, service.getTitle());
                intent.putExtra(CommonCode.INTENT_SETTING_URL, service.getUrl());
                intent.putExtra(CommonCode.INTENT_SHARED_DESC, service.getTitle());
                if (service.getFile() != null) {
                    intent.putExtra(CommonCode.INTENT_SHARED_IMG, service.getFile().getFileUrl());
                } else {
                    intent.putExtra(CommonCode.INTENT_SHARED_IMG, CommonCode.APP_ICON);
                }
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView tvTitle;
        ImageView ivPhoto;
        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_indexfrm_item);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_indexfrm_item_service_title);
            tvTitle = (TextView) view.findViewById(R.id.tv_indexfrm_item_title);
        }
    }
}