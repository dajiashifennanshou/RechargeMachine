package com.develop.xdk.df.rechargemachine.http;


import com.develop.xdk.df.rechargemachine.Application.App;
import com.develop.xdk.df.rechargemachine.utils.C;
import com.develop.xdk.df.rechargemachine.utils.SharedPreferencesUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 网络请求
 */
public class HttpMethods {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                //modify by zqikai 20160317 for 对http请求结果进行统一的预处理 GosnResponseBodyConvert
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl((String) SharedPreferencesUtils.getParam(App.getContext(),C.BASE_URL_NAME,C.BASE_URL))
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    //获取Retrofit
    public Retrofit getRetrofit(){
        return retrofit;
    }
    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
