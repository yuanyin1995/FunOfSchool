package com.funOfSchool.ui.http;

import android.content.Context;

import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AsyncHttpMangers {

    /**
     * 获取昵称
     * @param itemId
     * @param handler
     */
    public static void exChangeItem(Context context,String itemId,AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MARKET_EXCHANGE_ITEM;
        RequestParams param = new RequestParams();
        param.put("itemId",itemId);
        param.put("token",AppUtils.getToken(context));

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 获取所有商品
     * @param handler
     */
    public static void getItem(AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MARKET_ALL_ITEM;

        AsyncHttpClients.getLocal(url,handler);
    }

    /**
     * 获取昵称
     * @param userId
     * @param handler
     */
    public static void getName(String userId,AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + "api/account/getName";
        RequestParams param = new RequestParams();
        param.put("userId",userId);

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 出游者选择结果
     * @param handler
     */
    public static void userChoose(Context context,int result, String guiderId,AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CONFIRMATION;
        RequestParams param = new RequestParams();
        param.put("guiderId",guiderId);
        param.put("result",result);
        param.put("token",AppUtils.getToken(context));

        AsyncHttpClients.getLocal(url,param,handler);
    }
    /**
     * 导游选择结果
     * @param handler
     */
    public static void guiderChoose(Context context, int result,String userId,AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_GUIDER_CONFIRMATION;
        RequestParams param = new RequestParams();
        param.put("userId",userId);
        param.put("result",result);
        param.put("token",AppUtils.getToken(context));

        AsyncHttpClients.getLocal(url,param,handler);
    }
    /**
     * 获取导游会话列表
     * @param handler
     */
    public static void getGuiderList(Context context, AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_GUIDER_CHAT_LIST;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(context));

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 获取出游者会话列表
     * @param handler
     */
    public static void getUserList(Context context,AsyncHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CHAT_LIST;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(context));

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 获取导游remark
     * @param userId
     * @param handler
     */
    public static void getGuiderRemark(Context context,String userId, JsonHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_GUIDER_CHAT_REMARK;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(context));
        param.put("userId",userId);

        AsyncHttpClients.getLocal(url,param,handler);
    }

    /**
     * 获取出游者remark
     * @param guiderId
     * @param handler
     */
    public static void getUserRemark(Context context,String guiderId, JsonHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CHAT_REMARK;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(context));
        param.put("guiderId",guiderId);

        AsyncHttpClients.getLocal(url,param,handler);
    }
}
