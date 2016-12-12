package com.funOfSchool.ui.http;

import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AsyncHttpMangers {
    public static void getUserList(JsonHttpResponseHandler handler){
        String url = AppUtils.HOST + ApiUtils.API_MATCH_USER_CHAT_LIST;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.GetToken());

        AsyncHttpClients.getLocal(url,param,handler);
    }
}
