package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.funOfSchool.util.ApiUtils.API_ACCOUNT_USER_PROFILE;
import static com.funOfSchool.util.AppUtils.HOST;

/**
 * Created by lenovo on 2016/12/24.
 */

public class UserInfoActivity extends Activity {
    //个人资料的各个控件以及值
    private TextView getTvName;
    private TextView getTvSex;
    private TextView getTvBirthday;
    private TextView getTvYear;
    private TextView getTvStars;
    private TextView getTvPoint;
    private TextView getTvCollstellation;
    private TextView getTvSchool;
    private TextView getTvMajor;
    private CircleImageView getAvatar;
    private RelativeLayout onesComment;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpersoninfo);

        findView();



        onesComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_comment = new Intent(UserInfoActivity.this,CommentListActivity.class);
                if (userId == null){
                    intent_comment.putExtra("token",AppUtils.getToken(getApplicationContext()));
                }else {
                    intent_comment.putExtra("userId",userId);
                }

                startActivity(intent_comment);
            }
        });
        userId = getIntent().getStringExtra("userId");

        AsyncHttpClient client = new AsyncHttpClient();
        String url = HOST+API_ACCOUNT_USER_PROFILE;
        RequestParams param = new RequestParams();
        param.put("userId", userId); //填写userId的部分！
        client.put(url,param,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.e("response",response.toString());
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    String userName = isNull(profile1.getString("userName"));
                    String birthday = isNull(profile1.getString("birthday"));
                    String schoolName = isNull(profile1.getString("schoolName"));
                    String enrollment = isNull(profile1.getString("enrollment"));
                    String constellation = isNull(profile1.getString("constellation"));
                    String stars = isNull(profile1.getString("stars"));
                    String point = isNull(profile1.getString("point"));
                    String majorName = isNull(profile1.getString("majorName"));


                    getTvName.setText(userName);
                    String sex = isNull(profile1.getString("sex"));

                    if(sex.equals("0")){
                        getTvSex.setText("男");
                    }else if (sex.equals("1")){
                        getTvSex.setText("女");
                    }else {
                        getTvSex.setText(sex);
                    }
                    getTvBirthday.setText(birthday);
                    getTvSchool.setText(schoolName);
                    getTvYear.setText(enrollment);
                    getTvCollstellation.setText(constellation);
                    getTvStars.setText(stars);
                    getTvPoint.setText("积分|"+point);
                    getTvMajor.setText(majorName);
                    Log.i("profileImage",profile1.getString("profileImage"));
                    Glide.with(UserInfoActivity.this).load(AppUtils.HOST+profile1.getString("profileImage")).into(getAvatar);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            });
    }
    public String isNull(String m) {
        if (m == null || m.equals("null") || m.isEmpty()){
            return "未填写";
        }else {
            return m;
        }
    }

    private void findView() {
        getTvName = (TextView)findViewById(R.id.get_value_name);
        getTvSex = (TextView)findViewById(R.id.get_value_sex);
        getTvBirthday = (TextView)findViewById(R.id.get_value_birthday);
        getTvYear = (TextView)findViewById(R.id.get_value_year);
        getTvMajor = (TextView)findViewById(R.id.get_value_major);
        getTvSchool = (TextView)findViewById(R.id.get_value_school);
        getTvCollstellation = (TextView)findViewById(R.id.get_value_constellation);
        getTvStars = (TextView)findViewById(R.id.stars);
        getTvPoint = (TextView)findViewById(R.id.point);
        getAvatar = (CircleImageView)findViewById(R.id.get_avatarImg);
        onesComment = (RelativeLayout)findViewById(R.id.onescomment);
    }
}
