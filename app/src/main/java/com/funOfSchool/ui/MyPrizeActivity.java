package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.MyprizeAdapter;
import com.funOfSchool.model.Myprize;
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

/**
 * Created by lenovo on 2016/12/10.
 */
public class MyPrizeActivity extends Activity {
    private ListView lvMyprize; //  出游列表数据集合
    final ArrayList<Myprize> myprizelIems = new ArrayList<Myprize>();
    private String usePrizeId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprize);

        lvMyprize = (ListView)findViewById(R.id.myprize_list);

        //  得到我的奖品列表
        getMyprizeList();
    }

    /**
     * 得到我的奖品列表
     */
    private void getMyprizeList() {
        //  发送网络请求，得到我的奖品列表
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_MY_PRIZE_LIST;
        // 请求参数: token
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        // Log.e("prizetoken",AppUtils.getToken(getApplicationContext()));
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("prizeres",response.toString());

                try {
                    JSONObject myprizeJO = new JSONObject(response.toString());
                    JSONArray myprizeJA = myprizeJO.getJSONArray("datum");

                    for (int i=0 ; i<myprizeJA.length() ; i++){
                        myprizelIems.add(new Myprize(
                                myprizeJA.getJSONObject(i).getString("allocatePrizeId"),
                                myprizeJA.getJSONObject(i).getString("prizeName"),
                                myprizeJA.getJSONObject(i).getString("time")
                        ));
                    }

                    for (int j=0 ; j<myprizelIems.size() ; j++){
                        if (myprizelIems.get(j).getMyprizeId() == usePrizeId){
                            myprizelIems.remove(j);
                        }
                    }
                    Log.e("prizelist",myprizelIems.toString());

                    //  配置adapter
                    final MyprizeAdapter adapter = new MyprizeAdapter(getApplicationContext(),
                            myprizelIems);
                    lvMyprize.setAdapter(adapter);

                    lvMyprize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // long myprizeId = adapter.getItemId(i);

                            Intent intent = new Intent(MyPrizeActivity.this,MyprizeInfoActivity.class);
                            intent.putExtra("prize_info_name",myprizelIems.get(i).getMyprizeName());
                            intent.putExtra("prize_info_date",myprizelIems.get(i).getValidDate());
                            intent.putExtra("prize_info_id",myprizelIems.get(i).getMyprizeId());
                            startActivityForResult(intent,1);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMyprizeList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMyprizeList();
    }
}
