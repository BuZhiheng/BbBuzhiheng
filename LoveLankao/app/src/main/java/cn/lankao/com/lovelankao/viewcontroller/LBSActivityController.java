package cn.lankao.com.lovelankao.viewcontroller;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.ToastUtil;

/**
 * Created by BuZhiheng on 2016/4/1.
 */
public class LBSActivityController implements View.OnClickListener {
    private LBSActivity context;
    private MapView mapView;
    private BaiduMap map;
    private SubActionButton btn1, btn2, btn3, btn4, btn5;
    private List<AdvertNormal> data;
    public LBSActivityController(LBSActivity context) {
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        map.clear();
        data = new ArrayList<>();
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<AdvertNormal>() {
            @Override
            public void onSuccess(List<AdvertNormal> list) {
                data = list;
                setMapMarker();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMapMarker() {
        AdvertNormal advert;
        LatLng ll;
        BitmapDescriptor bitmap;
        MarkerOptions option;
        for(int i=0;i<data.size();i++){
            advert = data.get(i);
            if (advert.getAdvLat() != null && advert.getAdvLng() != null){
                ll = new LatLng(advert.getAdvLat(), advert.getAdvLng());
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_common_map_meishi);
                option = new MarkerOptions().position(ll).icon(bitmap).zIndex(0).period(10);
                option.animateType(MarkerOptions.MarkerAnimateType.drop);
                map.addOverlay(option);
            }
        }
    }

    private void initView() {
        mapView = (MapView) context.findViewById(R.id.map_lbs_act);
        map = mapView.getMap();
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        ImageView icon = new ImageView(context);
        icon.setImageResource(R.drawable.ic_common_add);
        FloatingActionButton actionButton = new FloatingActionButton
                .Builder(context)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(context);
        ImageView icon1 = new ImageView(context);
        ImageView icon2 = new ImageView(context);
        ImageView icon3 = new ImageView(context);
        ImageView icon4 = new ImageView(context);
        ImageView icon5 = new ImageView(context);
        icon1.setImageResource(R.drawable.ic_mainfrm_meishi);
        icon2.setImageResource(R.drawable.ic_mainfrm_jiudian);
        icon3.setImageResource(R.drawable.ic_mainfrm_xiuxian);
        icon4.setImageResource(R.drawable.ic_mainfrm_liren);
        icon5.setImageResource(R.drawable.ic_mainfrm_other);
        btn1 = itemBuilder.setContentView(icon1).build();
        btn2 = itemBuilder.setContentView(icon2).build();
        btn3 = itemBuilder.setContentView(icon3).build();
        btn4 = itemBuilder.setContentView(icon4).build();
        btn5 = itemBuilder.setContentView(icon5).build();
        new FloatingActionMenu
                .Builder(context)
                .addSubActionView(btn1)
                .addSubActionView(btn2)
                .addSubActionView(btn3)
                .addSubActionView(btn4)
                .addSubActionView(btn5)
                .attachTo(actionButton)
                .build();
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            initData();
        } else if (v == btn2) {
            ToastUtil.show("btn2");
        } else if (v == btn3) {
            ToastUtil.show("btn3");
        } else if (v == btn4) {
            ToastUtil.show("btn4");
        } else if (v == btn5) {
            ToastUtil.show("btn5");
        }
    }
}
