package com.funOfSchool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.model.TravelItem;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.location.d.a.i;

/**
 * Created by lenovo on 2016/12/14.
 */

public class TravelListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TravelItem> travelItems;
    public TravelListAdapter(Context c,ArrayList<TravelItem> l){
        context = c;
        travelItems = l;
    }
    @Override
    public int getCount() {
        return travelItems.size();
    }

    @Override
    public Object getItem(int position) {
        return travelItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_travellist_item,null);
        }
        //获取id
        TextView school =(TextView) convertView.findViewById(R.id.school);
        TextView date = (TextView)convertView.findViewById(R.id.date);
        //设值
        school.setText(travelItems.get(position).getSchool());
        Log.e("aaa",travelItems.get(position).getSchool());
        date.setText(travelItems.get(position).getDate());
        return convertView;
    }
}
