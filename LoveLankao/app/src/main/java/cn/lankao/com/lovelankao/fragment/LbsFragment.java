package cn.lankao.com.lovelankao.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.LBSFragmentController;

/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class LbsFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lbs, container, false);
        new LBSFragmentController(getActivity(),view);
        return view;
    }
}
