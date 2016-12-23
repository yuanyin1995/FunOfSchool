package com.funOfSchool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funOfSchool.R;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ShopAdapter extends BaseAdapter {
    private Context context;
    public ShopAdapter(Context c){
        context = c;
    }
    private String[] conNames = {
            "水杯1","水杯2","水杯3","水杯4", "水杯5","水杯6"
    };
    private int[] conIcons = {
            R.mipmap.thingone,R.mipmap.thingtwo,R.mipmap.thingthree,
            R.mipmap.thingfour,R.mipmap.thingfive,R.mipmap.thingsix
    };
    private int[] conIscores ={
            1000,500,800,1500,300,600
    };
    @Override
    public int getCount() {
        return conIscores.length;
    }

    @Override
    public Object getItem(int position) {
        return conIscores[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_shop,null);
        }
        ImageView item_pic = (ImageView)convertView.findViewById(R.id.item_pic);
        TextView item_name = (TextView)convertView.findViewById(R.id.item_name);
        TextView item_jifen=(TextView)convertView.findViewById(R.id.item_jifen);
        item_pic.setImageResource(conIcons[position]);
        item_name.setText(conNames[position]);
        item_jifen.setText(conIscores[position]+"");
        return convertView;
    }
    //创建adapter
    //CustomAdapter adapter = new CustomAdapter(this, R.layout.activity_shop);
}
