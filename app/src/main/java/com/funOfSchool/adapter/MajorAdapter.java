package com.funOfSchool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funOfSchool.R;

import java.util.ArrayList;

/**
 * Created by asus1 on 2016/11/25.
 */
public class MajorAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mList = new ArrayList<String>();

    public MajorAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mList = list;
        //Log.e("mlist",mList.toString());
        //Log.e("mlistsize",mList.size()+"");
        //Log.e("mlistitem",mList.get(1).toString());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_major_item,null);
        }

        TextView tvMajorName = (TextView)view.findViewById(R.id.dialog_major_item_name);
        tvMajorName.setText(mList.get(i).toString());

        return view;
    }
}
