package com.funOfSchool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

public class ReceiveInviteActivity extends AppCompatActivity {

    private TextView receiveDate;
    private TextView receiveRemark;
    private TextView receiveName;
    private CircleImageView receiveImgAvatar;
    private Button receiveBtnAccept;
    private Button receiveBtnRefuse;
    private Button receiveBtnTochat;

    private AsyncHttpResponseHandler mHandler;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_invite);

        //  得到界面控件
        findView();
        //  初始化控件
        setUpView(getIntent());
        //  为各控件设置监听器
        setListener();
    }

    private void setUpView(Intent intent) {
        Bundle bundle = intent.getExtras();
        mUserId = bundle.getString("userId");
        receiveDate.setText(bundle.getString("time"));
        receiveName.setText(bundle.getString("userName"));
        String remark = bundle.getString("remark");
        if (remark.equals("null")){
            receiveRemark.setText("无");
        }else{
            receiveRemark.setText(remark);
        }

        Glide.with(this).load(AppUtils.HOST + bundle.getString("url")).into(receiveImgAvatar);

        mHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        };
    }

    private void findView() {
        receiveDate = (TextView)findViewById(R.id.receive_date_result);
        receiveRemark = (TextView)findViewById(R.id.receive_remark_result);
        receiveBtnAccept = (Button)findViewById(R.id.receive_btn_accept);
        receiveBtnRefuse = (Button)findViewById(R.id.receive_btn_refuse);
        receiveBtnTochat = (Button)findViewById(R.id.receive_btn_tochat);
        receiveImgAvatar = (CircleImageView) findViewById(R.id.receive_iv_avatar);
        receiveName = (TextView) findViewById(R.id.receive_from_user);
    }

    private void setListener() {
        ReceiveLisener receiveLisener = new ReceiveLisener();
        receiveBtnAccept.setOnClickListener(receiveLisener);
        receiveBtnRefuse.setOnClickListener(receiveLisener);
        receiveBtnTochat.setOnClickListener(receiveLisener);
    }

    private class ReceiveLisener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.receive_btn_accept:
                    AsyncHttpMangers.guiderChoose(getApplicationContext(),1,mUserId,mHandler);
                    startActivity(new Intent(ReceiveInviteActivity.this,MainActivity.class));
                    break;
                case R.id.receive_btn_refuse:
                    AsyncHttpMangers.guiderChoose(getApplicationContext(),0,mUserId,mHandler);
                    startActivity(new Intent(ReceiveInviteActivity.this,MainActivity.class));
                    break;
                case R.id.receive_btn_tochat:
                    break;
                default:
                    break;
            }
        }
    }
}
