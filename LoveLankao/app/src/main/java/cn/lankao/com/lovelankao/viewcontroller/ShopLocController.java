package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.ShopLocationActivity;

/**
 * Created by BuZhiheng on 2016/4/6.
 */
public class ShopLocController implements View.OnClickListener {
    private ShopLocationActivity context;
    private TextView tvTitle;
    private MapView mapView;
    private BaiduMap map;
    private float lat;
    private float lng;
    public ShopLocController(ShopLocationActivity context){
        this.context = context;
        initView();
    }

    private void initView() {
        context.findViewById(R.id.iv_shoploc_back).setOnClickListener(this);
        tvTitle = (TextView) context.findViewById(R.id.tv_shoploc_title);
        mapView = (MapView) context.findViewById(R.id.map_activity_shoploc);
        map = mapView.getMap();
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        Intent intent = context.getIntent();
        if (intent != null){
            lat = intent.getFloatExtra("lat",0);
            lng = intent.getFloatExtra("lng",0);
            tvTitle.setText(intent.getStringExtra("title"));
        }
        //定位到商家位置
        LatLng ll = new LatLng(lat, lng);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(ll)
                .zoom(18)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        map.setMapStatus(mMapStatusUpdate);
        //添加標註物
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_common_map_meishi);
        MarkerOptions option = new MarkerOptions().position(ll).icon(bitmap).zIndex(0).period(10);
                option.animateType(MarkerOptions.MarkerAnimateType.drop);
                map.addOverlay(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shoploc_back:
                context.finish();
                break;
        }
    }
}
