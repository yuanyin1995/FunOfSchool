package com.funOfSchool;

import android.app.Application;

import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aiome on 2016/11/29.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化easeui
        EaseUI.getInstance().init(this,null);
        //开启debug模式
//        EMClient.getInstance().setDebugMode(true);
//        initUserInfo();
    }

    private void initUserInfo() {
        //get easeui instance
        EaseUI easeUI = EaseUI.getInstance();
        //需要EaseUI库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                final String[] name = {""};
                JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getInt("code") == 1){
                                name[0] = response.getJSONObject("datum").getString("userName");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AsyncHttpMangers.getName(username,handler);
                return new EaseUser("111");
            }
        });
    }
}
