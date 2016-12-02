package com.funOfSchool.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.funOfSchool.R;
import com.hyphenate.easeui.EaseConstant;

/**
 * Created by Aiome on 2016/11/29.
 */

public class ChatActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //初始化聊天界面
        ChatFragment chatFragment = new ChatFragment();
        //将参数传递给聊天界面
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "test1");
        chatFragment.setArguments(args);

        //加载EaseUI封装的聊天界面Fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ec_layout_container, chatFragment).commit();
    }
}
