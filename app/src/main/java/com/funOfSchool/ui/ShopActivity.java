package com.funOfSchool.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.ShopAdapter;
import com.funOfSchool.model.Item;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    private GridView Gv;

    private List<Item> mList;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__fragment_);

        findView();
        initDate();
        initListener();
    }


    private void initListener() {
    }

    private void findView() {
        Gv = (GridView)findViewById(R.id.shop_gv);
        mButton = (Button)  LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.activity_shop,null)
                .findViewById(R.id.shop_btn);
    }

    private void initDate() {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 1){
                        mList = new ArrayList<Item>();
                        JSONArray datum = response.getJSONArray("datum");
                        for (int i = 0; i < datum.length(); i++){
                            mList.add(new Item(
                                    datum.getJSONObject(i).getString("itemId"),
                                    datum.getJSONObject(i).getString("url"),
                                    datum.getJSONObject(i).getString("itemName"),
                                    datum.getJSONObject(i).getInt("price")
                            ));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AppUtils.Log(response.toString());
                //创建adapter
                ShopAdapter adapter = new ShopAdapter(ShopActivity.this,mList);
                //绑定adapter

                Gv.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AppUtils.showShort(getApplicationContext(),"网络连接失败");
            }
        };

        AsyncHttpMangers.getItem(handler);
    }
}
