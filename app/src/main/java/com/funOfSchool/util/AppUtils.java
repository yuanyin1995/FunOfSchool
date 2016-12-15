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
        return "cf313de633594a39b4e26a75e525d7255WWVzV";
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
