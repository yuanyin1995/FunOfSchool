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
import android.widget.Toast;

import com.baidu.mapapi.http.AsyncHttpClient;
import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getVerificationCode;

public class RegistActivity extends AppCompatActivity {
    private LinearLayout l;
    private LinearLayout l1;
    int height;
    int h;
    private EditText Etphone;
    private Button btn;
    private String Num;
    private TextView Tv;
    private EditText Etpsd;
    private String psd;
    private Button getnum;
    private ImageView ivregist;
    private ImageButton ib;
    private EditText code;
    private int code1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        findview();
        setlistener();
        initsdk();
/*短信验证码功能 */



        /*获取屏幕高度*/
        l=(LinearLayout)findViewById(R.id.line1);
        WindowManager wm = (WindowManager)getApplicationContext().
                getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        h = height /11;
        l1=(LinearLayout)findViewById(R.id.l1);
        l1.setMinimumHeight(h);
        Etphone =(EditText) findViewById(R.id.Et_regist_phone);
        ivregist=(ImageView) findViewById(R.id.IVregist);
        ib=(ImageButton) findViewById(R.id.IB);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(RegistActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        Etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Num = Etphone.getText().toString();
                /*验证手机号码*/
                boolean judge = isMobile(Num);
                if (judge == true) {
                    Tv.setText("您输入的手机号合法");
                    Tv.setVisibility(View.GONE);
                    ivregist.setVisibility(View.GONE);
                } else {
                    Tv.setText("您输入的手机号不合法，请重新输入");
                    Tv.setVisibility(View.VISIBLE);
                    ivregist.setVisibility(View.VISIBLE);
                }
            }
        });
        /*getnum = (Button)findViewById(R.id.Getnum);
      getnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/
        Etpsd = (EditText)findViewById(R.id.Et_regist_password);
        /*控制密码*/
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        Etpsd.setTransformationMethod(method);
        Etpsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                psd = Etpsd.getText().toString();
                         /*验证密码*/
                boolean jone = isPsd(psd);
                if (jone == true) {
                    Tv.setText("您输入的密码合法");
                    Tv.setVisibility(View.GONE);
                    ivregist.setVisibility(View.GONE);
                } else {
                    Tv.setText("您输入的密码不合法，请重新输入");
                    Tv.setVisibility(View.VISIBLE);
                    ivregist.setVisibility(View.VISIBLE);
                }
            }
        });
        btn = (Button)findViewById(R.id.Btn_regist);
        code=(EditText)findViewById(R.id.code) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  创建网络访问的类的对象
                com.loopj.android.http.AsyncHttpClient client = new com.loopj.android.http.AsyncHttpClient();
                String url = "http://10.7.88.22/api/account/register";
                RequestParams param = new RequestParams();
                param.put("loginName", Etphone.getText().toString().trim());
                param.put("code", code.getText().toString().trim());
                param.put("userName","hhhh");
                param.put("password", Etpsd.getText().toString().trim());
// 发送网络请求
                client.get(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e("response",response.toString());
                        try {

                            JSONObject JO = new JSONObject(response.toString());
                            //JSONObject JO1 = JO.getJSONObject("info");
                            code1 = JO.getInt("code");
                            //AppUtils.setToken(JO.getString("token"),RegistActivity.this);

                            if (code1==1) {
                                Toast toast = Toast.makeText(RegistActivity.this, "注册成功",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Intent i = new Intent(RegistActivity.this, LoginActivity.class);
//                                i.setClass();
                                startActivity(i);
                            } else {
                                Toast toast = Toast.makeText(RegistActivity.this, "注册失败",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }


                            /**写你要进行的操作**/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });


        Tv = (TextView)findViewById(R.id.Tv_regist);



    }
    /*smssdk*/
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
        getnum=(Button)findViewById(R.id.Getnum);
    }
    public void setlistener(){
        getnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode("+86", Num);
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
