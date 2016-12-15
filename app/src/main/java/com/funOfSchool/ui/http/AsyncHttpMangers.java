package com.funOfSchool.ui.http;

import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AsyncHttpMangers {
    public static void getGuiderList(AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_GUIDER_CHAT_LIST;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.GetToken());

        AsyncHttpClients.getLocal(url,param,handler);
    }
    public static void getUserList(AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CHAT_LIST;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.GetToken());

        AsyncHttpClients.getLocal(url,param,handler);
    }
    public static void getGuiderRemark(String userId, JsonHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_GUIDER_CHAT_REMARK;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.GetToken());
        param.put("userId",userId);

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 获取出游者remark
     * @param guiderId
     * @param handler
     */
    public static void getUserRemark(String guiderId, JsonHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CHAT_REMARK;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.GetToken());
        param.put("guiderId",guiderId);

        AsyncHttpClients.getLocal(url,param,handler);
    }
}
