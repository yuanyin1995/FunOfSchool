package com.funOfSchool.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.funOfSchool.R;
import com.funOfSchool.adapter.ShopAdapter;

public class ShopActivity extends AppCompatActivity {
    private GridView Gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__fragment_);
        //创建adapter
        ShopAdapter adapter = new ShopAdapter(ShopActivity.this);
        //绑定adapter
        Gv = (GridView)findViewById(R.id.shop_gv);
        Gv.setAdapter(adapter);

    }
}
