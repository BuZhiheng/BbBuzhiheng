package cn.lankao.com.lovelankao.utils;

import java.util.Random;

/**
 * Created by BuZhiheng on 2016/5/26.
 */
public class IntegerUtils {
    public static int random(){
        Random r = new Random();
        int i = r.nextInt(5);
        return i+1;
    }
}
