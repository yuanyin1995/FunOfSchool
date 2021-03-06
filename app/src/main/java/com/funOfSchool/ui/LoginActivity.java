package com.funOfSchool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private TextView tvRegist;
    private TextView tvPassword;
    private EditText etpsd;
    private EditText etlogin;
    private String login_num;
    private String login_psd;
    private Button btn;
    private int code;
    //static public String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        };

        tvRegist = (TextView) findViewById(R.id.Regist);
        etlogin=(EditText)findViewById(R.id.Et_login);
        etlogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                login_num =etlogin.getText().toString();
            }
        });
        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, RegistActivity.class);
                startActivity(i);
            }
        });
        tvPassword = (TextView) findViewById(R.id.Password);
        etpsd = (EditText)findViewById(R.id.Et_login_psd);
        etpsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                login_psd=etpsd.getText().toString();
            }
        });
        /*密码密文*/
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        etpsd.setTransformationMethod(method);
        tvPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, PasswordActivity.class);
                startActivity(i);
            }
        });
        btn =(Button)findViewById(R.id.Btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建网络访问的类的对象
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST + "api/account/login";
                RequestParams param = new RequestParams();
                param.put("loginName", login_num);
                param.put("password", login_psd);
                Log.e("param",param.toString());

                final String psw = login_psd;

                // 发送网络请求
                client.get(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        AppUtils.showShort(getApplicationContext(),"网络连接失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e("response",response.toString());
                        try {
                            JSONObject JO = new JSONObject(response.toString());
                            //JSONObject JO1 = JO.getJSONObject("info");
                            code = JO.getInt("code");
                            AppUtils.setToken(JO.getString("token"),LoginActivity.this);

                            if (code==1) {
                                //绑定alias
                                PushAgent mPushAgent = PushAgent.getInstance(LoginActivity.this);
                                mPushAgent.addAlias(response.getJSONObject("info").getString("userId"), "userId", new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean b, String s) {
                                        AppUtils.Log(b + "  " + s);
                                    }
                                });
                                //聊天登录
                                EMClient.getInstance().login(response.getJSONObject("info").getString("userId"), psw, new EMCallBack() {

                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onProgress(int i, String s) {

                                    }
                                });
                                AppUtils.AVATAR = AppUtils.HOST + response.getJSONObject("info").getString("profileImage");
                                Toast toast = Toast.makeText(LoginActivity.this, "登录成功",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                                i.setClass();
                                startActivity(i);
                            } else {
                                Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码错误",
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
    }
}