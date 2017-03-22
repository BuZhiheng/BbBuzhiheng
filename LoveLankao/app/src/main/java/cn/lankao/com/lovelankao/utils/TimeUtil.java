package cn.lankao.com.lovelankao.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by buzhiheng on 2016/11/9.
 */
public class TimeUtil {
    public static String FORMAT_YYMMDD = "yyyy-MM-dd";
    public static String FORMAT_YYMMDD_HMS = "yyyy-MM-dd hh:mm:ss";
    public static String getTime(String format){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String date = sDateFormat.format(new Date());
        return date;
    }
    public static boolean isTrueTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(FORMAT_YYMMDD_HMS);
        try {
            //在此日期之后恢复正常
            //一般要预留5天
            Date date = sDateFormat.parse("2017-3-21 00:00:00");
            long lDate = date.getTime();
            long lNow = System.currentTimeMillis();
            if (lNow > lDate){
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}