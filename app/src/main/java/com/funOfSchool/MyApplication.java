package com.funOfSchool;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.funOfSchool.ui.ReceiveInviteActivity;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

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
        initUmeng();
    }

    private void initUmeng() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("mytoken",deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d("mytoken","s:" + s + "     s1" + s1);
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.i("shazhu",msg.custom);
                Intent intent = new Intent(context, ReceiveInviteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    JSONObject object = new JSONObject(msg.custom);
                    intent.putExtra("userId",object.getString("userId"));
                    intent.putExtra("userName",object.getString("userName"));
                    intent.putExtra("time",object.getString("time"));
                    intent.putExtra("remark",object.getString("remark"));
                    intent.putExtra("url",object.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
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
