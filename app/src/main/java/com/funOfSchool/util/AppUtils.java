package com.funOfSchool.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AppUtils {
    /**
     * 主机地址
     */
    public static final String HOST = "http://192.168.178.2/";
    /**
     * 写入token
     */
    /**
     * 获取token
     * @return token
     */
    public static String GetToken(){
        return "f8364cf1c0a9416880eecca08e6a0d4da1l3hG";
    }

    /**
     * 打印log
     * @param msg
     */
    public static void Log(String msg){
        Log.i("FunOfSchool",msg);
    }

    /**
     * toast
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
