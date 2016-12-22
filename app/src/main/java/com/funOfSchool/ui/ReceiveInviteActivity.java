package com.funOfSchool.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.funOfSchool.R;

public class ReceiveInviteActivity extends AppCompatActivity {

    private TextView receiveDate;
    private TextView receiveRemark;
    private Button receiveBtnAccept;
    private Button receiveBtnRefuse;
    private Button receiveBtnTochat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_invite);

        //  得到界面控件
        findView();
        //  为各控件设置监听器
        setListener();
    }

    private void findView() {
        receiveDate = (TextView)findViewById(R.id.receive_date_result);
        receiveRemark = (TextView)findViewById(R.id.receive_remark_result);
        receiveBtnAccept = (Button)findViewById(R.id.receive_btn_accept);
        receiveBtnRefuse = (Button)findViewById(R.id.receive_btn_refuse);
        receiveBtnTochat = (Button)findViewById(R.id.receive_btn_tochat);
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

        }
    }
}
