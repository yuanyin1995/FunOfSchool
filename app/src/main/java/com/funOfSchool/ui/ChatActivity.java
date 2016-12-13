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
    ChatFragment chatFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //初始化聊天界面
        chatFragment = new ChatFragment();
        //将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());

        //加载EaseUI封装的聊天界面Fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_chat, chatFragment).commit();
        getRemark();
    }

    /**
     * 处理是否显示remark
     */
    public void getRemark(){
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
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

                            if (remark == null){
                                remark = "无";
                            }
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
        AsyncHttpMangers.getReamrk(chatFragment.getToChatWith(),handler);
    }
}
