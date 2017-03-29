package cn.lankao.com.lovelankao.utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lankao.com.lovelankao.model.CommonCode;

/**
 * Created by buzhiheng on 2016/8/4.
 */
public class TextUtil {
    public static final String EX_EMAIL = "^w+[-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$";
    public static final String EX_PHONE = "^1[3|4|5|7|8]\\d{9}$";//判断国内电话号码
    public static final String EX_NUMBER_ABC = "^[A-Za-z0-9]+$";//判断密码,字母开头,数字结尾
    public static final String regEx = "^[A-Za-z0-9]+$";
    public static final String EX_CAR_IDENTITY = "^[\u4e00-\u9fa5]{1}[A-Z][A-Z0-9]{5}$";
    public static final String EX_CHINESE = "[\u4e00-\u9fa5]";//检测是否包含中文
    public static final String EX_CARID_SIMPLE = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼";
    /**
     * 字符串为空的时候返回true
     **/
    public static boolean isNull(String s){
        if (s == null || "".equals(s)){
            return true;
        } else {
            return false;
        }
    }
    public static boolean isJson(String json){
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean strEX(String str, String ex){
        Pattern pattern = Pattern.compile(ex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static boolean strEXChinese(String str, String ex){
        Pattern p = Pattern.compile(ex);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public static boolean isCarId(String id){
        if (isNull(id)){
            return false;
        }
        if (id.length() != 7){
            return false;
        }
        if (!EX_CARID_SIMPLE.contains(id.substring(0,1))){
            return false;
        }
        if (!strEX(id,EX_CAR_IDENTITY)){
            return false;
        }
        return true;
    }
    public static String phoneSetMiddleGone(String phone){
        if (isNull(phone)){
            return "";
        } else {
            if (phone.length()<11){
                return phone;
            } else {
                String sMiddle = phone.substring(3,7);
                return phone.replaceFirst(sMiddle,"****");
            }
        }
    }
    public static String getVipString(String vipCode){
        if (isNull(vipCode)){
            return "";
        }
        if(CommonCode.USER_VIP_TYPE1.equals(vipCode)){
            return "  钻石用户 ";
        }
        if(CommonCode.USER_VIP_TYPE2.equals(vipCode)){
            return "  黄金用户  ";
        }
        if(CommonCode.USER_VIP_TYPE3.equals(vipCode)){
            return "  白银用户  ";
        }
        return "";
    }
}