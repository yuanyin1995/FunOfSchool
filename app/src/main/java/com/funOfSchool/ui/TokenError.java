package com.funOfSchool.ui;

import android.content.Context;
import android.content.Intent;

import com.funOfSchool.util.AppUtils;

/**
 * Created by Aiome on 2016/12/18.
 */

public class TokenError {
    public static void Login(Context context){
        AppUtils.showShort(context,"登录过期,请重新登录!");
        Intent intent = new Intent(context,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
