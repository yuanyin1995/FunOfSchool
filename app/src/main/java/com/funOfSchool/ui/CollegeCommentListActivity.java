package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.CollegeCommentAdapter;
import com.funOfSchool.model.CollegeComment;
import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollegeCommentListActivity extends Activity {

    private ListView ccLv;
    private String ccCollegeId;
    private String ccCollegeIdStr;
    private ArrayList<CollegeComment>  cclist = new ArrayList<CollegeComment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_comment_list);

        ccLv = (ListView)findViewById(R.id.college_comment_list);

        //  得到学校ID
        getCollegeID();

        //  获取学校评论列表
        getCollegeCommentList();
    }

    private void getCollegeID() {
        Intent i = getIntent();
        ccCollegeId = i.getIntExtra("collegeId",1001)+"";
        Log.e("cccollegeID",ccCollegeId);
    }

    private void getCollegeCommentList() {
        //  发送网络请求，得到我学校评价列表
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_COLLEGE_COMMENT;
        // 请求参数: collegeId
        RequestParams param = new RequestParams();
        param.put("collegeId", ccCollegeId);
        Log.e("ccparam",param.toString());
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("ccres", response.toString());

                try {
                    JSONObject ccJO = new JSONObject(response.toString());
                    JSONArray ccJA = ccJO.getJSONArray("datum");

                    for (int i= 0 ; i<ccJA.length() ; i++){
                        cclist.add(new CollegeComment(
                                ccJA.getJSONObject(i).getString("comment"),
                                ccJA.getJSONObject(i).getInt("score"),
                                ccJA.getJSONObject(i).getString("userId"),
                                ccJA.getJSONObject(i).getString("image"),
                                ccJA.getJSONObject(i).getString("time"),
                                ccJA.getJSONObject(i).getString("userName")
                        ));
                    }

                    Log.e("cclist",cclist.toString());

                    final CollegeCommentAdapter adapter = new CollegeCommentAdapter(
                            CollegeCommentListActivity.this,cclist);

                    ccLv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
