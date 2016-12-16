package cn.lankao.com.lovelankao.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.viewcontroller.TalkController;
/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class TalkFragment extends Fragment{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_talknew,container,false);
        new TalkController((AppCompatActivity) getActivity(),view);
        return view;
    }
}