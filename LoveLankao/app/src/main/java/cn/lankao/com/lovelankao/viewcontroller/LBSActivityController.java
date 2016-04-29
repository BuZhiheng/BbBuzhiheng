package cn.lankao.com.lovelankao.viewcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
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
import cn.lankao.com.lovelankao.activity.AdvertMsgActivity;
import cn.lankao.com.lovelankao.activity.LBSActivity;
import cn.lankao.com.lovelankao.entity.AdvertNormal;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/4/1.
 */
public class LBSActivityController implements View.OnClickListener {
    private LBSActivity context;
    private MapView mapView;
    private BaiduMap map;
    private TextView tvTitle;
    private SubActionButton btn1, btn2, btn3, btn4, btn5;
    private FloatingActionMenu menu;
    private List<AdvertNormal> data;
    public LBSActivityController(LBSActivity context) {
        this.context = context;
        initView();
        initData(CommonCode.ADVERT_OTHER);
    }

    private void initData(int type) {
        map.clear();
        data = new ArrayList<>();
        BmobQuery<AdvertNormal> query = new BmobQuery<>();
        if (type != CommonCode.ADVERT_OTHER){
            query.addWhereEqualTo("advType",type);
        }
        query.findObjects(context, new FindListener<AdvertNormal>() {
            @Override
            public void onSuccess(List<AdvertNormal> list) {
                data = list;
                setMapMarker();
                menu.close(true);
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
            Marker marker;
            advert = data.get(i);
            if (advert.getAdvLat() != null && advert.getAdvLng() != null){
                ll = new LatLng(advert.getAdvLat(), advert.getAdvLng());
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_common_map_meishi);
                option = new MarkerOptions().position(ll).icon(bitmap).zIndex(0).period(10);
                option.animateType(MarkerOptions.MarkerAnimateType.drop);
                marker = (Marker) map.addOverlay(option);
                Bundle b = new Bundle();
                b.putSerializable("data",data.get(i));
                marker.setExtraInfo(b);
            }
        }
    }

    private void initView() {
        context.findViewById(R.id.iv_lbsact_back).setOnClickListener(this);
        mapView = (MapView) context.findViewById(R.id.map_lbs_act);
        map = mapView.getMap();
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        //定位到兰考位置
        LatLng ll = new LatLng(34.828593,114.827743);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(ll)
                .zoom(15)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        map.setMapStatus(mMapStatusUpdate);
        tvTitle = (TextView) context.findViewById(R.id.tv_lbsact_title);
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
        icon1.setImageResource(R.drawable.ic_mainfrm_chihewanle);
        icon2.setImageResource(R.drawable.ic_mainfrm_women);
        icon3.setImageResource(R.drawable.ic_mainfrm_offer);
        icon4.setImageResource(R.drawable.ic_mainfrm_zu);
        icon5.setImageResource(R.drawable.ic_mainfrm_other);
        btn1 = itemBuilder.setContentView(icon1).build();
        btn2 = itemBuilder.setContentView(icon2).build();
        btn3 = itemBuilder.setContentView(icon3).build();
        btn4 = itemBuilder.setContentView(icon4).build();
        btn5 = itemBuilder.setContentView(icon5).build();
        menu = new FloatingActionMenu
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
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundler = marker.getExtraInfo();
                if (bundler != null){
                    final AdvertNormal advert = (AdvertNormal) bundler.getSerializable("data");
                    if(advert != null){
                        //创建InfoWindow展示的view
                        Button button = new Button(context);
                        button.setText(advert.getTitle());
                        //定义用于显示该InfoWindow的坐标点
                        LatLng pt = new LatLng(advert.getAdvLat(),advert.getAdvLng());
                        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                        //显示InfoWindow
                        map.showInfoWindow(mInfoWindow);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AdvertMsgActivity.class);
                                intent.putExtra("data",advert);
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            tvTitle.setText("吃喝玩乐");
            initData(CommonCode.ADVERT_CHIHEWANLE);
        } else if (v == btn2) {
            tvTitle.setText("佳丽专区");
            initData(CommonCode.ADVERT_WOMEN);
        } else if (v == btn3) {
            tvTitle.setText("招聘租赁");
            initData(CommonCode.ADVERT_OFFER);
        } else if (v == btn4) {
            tvTitle.setText("爱家");
            initData(CommonCode.ADVERT_ZULIN);
        } else if (v == btn5) {
            tvTitle.setText("全部商家");
            initData(CommonCode.ADVERT_OTHER);
        } else if(v.getId() == R.id.iv_lbsact_back){
            context.finish();
        }
    }
}
