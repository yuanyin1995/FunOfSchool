package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;

public class MyprizeInfoActivity extends Activity {

    private TextView tvPrizeInfoName;
    private TextView tvPrizeInfoValidDate;
    private Button btnUse;
    private ImageView btnBack;

    private String prizeInfoId;
    private String prizeInfoName;
    private String prizeInfoValidDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprize_info);

        findView();

        setListener();

        setPrizeInfo();
    }

    /**
     * 设置奖品信息内容
     */
    private void setPrizeInfo() {
        Intent intent = getIntent();
        prizeInfoId = intent.getStringExtra("prize_info_id");
        prizeInfoName = intent.getStringExtra("prize_info_name");
        prizeInfoValidDate = intent.getStringExtra("prize_info_date");

        tvPrizeInfoName.setText(prizeInfoName);
        tvPrizeInfoValidDate.setText(prizeInfoValidDate);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  请求将已使用的卡券删除
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST + ApiUtils.API_DELETE_PRIZE;
                // 请求参数: token, allocateprizeId
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(getApplicationContext()));
                param.put("allocatePrizeId",prizeInfoId);

                Log.e("prizeinfoparam",param.toString());

                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("prizeinfores",response.toString());

                        try {
                            JSONObject usePrizeJO = new JSONObject(response.toString());
                            int code = usePrizeJO.getInt("code");
                            Log.e("pcode",code+"");

                            if (code == 1){
                                Toast.makeText(getApplicationContext(),
                                        "使用成功~",
                                        Toast.LENGTH_LONG).show();

                                //  返回卡券列表页要删除的奖品ID
                                Intent res = new Intent();
                                res.putExtra("usePrizeId",prizeInfoId);
                                setResult(1,res);
                                Log.e("up",prizeInfoId);

                                MyprizeInfoActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

    }

    /**
     * 得到各界面组件
     */
    private void findView() {
        tvPrizeInfoName = (TextView)findViewById(R.id.myprize_info_name);
        tvPrizeInfoValidDate = (TextView)findViewById(R.id.myprize_info_valid_date);
        btnUse = (Button)findViewById(R.id.prize_info_btn_use);
        btnBack = (ImageView)findViewById(R.id.prize_info_back);
    }
}
