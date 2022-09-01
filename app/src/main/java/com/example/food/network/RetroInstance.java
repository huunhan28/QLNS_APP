package com.example.food.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.food.util.AppUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {



    //public static String BASE_URL = "http://172.25.176.1:8080/api/";

    public static String BASE_URL = AppUtils.BASE_URL;
//    public static String BASE_URL = "http://10.0.2.2:8080/api/";



    private static Retrofit retrofit;
    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static final String Authorization = "";
    private static final int READ_TIMEOUT = 5000;
    private static final int WRITE_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;
    private static String CACHE_CONTROL = "Cache-Control";
    private static final String TIME_CACHE_ONLINE = "public, max-age = 60";// 1 minute
    private static final String TIME_CACHE_OFFLINE = "public, only-if-cached, max-stale = 86400";//1 day

    private static OkHttpClient initClient(final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(context.getCacheDir(), CACHE_SIZE))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (isNetworkAvailable(context)) {
                        request = request
                                .newBuilder()
                                .header(CACHE_CONTROL, TIME_CACHE_ONLINE)
                                .addHeader("Authorization", Authorization )
                                .build();
                    } else {
                        request = request
                                .newBuilder()
                                .header(CACHE_CONTROL, TIME_CACHE_OFFLINE)
                                .addHeader("Authorization", Authorization)
                                .build();
                    }
                    HttpUrl httpUrl = request.url()
                            .newBuilder()
//                            .addQueryParameter(QUERRY_PARAMETER_API_KEY, API_KEY)
                            .build();
                    Request.Builder requestBuilder = request
                            .newBuilder()
                            .url(httpUrl);
                    return chain.proceed(requestBuilder.build());
                });
        return builder.build();
    }

    public static Retrofit getRetrofitClient(){
        if(retrofit==null){
             retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtils.BASE_URL+"api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            System.out.println("retrofit build:"+ retrofit.toString());
        }
        return retrofit;
    }

    public static Retrofit getRetrofitExpand(Context context, String url){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(initClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            System.out.println("retrofit build:"+ retrofit.toString());
        }
        return retrofit;
    }

    public static Retrofit getRetrofitClient(Context context){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtils.BASE_URL+"api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(initClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            System.out.println("retrofit build:"+ retrofit.toString());
        }
        return retrofit;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
