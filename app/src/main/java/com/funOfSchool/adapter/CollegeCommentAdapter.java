package com.funOfSchool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.model.CollegeComment;
import com.funOfSchool.model.TravelItem;

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

        TextView userName = (TextView)view.findViewById(R.id.cc_user_name);
        TextView commentDate = (TextView)view.findViewById(R.id.cc_date);
        TextView score = (TextView)view.findViewById(R.id.cc_score);
        TextView comment = (TextView)view.findViewById(R.id.cc_comment);

        userName.setText(ccItems.get(i).getUserId());
        commentDate.setText(ccItems.get(i).getCommentDate());
        score.setText(ccItems.get(i).getScore()+"分");
        comment.setText(ccItems.get(i).getComment());

        return view;
    }
}
