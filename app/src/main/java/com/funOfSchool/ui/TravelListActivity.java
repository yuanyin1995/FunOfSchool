package com.funOfSchool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.TravelListAdapter;
import com.funOfSchool.model.TravelItem;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/10.
 */
public class TravelListActivity extends Activity {
    private ImageButton btnBack;
    private ListView travelListView;
    private ArrayList<TravelItem> travelIems = new ArrayList<TravelItem>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travellist);
        btnBack = (ImageButton)findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        travelListView = (ListView)findViewById(R.id.travellv);
        getData();
        TravelListAdapter t = new TravelListAdapter(getApplicationContext(),travelIems);
        travelListView.setAdapter(t);
    }

    private void getData() {
        //创建数据
        TravelItem t1 = new TravelItem("北京大学","2016-12-01");
        TravelItem t2 = new TravelItem("河北师范大学","2016-12-12");
        TravelItem t3 = new TravelItem("河北师范学","2016-12-12");
        TravelItem t4 = new TravelItem("河北范大学","2016-01-12");
        TravelItem t5 = new TravelItem("河师范大学","2016-12-22");
        TravelItem t6 = new TravelItem("河北大学","2016-02-12");
        //添加数据
        travelIems.add(t1);
        travelIems.add(t2);
        travelIems.add(t3);
        travelIems.add(t4);
        travelIems.add(t5);
        travelIems.add(t6);
        AsyncHttpClient client = new AsyncHttpClient();
    }
}
