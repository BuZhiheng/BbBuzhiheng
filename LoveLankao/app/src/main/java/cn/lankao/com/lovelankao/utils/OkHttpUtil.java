package cn.lankao.com.lovelankao.utils;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class OkHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static void get(String url, final CallBack callBack) {
        final Request request = new Request
                .Builder()
                .url(url)
                .build();
        new Thread(){
            public void run(){
                try {
                    Response resp = client.newCall(request).execute();
                    if (resp.isSuccessful()){
                        if (callBack != null){
                            callBack.success(resp.body().string());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static Observable<String> getApi(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request
                        .Builder()
                        .url(url)
                        .build();
                new Thread(){
                    public void run(){
                        try {
                            Response resp = client.newCall(request).execute();
                            if (resp.isSuccessful()){
                                subscriber.onNext(resp.body().string());
                                subscriber.onCompleted();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
    public interface CallBack{
        void success(String result);
    }
}
