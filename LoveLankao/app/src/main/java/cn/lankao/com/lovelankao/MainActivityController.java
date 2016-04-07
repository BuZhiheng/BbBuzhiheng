package cn.lankao.com.lovelankao;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.List;

import cn.lankao.com.lovelankao.fragment.LbsFragment;
import cn.lankao.com.lovelankao.fragment.MainFragment;
import cn.lankao.com.lovelankao.fragment.MineFragment;
import cn.lankao.com.lovelankao.fragment.TalkFragment;
import cn.lankao.com.lovelankao.utils.MyLocationClient;

/**
 * Created by 卜智衡 on 2016/3/18.
 */
public class MainActivityController implements View.OnClickListener{
    private MainActivity context;
    private LinearLayout llIndex;
    private LinearLayout llLbs;
    private LinearLayout llTalk;
    private LinearLayout llUser;

    private ImageView ivIndex;
    private ImageView ivLbs;
    private ImageView ivTalk;
    private ImageView ivUser;

    private TextView tvIndex;
    private TextView tvLbs;
    private TextView tvTalk;
    private TextView tvUser;
    ViewPager viewPager;
    private List<Fragment> fragments;

    public MainActivityController(MainActivity context) {
        this.context = context;
        PushManager.getInstance().initialize(context.getApplicationContext());
        llIndex = (LinearLayout) context.findViewById(R.id.ll_main_index);
        llLbs = (LinearLayout) context.findViewById(R.id.ll_main_lbs);
        llTalk = (LinearLayout) context.findViewById(R.id.ll_main_talk);
        llUser = (LinearLayout) context.findViewById(R.id.ll_main_user);

        ivIndex = (ImageView) context.findViewById(R.id.iv_main_index);
        ivLbs = (ImageView) context.findViewById(R.id.iv_main_lbs);
        ivTalk = (ImageView) context.findViewById(R.id.iv_main_talk);
        ivUser = (ImageView) context.findViewById(R.id.iv_main_user);

        tvIndex = (TextView) context.findViewById(R.id.tv_main_index);
        tvLbs = (TextView) context.findViewById(R.id.tv_main_lbs);
        tvTalk = (TextView) context.findViewById(R.id.tv_main_talk);
        tvUser = (TextView) context.findViewById(R.id.tv_main_user);

        viewPager = (ViewPager) context.findViewById(R.id.vp_main_act);
        llIndex.setOnClickListener(this);
        llLbs.setOnClickListener(this);
        llTalk.setOnClickListener(this);
        llUser.setOnClickListener(this);
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new LbsFragment());
        fragments.add(new TalkFragment());
        fragments.add(new MineFragment());
        viewPager.setAdapter(new MyFragmentAdapter(context.getFragmentManager()));
        viewPager.addOnPageChangeListener(new MyFragmentListener());
        viewPager.setOffscreenPageLimit(3);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_main_index:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_main_lbs:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_main_talk:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_main_user:
                viewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
    class MyFragmentListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setBottom(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
    private void setBottom(int position) {
        switch (position) {
            case 0:
                initBottom();
                ivIndex.setImageResource(R.drawable.ic_main_indexc);
                tvIndex.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                break;
            case 1:
                initBottom();
                ivLbs.setImageResource(R.drawable.ic_main_lbsc);
                tvLbs.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                break;
            case 2:
                initBottom();
                ivTalk.setImageResource(R.drawable.ic_main_talkc);
                tvTalk.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                break;
            case 3:
                initBottom();
                ivUser.setImageResource(R.drawable.ic_main_minec);
                tvUser.setTextColor(ContextCompat.getColor(context, R.color.color_green));

                break;
            default:
                break;
        }
    }

    private void initBottom() {
        ivIndex.setImageResource(R.drawable.ic_main_index);
        tvIndex.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));

        ivLbs.setImageResource(R.drawable.ic_main_lbs);
        tvLbs.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));

        ivTalk.setImageResource(R.drawable.ic_main_talk);
        tvTalk.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));

        ivUser.setImageResource(R.drawable.ic_main_mine);
        tvUser.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));
    }
}
