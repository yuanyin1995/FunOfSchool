package com.funOfSchool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.model.Item;
import com.funOfSchool.ui.TokenError;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ShopAdapter extends BaseAdapter {
    private Context context;
    private List<Item> mList;
    public ShopAdapter(Context c,List<Item> list){
        context = c;
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mList.get(position).getItemId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_shop,null);
        }
        ImageView item_pic = (ImageView)convertView.findViewById(R.id.item_pic);
        TextView item_name = (TextView)convertView.findViewById(R.id.item_name);
        TextView item_jifen=(TextView)convertView.findViewById(R.id.item_jifen);
        Button button = (Button) convertView.findViewById(R.id.shop_btn);


        Glide.with(context).load(AppUtils.HOST + mList.get(position).getUrl()).into(item_pic);
        item_name.setText(mList.get(position).getItmeName());
        item_jifen.setText(mList.get(position).getPrice()+"");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            int code = response.getInt("code");
                            AppUtils.Log(code+"");
                            if (code == 2){
                                TokenError.Login(context);
                            }
                            if (code == 23){
                                AppUtils.showShort(context,"积分不足");
                            }
                            if (code == 0){
                                AppUtils.showShort(context,"商品不存在");
                            }
                            if (code == 1){
                                AppUtils.showShort(context,"兑换成功");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        AppUtils.showShort(context,"网络连接失败");
                    }
                };

                AsyncHttpMangers.exChangeItem(context,mList.get(position).getItemId(), handler);
            }
        });

        return convertView;
    }
    //创建adapter
    //CustomAdapter adapter = new CustomAdapter(this, R.layout.activity_shop);
}
