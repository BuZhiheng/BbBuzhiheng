package cn.lankao.com.lovelankao.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.IndexFragmentController;
/**
 * Created by BuZhiheng on 2016/5/11.
 */
public class IndexFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_index, container, false);
        new IndexFragmentController(getActivity(),view);
        return view;
    }
}