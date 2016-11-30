package com.funOfSchool;

import android.app.Application;

import com.hyphenate.easeui.controller.EaseUI;

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
    }
}
