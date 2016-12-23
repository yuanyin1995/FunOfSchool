package com.funOfSchool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.model.Myprize;

import java.util.ArrayList;

/**
 * Created by asus1 on 2016/12/21.
 */
public class MyprizeAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Myprize> myprizeItems;
    public MyprizeAdapter(Context c,ArrayList<Myprize> l){
        context = c;
        myprizeItems = l;
    }
    @Override
    public int getCount() {
        return myprizeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return myprizeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_myprize_item,null);
        }

        TextView prizeName = (TextView) convertView.findViewById(R.id.prize_name);
        TextView validDate = (TextView) convertView.findViewById(R.id.prize_valid_date);

        prizeName.setText(myprizeItems.get(position).getMyprizeName());
        validDate.setText(myprizeItems.get(position).getValidDate());

        return convertView;
    }
}
