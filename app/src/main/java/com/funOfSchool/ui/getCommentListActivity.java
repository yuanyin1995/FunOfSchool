package com.funOfSchool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.CommentAdapter;
import com.funOfSchool.model.Comment;
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

import static com.funOfSchool.util.AppUtils.getToken;

public class getCommentListActivity extends Activity {

    private ListView ccLv;
    private ArrayList<Comment>  cclist = new ArrayList<Comment>();
    private ImageView back;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        ccLv = (ListView)findViewById(R.id.college_comment_list);
        userId = getIntent().getStringExtra("userId");

        //  获取学校评论列表
        getCollegeCommentList();
        // back的操作
        Back();
    }
    private void Back(){
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getCollegeCommentList() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_MY_COMMENT;
        // 请求参数: token
        RequestParams param = new RequestParams();
        param.put("userId", "1216805813af438c85d249746e32ea29");
        Log.e("userId",param.toString());
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("ccres", response.toString());
                try {
                    JSONObject ccJO = new JSONObject(response.toString());
                    JSONArray ccJA = ccJO.getJSONArray("datum");

                    for (int i= 0 ; i<ccJA.length() ; i++){
                        cclist.add(new Comment(
                                ccJA.getJSONObject(i).getString("url"),
                                ccJA.getJSONObject(i).getString("userName"),
                                ccJA.getJSONObject(i).getString("time"),
                                ccJA.getJSONObject(i).getInt("score"),
                                ccJA.getJSONObject(i).getString("comment")
                        ));
                    }
                    Log.e("cclist",cclist.toString());

                    final CommentAdapter adapter = new CommentAdapter(
                            getCommentListActivity.this,cclist);

                    ccLv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
