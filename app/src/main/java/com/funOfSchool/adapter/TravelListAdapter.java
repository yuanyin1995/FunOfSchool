package com.funOfSchool.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.model.TravelItem;
import com.funOfSchool.ui.PersonInfoActivity;
import com.funOfSchool.ui.QueryTraceActivity;
import com.funOfSchool.ui.TravelingActivity;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_travellist_item,null);
        }
        //  获取id
        TextView school =(TextView) convertView.findViewById(R.id.travel_list_school);
        TextView date = (TextView)convertView.findViewById(R.id.travel_list_date);
        CircleImageView firstU = (CircleImageView)convertView.findViewById(R.id.firstU);
        CircleImageView secondU = (CircleImageView)convertView.findViewById(R.id.secondU);
        //  设值
        school.setText(travelItems.get(position).getTravelCollege());
        date.setText(travelItems.get(position).getTravelDate());
        Glide.with(context).load(AppUtils.HOST+travelItems.get(position).getUserAvatarUrl()).into(firstU);
        Glide.with(context).load(AppUtils.HOST+travelItems.get(position).getGuideAvatarUrl()).into(secondU);
        //  路线图点击事件
        TextView tvPath = (TextView) convertView.findViewById(R.id.travel_list_path);
        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, QueryTraceActivity.class);
                //  不是在Activity中进行跳转，需要添加这个方法
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  添加日期参数
                i.putExtra("travelDate",travelItems.get(position).getTravelDate());
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
