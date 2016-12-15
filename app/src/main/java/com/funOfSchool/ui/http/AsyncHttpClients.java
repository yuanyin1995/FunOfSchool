package com.funOfSchool.ui.http;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AsyncHttpClients {
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    public static AsyncHttpClient client = new AsyncHttpClient();

    public AsyncHttpClients() {
        client.setConnectTimeout(6000);
    }

    public static void get(AsyncHttpClient client,String partUrl, AsyncHttpResponseHandler handler) {
        client.get(partUrl, handler);
        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    /**
     * 获取服务器数据
     * @param partUrl
     * @param handler
     */
    public static void getLocal(String partUrl, AsyncHttpResponseHandler handler) {
        client.get(partUrl, handler);
        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    /**
     * 获取服务器数据
     * @param partUrl
     * @param params 请求参数
     * @param handler
     */
    public static void getLocal(String partUrl, RequestParams params,
                                AsyncHttpResponseHandler handler) {
        client.get(partUrl, params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("?")
                .append(params).toString());
    }



    public static void log(String log) {
        Log.d("FunOfSchoolApi", log);
    }

    public static void post(String partUrl, AsyncHttpResponseHandler handler) {
        client.post(partUrl, handler);
        log(new StringBuilder("POST ").append(partUrl).toString());
    }

    public static void post(String partUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(partUrl, params, handler);
        log(new StringBuilder("POST ").append(partUrl).append("?")
                .append(params).toString());
    }

    public static void postDirect(String url, RequestParams params,
                                  AsyncHttpResponseHandler handler) {
        client.post(url, params, handler);
        log(new StringBuilder("POST ").append(url).append("?").append(params)
                .toString());
    }

    public static void put(String partUrl, AsyncHttpResponseHandler handler) {
        client.put(partUrl, handler);
        log(new StringBuilder("PUT ").append(partUrl).toString());
    }

    public static void put(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.put(partUrl, params, handler);
        log(new StringBuilder("PUT ").append(partUrl).append("?")
                .append(params).toString());
    }
}
