package cn.lankao.com.lovelankao.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class GsonUtil {
    private static Gson gson = new Gson();
    public static <T> T jsonToObject(String jsonStr, Class<T> cls) {
        return gson.fromJson(jsonStr, cls);
    }
    public static <T> List<T> jsonToList(JsonElement json,Class<T> cls) {
        List<T> list = new ArrayList<>();
        try{
            JsonArray array = json.getAsJsonArray();
            for(final JsonElement elem : array){
                list.add(gson.fromJson(elem, cls));
            }
        }catch (Exception e){
        }
        return list;
    }
}
