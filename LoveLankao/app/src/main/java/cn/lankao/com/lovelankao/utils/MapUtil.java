package cn.lankao.com.lovelankao.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;

/**
 * Created by BuZhiheng on 2016/4/6.
 */
public class MapUtil {
    public static String getDistance(LatLng ll1,LatLng ll2){
        Double dis = DistanceUtil.getDistance(ll1,ll2);
        DecimalFormat df = new DecimalFormat("#.00");
        if (dis == null){
            return "";
        }else {
            if (dis <= 1000){
                return df.format(dis)+"m";
            }else{
                float d = (float) (dis/1000);
                if (d > 10000){
                    return "";
                }else{
                    return df.format(d) + "km";
                }
            }
        }
    }
}