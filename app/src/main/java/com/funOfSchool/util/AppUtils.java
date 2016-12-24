package com.funOfSchool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aiome on 2016/12/12.
 */

public class AppUtils {
    /**
     * 主机地址
     */
    public static final String HOST = "http://10.7.1.205/";
    /**
     * 用户头像url
     */
    public static String AVATAR = HOST + "images/defaultUserAvatar.jpg";
    /**
     * 写入token------临时
     */
    public static void setToken(String token,Context context){
        SharedPreferences spf = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString("token",token);

        editor.commit();
    }
    /**
     * 获取token
     * @return token
     */
    public static String getToken(Context context){
        SharedPreferences spf = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        return spf.getString("token","");
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
