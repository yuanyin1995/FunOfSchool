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
 * Created by asus1 on 2016/11/25.
 */
public class ConstellationAdapter extends BaseAdapter {
    private Context mContext;

    public ConstellationAdapter(Context context){
        mContext = context;
    }

    // 设置星座名称与图标
    private String[] conNames = {
            "牡羊座","金牛座","双子座","巨蟹座",
            "狮子座","处女座","天枰座","天蝎座",
            "射手座","摩羯座","水瓶座","双鱼座"
    };
    private int[] conIcons = {
            R.mipmap.moyangzuo,R.mipmap.jinniuzuo,R.mipmap.shuangzizuo,
            R.mipmap.juxiezuo,R.mipmap.shizizuo,R.mipmap.chunvzuo,
            R.mipmap.tianpingzuo,R.mipmap.tianxiezuo,R.mipmap.sheshouzuo,
            R.mipmap.mojiezuo,R.mipmap.shuipingzuo,R.mipmap.shuangyuzuo};

    @Override
    public int getCount() {
        return conNames.length;
    }

    @Override
    public Object getItem(int i) {
        return conNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_constellation_item,null);
        }

        TextView tvConName = (TextView)view.findViewById(R.id.dialog_constellation_item_name);
        ImageView ivConIcon = (ImageView)view.findViewById(R.id.dialog_constellation_item_icon);
        tvConName.setText(conNames[i]);
        ivConIcon.setImageResource(conIcons[i]);

        return view;
    }
}
