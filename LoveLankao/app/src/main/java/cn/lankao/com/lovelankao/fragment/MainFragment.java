package cn.lankao.com.lovelankao.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.MainFragmentController;
/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class MainFragment extends Fragment{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main,container,false);
        new MainFragmentController(getActivity(),view);
        return view;
    }
}