package cn.lankao.com.lovelankao.utils;

import com.google.gson.Gson;

/**
 * Created by dell on 2016/4/3.
 */
public class GsonUtil {
    private static Gson gson;
    private static void setGson(){
        if (gson == null){
            gson = new Gson();
        }
    }
}
