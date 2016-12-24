package com.funOfSchool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.model.CollegeComment;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;

import java.util.ArrayList;

/**
 * Created by asus1 on 2016/12/22.
 */
public class CollegeCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CollegeComment> ccItems;

    public CollegeCommentAdapter(Context context, ArrayList<CollegeComment> ccItems) {
        this.context = context;
        this.ccItems = ccItems;
    }

    @Override
    public int getCount() {
        return ccItems.size();
    }

    @Override
    public Object getItem(int i) {
        return ccItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (null == view){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.activity_college_comment_item,null);
        }

        CircleImageView cc_user_avatar = (CircleImageView)view.findViewById(R.id.cc_user_avatar);
        TextView userName = (TextView)view.findViewById(R.id.cc_user_name);
        TextView commentDate = (TextView)view.findViewById(R.id.cc_date);
        TextView score = (TextView)view.findViewById(R.id.cc_score);
        TextView comment = (TextView)view.findViewById(R.id.cc_comment);
        ImageView pic1 = (ImageView)view.findViewById(R.id.comment_pic1);
        ImageView pic2 = (ImageView)view.findViewById(R.id.comment_pic2);
        ImageView pic3 = (ImageView)view.findViewById(R.id.comment_pic3);

        Glide.with(context).load(AppUtils.HOST+ccItems.get(i).getUserImage()).into(cc_user_avatar);
        userName.setText(ccItems.get(i).getUserName());
        commentDate.setText(ccItems.get(i).getCommentDate());
        score.setText(ccItems.get(i).getScore()+"分");
        comment.setText(ccItems.get(i).getComment());

        String commentPicUrl = ccItems.get(i).getPicUrl();
        String [] temp = null;
        temp = commentPicUrl.split(",");
        Log.i("评论图片",temp.toString());

        for (i = 0; temp[i]!=null; i++){
            if (i==0) {
                Glide.with(context).load(AppUtils.HOST + temp[i]).into(pic1);
            }else if(i==1){
                Glide.with(context).load(AppUtils.HOST + temp[i]).into(pic2);
            }else if(i==2){
                Glide.with(context).load(AppUtils.HOST + temp[i]).into(pic3);
            }else {
                break;
            }
        }

        return view;
    }
}
