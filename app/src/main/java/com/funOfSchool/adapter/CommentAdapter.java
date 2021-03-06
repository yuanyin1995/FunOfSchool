package com.funOfSchool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.model.Comment;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;

import java.util.ArrayList;

/**
 * Created by asus1 on 2016/12/22.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> ccItems;

    public CommentAdapter(Context context, ArrayList<Comment> ccItems) {
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
                    .inflate(R.layout.activity_comment_item,null);
        }

        CircleImageView cc_user_avatar = (CircleImageView)view.findViewById(R.id.user_avatar);
        TextView userName = (TextView)view.findViewById(R.id.user_name);
        TextView commentDate = (TextView)view.findViewById(R.id.date);
        TextView score = (TextView)view.findViewById(R.id.score);
        TextView comment = (TextView)view.findViewById(R.id.comment);

        Glide.with(context).load(AppUtils.HOST+ccItems.get(i).getUserImage()).into(cc_user_avatar);
        userName.setText(ccItems.get(i).getUserName());
        commentDate.setText(ccItems.get(i).getCommentDate());
        score.setText(ccItems.get(i).getScore() + "");
        comment.setText(ccItems.get(i).getComment());
        return view;
    }
}
