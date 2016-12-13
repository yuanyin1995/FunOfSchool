package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2016/12/10.
 */
public class PersonInfoActivity extends Activity {
    private String token="976d94215afa457ab133bdbce52f6542ohmrM4"; //临时测试使用
    private ImageButton btnBack; //返回按钮
    //个人资料的各个控件以及值
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvSchool;
    private TextView tvMajor;
    private TextView tvYear;
    private TextView tvCollstellation;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        //  获得各控件
        findView();
        //  为各按钮设置监听器
        setListener();
        //通过token获取个人信息
        getInfo();
    }
    private void findView(){
        btnBack = (ImageButton)findViewById(R.id.back);
        tvName = (TextView)findViewById(R.id.value_name);
        tvSex = (TextView)findViewById(R.id.value_sex);
        tvBirthday = (TextView)findViewById(R.id.value_birthday);
        tvSchool = (TextView)findViewById(R.id.value_school);
        tvMajor = (TextView)findViewById(R.id.value_major);
        tvYear = (TextView)findViewById(R.id.value_year);
        tvCollstellation = (TextView)findViewById(R.id.value_constellation);
    }
    private void setListener(){
        PersoninfoListener personinfoListener = new PersoninfoListener();
        btnBack.setOnClickListener(personinfoListener);
    }
    private void getInfo(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://10.7.82.168:8080/api/account/profile/getProfile";
        RequestParams param = new RequestParams();
        param.put("token",token);
        client.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    //Log.e("response",response.toString());
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    tvName.setText(profile1.getString("userName"));
                    int sex = profile1.getInt("sex");
                    if(sex == 0){
                        tvSex.setText("男");
                    }else{
                        tvSex.setText("女");
                    }
                    tvBirthday.setText(profile1.getString("birthday"));
                    tvSchool.setText(profile1.getString("schoolName"));
                    tvMajor.setText(profile1.getString("majorName"));
                    tvYear.setText(profile1.getString("enrollment"));
                    tvCollstellation.setText(profile1.getString("constellation"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private class PersoninfoListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    }
}
