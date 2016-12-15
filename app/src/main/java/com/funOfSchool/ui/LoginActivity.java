package com.funOfSchool.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.funOfSchool.R;

public class LoginActivity extends AppCompatActivity {
    private TextView tvRegist;
    private TextView tvPassword;
    private EditText etpsd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvRegist = (TextView) findViewById(R.id.Regist);
        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, RegistActivity.class);
                startActivity(i);
            }
        });
        tvPassword = (TextView) findViewById(R.id.Password);
        etpsd = (EditText)findViewById(R.id.Et_login_psd);
        /*密码密文*/
        TransformationMethod method =  PasswordTransformationMethod.getInstance();
        etpsd.setTransformationMethod(method);
        tvPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, PasswordActivity.class);
                startActivity(i);
            }
        });
    }
}
