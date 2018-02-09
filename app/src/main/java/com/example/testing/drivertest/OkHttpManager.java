package com.example.testing.drivertest;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by USER on 2017/11/29.
 */
public class OkHttpManager {
    /**
     * 静态实例
     */
    private static OkHttpManager okHttpManager;


    /**
     * OkHttpClient实例
     */
    private OkHttpClient client;

    /**
     * 单例模式
     * 对于但是模式网上有很对写法 实际得看需求
     *
     * @return OkHttpManager
     */
    private static OkHttpManager getInstance() {
        if (okHttpManager == null)
            okHttpManager = new OkHttpManager();
        return okHttpManager;
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    //post请求header-image
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    /**
     * 构造方法
     */
    public OkHttpManager() {
        //实例化OkHttpClient
        client = new OkHttpClient();
        //配置okHttpClient的参数
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        //设置缓存信息的处理：创建缓存对象，构造方法用于控制缓存位置及最大缓存大小【单位是Byte】
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath()), 10 * 1024 * 1024);
        client.newBuilder().cache(cache);
    }
    //POST 异步访问 处理结果

    /**
     * post 异步访问 公开方法
     *
     * @param url      url
     * @param body     提交参数
     * @param callback 回调函数
     */
    public static void postEnqueueAsync(String url, RequestBody body, Callback callback) {
        OkHttpManager.getInstance().doPostEnqueueAsync(url, body, callback);
    }

    /**
     * post 异步访问 内部方法
     *
     * @param url      url
     * @param body     提交参数
     * @param callback 回调函数
     */
    private void doPostEnqueueAsync(String url, RequestBody body, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //计划好了 那就干吧
        client.newCall(request).enqueue(callback);
    }
}

