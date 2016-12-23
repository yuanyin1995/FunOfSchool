package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.funOfSchool.adapter.TravelListAdapter;
import com.funOfSchool.model.TravelItem;
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
import java.util.List;

/**
 * Created by lenovo on 2016/12/10.
 */
public class TravelListActivity extends Activity {
    private ImageView btnBack;
    private ListView travelListView;
    //  出游列表数据集合
    final ArrayList<TravelItem> travelIems = new ArrayList<TravelItem>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travellist);

        //  得到各组件
        fingView();
        //  设置监听器
        setListener();
        //  发送网络请求得到列表数据，并设置监听器
        getTravelList();
        //  设置回退按钮
        setBackBtn();
    }

    /**
     * 设置回退按钮
     */
    private void setBackBtn() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelListActivity.this.finish();
            }
        });
    }

    /**
     * 获得出游者ID 导游ID 游玩学校 游玩时间 出游记录状态
     */
    private void getTravelList() {
        //  发送网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_TRAVEL_LIST;
        //  请求参数：token
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        //  发送网络请求
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("SUCCESS","发送成功!");
                Log.e("RC",response.toString());

                JSONObject travelListJO = null;
                try {
                    //  获取JSONObject
                    travelListJO = new JSONObject(response.toString());
                    //  获取JSONArray
                    JSONArray travelListJA = travelListJO.getJSONArray("datum");
                    Log.e("TLJA",travelListJA.toString());
                    //  获取Travel数据集合
                    for (int i=0 ; i<travelListJA.length() ; i++){
                        travelIems.add(new TravelItem(
                                travelListJA.getJSONObject(i).getString("userId"),
                                travelListJA.getJSONObject(i).getString("guiderId"),
                                travelListJA.getJSONObject(i).getString("schoolId"),
                                travelListJA.getJSONObject(i).getString("travelTime"),
                                travelListJA.getJSONObject(i).getBoolean("flag")
                        ));
                    }
                    Log.e("travelIems",travelIems.toString());


                    //  去掉当前出游（flag=true）记录
                    for (int j=0 ; j<travelIems.size() ; j++){
                        if (travelIems.get(j).getTravelFlag()){
                            travelIems.remove(j);
                        }
                    }

                    //  修改collgeId为collegeName
                    for (int k=0 ; k<travelIems.size() ; k++){
                        //  发送网络请求
                        AsyncHttpClient client = new AsyncHttpClient();
                        String url = AppUtils.HOST + ApiUtils.API_COLLEGE_NAME;
                        //  请求参数：collegeId
                        final RequestParams param = new RequestParams();
                        param.put("collegeId",travelIems.get(k).getTravelCollege());
                        //  发送网络请求
                        final int temp = k;
                        client.post(url, param, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                JSONObject collegeNameJO = null;
                                try {
                                    //  获取JSONObject
                                    collegeNameJO = new JSONObject(response.toString());
                                    //  获取学校名称
                                    // ArrayList<String> collegeNameList = new ArrayList<String>();
                                    String collegeName = collegeNameJO
                                            .getJSONObject("datum")
                                            .getString("name");
                                    Log.e("collegeName",collegeName);
                                    travelIems.get(temp).setTravelCollege(collegeName);

                                    //  配置adapter
                                    final TravelListAdapter adapter =
                                            new TravelListAdapter(getApplicationContext(),travelIems);
                                    travelListView.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fingView() {
        btnBack = (ImageView)findViewById(R.id.travel_list_back);
        travelListView = (ListView)findViewById(R.id.travellv);
    }


}