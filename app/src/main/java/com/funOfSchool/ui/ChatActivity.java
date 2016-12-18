package com.funOfSchool.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.funOfSchool.R;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aiome on 2016/11/29.
 */

public class ChatActivity extends AppCompatActivity{
    private ChatFragment chatFragment;
    private JsonHttpResponseHandler handler;
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        setUpView();
    }

    private void setUpView() {
        handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AppUtils.showShort(getApplicationContext(), "网络连接失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                AppUtils.Log("网络请求成功");
                try {
                    int code = response.getInt("code");
                    if (422 == code){
                        TokenError.Login(getApplicationContext());
                    }
                    if (11 == code){
                        //不显示remark
                        chatFragment.hideRemark();
                    }else if(12 == code){
                        //显示remark
                        JSONObject datum = response.getJSONObject("datum");
                        String remark;
                        String time;
                        if (datum != null){
                            remark = datum.getString("remark");
                            time = datum.getString("time");

                            if (remark.equals("null")){
                                remark = "无";
                            }
                            //设置remark，time，显示remark
                            chatFragment.setTime(time);
                            chatFragment.setRemark(remark);
                            chatFragment.showRemark();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (mType.equals("user")){
            AsyncHttpMangers.getUserRemark(chatFragment.getToChatWith(),handler);
        }else {
            AsyncHttpMangers.getGuiderRemark(chatFragment.getToChatWith(),handler);
        }

    }

    private void initView() {
        //初始化聊天界面
        chatFragment = new ChatFragment();
        //将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());

        mType = getIntent().getExtras().getString("type");
        if (mType.equals("user")){
            chatFragment.setType(true);
        }else {
            chatFragment.setType(false);
        }

        //加载EaseUI封装的聊天界面Fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_chat, chatFragment).commit();
    }
}