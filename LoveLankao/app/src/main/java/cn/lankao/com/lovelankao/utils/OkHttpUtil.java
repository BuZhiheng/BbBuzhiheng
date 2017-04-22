package cn.lankao.com.lovelankao.utils;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class OkHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static ExecutorService executor = Executors.newCachedThreadPool();
    //限制系统中执行线程的数量。
    public static Subscription get(final String url, Subscriber<String> sub){
        Log.i(">>>", url);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request
                        .Builder()
                        .url(url)
                        .build();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response resp = client.newCall(request).execute();
                            if (resp.isSuccessful()) {
                                subscriber.onNext(resp.body().string());
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(null);
                            }
                        } catch (IOException e) {
                            subscriber.onError(null);
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sub);
    }
    //限制系统中执行线程的数量。
    public static Subscription post(final String url, final RequestBody body, Subscriber<String> sub){
        Log.i(">>>", url);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request
                        .Builder()
                        .url(url)
                        .post(body)
                        .build();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response resp = client.newCall(request).execute();
                            if (resp.isSuccessful()) {
                                subscriber.onNext(resp.body().string());
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(null);
                            }
                        } catch (IOException e) {
                            subscriber.onError(null);
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sub);
    }
}