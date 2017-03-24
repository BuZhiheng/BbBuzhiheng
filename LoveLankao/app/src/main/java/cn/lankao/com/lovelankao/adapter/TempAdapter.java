package cn.lankao.com.lovelankao.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.Jock;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class TempAdapter extends RecyclerView.Adapter<TempAdapter.MyViewHolder> {
    private Context context;
    private List<Jock> data;
    public TempAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }

    public void setData(List<Jock> data) {
        this.data = data;
    }
    public void addData(List<Jock> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_jock_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Jock news = data.get(position);
        holder.tvContent.setText(news.getContent());
        holder.tvTime.setText(news.getUpdatetime());
        holder.tvClick.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvTime;
        TextView tvClick;
        public MyViewHolder(View view) {
            super(view);
            tvContent = (TextView) view.findViewById(R.id.tv_jock_item_content);
            tvTime = (TextView) view.findViewById(R.id.tv_jock_item_time);
            tvClick = (TextView) view.findViewById(R.id.tv_jock_item_click);
        }
    }
}