package com.funOfSchool.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funOfSchool.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getVerificationCode;

public class PasswordActivity extends AppCompatActivity {
    private Button btnsms;
    private EditText Etpsdnew;
    private EditText psdnew;
    private String contents;
    private String cons;
    private TextView tv_psd;
    private Button btn;
    private EditText psd_phone;
    private String Numphone;
    private ImageView ivpsd;
    private LinearLayout l2;
    private ImageButton ib2;
    int height2;
    int h2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        findview();
        setlistener();
        initsdk();

        l2=(LinearLayout)findViewById(R.id.line2);
        ib2=(ImageButton)findViewById(R.id.IB2);
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(PasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        WindowManager wm = (WindowManager)getApplicationContext().
                getSystemService(Context.WINDOW_SERVICE);
        height2 = wm.getDefaultDisplay().getHeight();
        h2 = height2 /11;
        l2=(LinearLayout)findViewById(R.id.l2);
        l2.setMinimumHeight(h2);

        Etpsdnew = (EditText)findViewById(R.id.Etpsdnew);
        psdnew = (EditText)findViewById(R.id.psdnew);
        tv_psd = (TextView)findViewById(R.id.Tv_psd);
        btn = (Button)findViewById(R.id.btnpsd);
        psd_phone=(EditText)findViewById(R.id.Et_psd_phone);
        ivpsd =(ImageView)findViewById(R.id.IVpsd);

        /*控制密码密文显示*/
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        Etpsdnew.setTransformationMethod(method);
        psdnew.setTransformationMethod(method);
        psd_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Numphone = psd_phone.getText().toString();
                /*验证手机号码*/
                boolean judge = isMobile(Numphone);
                if (judge == true) {
                    tv_psd.setText("您输入的手机号合法");
                    tv_psd.setVisibility(View.GONE);
                    ivpsd.setVisibility(View.GONE);
                } else {
                    tv_psd.setText("您输入的手机号不合法，请重新输入");
                    tv_psd.setVisibility(View.VISIBLE);
                    ivpsd.setVisibility(View.VISIBLE);
                }
            }
        });
        Etpsdnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contents = Etpsdnew.getText().toString();
                    /*验证密码*/
                boolean jone = isPsd(contents);
                if (jone == true) {
                    tv_psd.setText("您输入的密码合法");
                    tv_psd.setVisibility(View.GONE);
                    ivpsd.setVisibility(View.GONE);
                } else {
                    tv_psd.setText("您输入的密码不合法，请重新输入");
                    tv_psd.setVisibility(View.VISIBLE);
                    ivpsd.setVisibility(View.VISIBLE);
                }
            }
        });
        psdnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                cons = psdnew.getText().toString();
                if(contents.equals(cons)){
                    tv_psd.setText("两次密码输入相同，修改成功");
                    tv_psd.setVisibility(View.GONE);
                    ivpsd.setVisibility(View.GONE);
                }else {
                    tv_psd.setText("两次密码输入不相同，请重新设置");
                    tv_psd.setVisibility(View.VISIBLE);
                    ivpsd.setVisibility(View.VISIBLE);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /*SMSsdk*/
    public void initsdk(){
        SMSSDK.initSDK(this, "1976ff5d7157a", "b78cfcbd11636cd1e1defee643a18c8a");
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Log.e("submit","on");
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        Log.e("git","on");
                        //获取验证码成功
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    public void  findview(){
        btnsms=(Button)findViewById(R.id.Btnsms);
    }
    public void setlistener(){
        btnsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode("+86", Numphone);
            }
        });
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
    /*控制密码为英文与数字的组合*/
    public static boolean isPsd(String Psd) {
        Pattern p = Pattern.compile("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,})$");
        Matcher m = p.matcher(Psd);

        return m.matches();
    }
}
