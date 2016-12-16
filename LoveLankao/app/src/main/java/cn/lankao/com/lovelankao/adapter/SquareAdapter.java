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

import com.bm.library.PhotoView;

import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.PhotoViewPagerActivity;
import cn.lankao.com.lovelankao.activity.SquareActivity;
import cn.lankao.com.lovelankao.model.Square;
import cn.lankao.com.lovelankao.utils.BitmapUtil;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.utils.PrefUtil;
import cn.lankao.com.lovelankao.utils.WindowUtils;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Square> data;
    private List<String> list;
    private int width = WindowUtils.getWindowWidth();
    public SquareAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        list = new ArrayList<>();
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
        list.clear();
        holder.tvNickname.setText(square.getNickName());
        holder.tvTime.setText(square.getCreatedAt());
        holder.tvCommentTimes.setText(square.getCommentTimes() == null ? "0" : square.getCommentTimes() + "");
        holder.tvLikeTimes.setText(square.getLikeTimes() == null ? "0" : square.getLikeTimes() + "");
        holder.tvClickTimes.setText(square.getClickTimes()==null?"0":square.getClickTimes()+"");
        if (square.getSquareTitle() == null){
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setText(square.getSquareTitle());
            holder.tvTitle.setVisibility(View.VISIBLE);
        }
        String content = square.getSquareContent();
        if (content != null && content.length() > 100){
            content = content.substring(0,80)+"...(全文)";
        }
        holder.tvContent.setText(content);
        if (square.getUserPhoto() != null){
            x.image().bind(holder.ivPhoto, square.getUserPhoto(), BitmapUtil.getOptionCommonRadius());
        } else {
            x.image().bind(holder.ivPhoto, CommonCode.APP_ICON, BitmapUtil.getOptionCommonRadius());
        }
        if (square.getSquarePhoto1() == null){
            holder.llPhoto.setVisibility(View.GONE);
        } else {
            holder.llPhoto.setVisibility(View.VISIBLE);
            holder.ivPhoto1.setVisibility(View.VISIBLE);
            x.image().bind(holder.ivPhoto1, square.getSquarePhoto1().getFileUrl(), BitmapUtil.getOptionCommon());
            list.add(square.getSquarePhoto1().getFileUrl());
            holder.ivPhoto1.setOnClickListener(this);
            if (square.getSquarePhoto2() == null){
                holder.ivPhoto2.setVisibility(View.GONE);
            } else {
                holder.ivPhoto2.setVisibility(View.VISIBLE);
                x.image().bind(holder.ivPhoto2, square.getSquarePhoto2().getFileUrl(), BitmapUtil.getOptionCommon());
                list.add(square.getSquarePhoto2().getFileUrl());
            }
            if (square.getSquarePhoto3() == null){
                holder.ivPhoto3.setVisibility(View.GONE);
            } else {
                holder.ivPhoto3.setVisibility(View.VISIBLE);
                x.image().bind(holder.ivPhoto3, square.getSquarePhoto3().getFileUrl(), BitmapUtil.getOptionCommon());
                list.add(square.getSquarePhoto3().getFileUrl());
            }
        }
        final String nickname = PrefUtil.getString(CommonCode.SP_USER_NICKNAME,"");
        if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimes));
        } else {
            holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_square_liketimesc));
        }
        holder.llLikeTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (square.getLikeUsers() == null || !square.getLikeUsers().contains(nickname)){
                    final int like = square.getLikeTimes()==null?1:square.getLikeTimes()+1;
                    String likeUsers = square.getLikeUsers()==null?nickname:nickname+","+square.getLikeUsers();
                    square.setLikeTimes(like);
                    square.setLikeUsers(likeUsers);
                    holder.ivLikeTimes.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_square_liketimesc));
                    holder.tvLikeTimes.setText(like + "");
                    square.update(square.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                        }
                    });
                }
            }
        });
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (square.getClickTimes() == null){
                    square.setClickTimes(1);
                } else {
                    int i = square.getClickTimes();
                    square.setClickTimes(i+1);
                }
                square.update(square.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                    }
                });
                Intent intent = new Intent(context, SquareActivity.class);
                intent.putExtra(CommonCode.INTENT_COMMON_OBJ,square);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, PhotoViewPagerActivity.class);
        intent.putStringArrayListExtra(CommonCode.INTENT_COMMON_OBJ, (ArrayList<String>) list);
        context.startActivity(intent);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvNickname;
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
        ImageView ivPhoto1;
        ImageView ivPhoto2;
        ImageView ivPhoto3;
        ImageView ivLikeTimes;
        TextView tvCommentTimes;
        TextView tvLikeTimes;
        TextView tvClickTimes;
        LinearLayout llPhoto;
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
            tvCommentTimes = (TextView) view.findViewById(R.id.tv_square_item_commenttimes);
            tvLikeTimes = (TextView) view.findViewById(R.id.tv_square_item_liketimes);
            tvClickTimes = (TextView) view.findViewById(R.id.tv_square_item_clicktimes);
            llPhoto = (LinearLayout) view.findViewById(R.id.ll_square_item_photo);
            llLikeTimes = (LinearLayout) view.findViewById(R.id.ll_square_item_liketimes);
            llContent = (LinearLayout) view.findViewById(R.id.ll_square_item_content);
        }
    }
}