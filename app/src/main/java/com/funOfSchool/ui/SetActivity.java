package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;

/**
 * Created by lenovo on 2016/12/10.
 */
public class SetActivity extends Activity {
    private int height;
    private int width;
    private RelativeLayout R1;
    private Button offlogin;
    private ImageView btnBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        R1 = (RelativeLayout) findViewById(R.id.RL_set_button);
        offlogin = (Button)findViewById(R.id.Bt_set_submit);
        btnBack = (ImageView) findViewById(R.id.set_back);
        //设置按钮大小
        setTitlehigh();
        //设置监听
        offlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetActivity.this,LoginActivity.class);
                startActivity(intent);
                AppUtils.setToken("null",SetActivity.this);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //设置按钮大小
    private void setTitlehigh(){
        //获取高度
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        //设置标题栏高度
        android.view.ViewGroup.LayoutParams lp = R1.getLayoutParams();
        lp.height = (height/16);
        lp.width = (3*width/4);
    }
}
