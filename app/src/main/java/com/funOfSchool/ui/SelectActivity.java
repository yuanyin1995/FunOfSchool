package com.funOfSchool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.funOfSchool.adapter.ConstellationAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectActivity extends Activity {

    private RelativeLayout selectDate;
    private RelativeLayout selectSex;
    private RelativeLayout selectMajor;
    private RelativeLayout selectYear;
    private RelativeLayout selectConstellation;
    private RelativeLayout selectAge;
    private EditText selectRemark;
    private Button selectBtnSendInvite;
    private TextView selectResultDate;
    private TextView selectResultSex;
    private TextView selectResultMajor;
    private TextView selectResultYear;
    private TextView selectResultConstellation;
    private TextView selectResultAge;
    // 定义记录用户所选日期的变量
    private int year;
    private int month;
    private int day;
    // 记录用户所选性别的变量
    private int sexRBId;
    private String sex;
    // 记录用户所选专业的便令
    private String majorName;
    // 记录用户所选入学年份的变量
    private String enrollYear;
    // 记录用户所选星座的变量
    private String constellation;
    // 记录用户所选年龄段的变量
    private int minAge;
    private int maxAge;
    // 记录用户所填备注的变量
    private String remark;
    // 所选学校的ID
    private int selectCollegeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //  得到各控件
        findView();
        //  为各控件设置监听器
        setListener();
    }

    /**
     * 得到各界面控件
     */
    private void findView() {
        selectDate = (RelativeLayout)findViewById(R.id.select_date);
        selectSex = (RelativeLayout)findViewById(R.id.select_sex);
        selectMajor = (RelativeLayout)findViewById(R.id.select_major);
        selectYear = (RelativeLayout)findViewById(R.id.select_year);
        selectConstellation = (RelativeLayout)findViewById(R.id.select_constellation);
        selectAge = (RelativeLayout)findViewById(R.id.select_age);
        selectBtnSendInvite = (Button)findViewById(R.id.select_btn_sendinvite);
        selectRemark = (EditText)findViewById(R.id.select_remark);
        selectResultDate = (TextView)findViewById(R.id.select_date_result);
        selectResultSex = (TextView)findViewById(R.id.select_sex_result);
        selectResultMajor = (TextView)findViewById(R.id.select_major_result);
        selectResultYear = (TextView)findViewById(R.id.select_year_result);
        selectResultConstellation = (TextView)findViewById(R.id.select_constellation_result);
        selectResultAge = (TextView)findViewById(R.id.select_age_result);
    }

    /**
     * 为各控件设置监听器
     */
    private void setListener() {
        SelectListener listener = new SelectListener();
        selectDate.setOnClickListener(listener);
        selectSex.setOnClickListener(listener);
        selectMajor.setOnClickListener(listener);
        selectYear.setOnClickListener(listener);
        selectConstellation.setOnClickListener(listener);
        selectAge.setOnClickListener(listener);

        selectRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                remark = selectRemark.getText().toString();
            }
        });
    }

    /**
     * 实现监听器
     */
    private class SelectListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.select_date:
                    setDateDialog();
                    break;
                case R.id.select_sex:
                    setSexDialog();
                    break;
                case R.id.select_major:
                    setMajorDialog();
                    break;
                case R.id.select_year:
                    setEnrollYearDialog();
                    break;
                case R.id.select_constellation:
                    setConstellationDialog();
                    break;
                case R.id.select_age:
                    setAgeDialog();
                    break;
            }
        }
    }

    /**
     * 设置时间对话框
     */
    private void setDateDialog(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 设置对话框标题
        builder.setTitle("选择出游时间");

        // 加载对话框布局
        View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_date,null);
        builder.setView(dialogLayout);

        // 获取日期选择器
        DatePicker datePicker = (DatePicker)dialogLayout.findViewById(R.id.dialog_date_datePicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                SelectActivity.this.year = year;
                SelectActivity.this.month = month;
                SelectActivity.this.day = day;
            }
        });

        // 点击确定按钮时更新用户选择的日期
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectResultDate.setText(year+"-"+month+"-"+day);
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    /**
     * 设置性别对话框
     */
    private void setSexDialog(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 设置对话框标题
        builder.setTitle("选择性别");

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_sex,null);
        builder.setView(dialogLayout);

        // 获取选择按钮
        RadioGroup sexRG = (RadioGroup)dialogLayout.findViewById(R.id.dialog_sex_rg);

        sexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexRBId = radioGroup.getCheckedRadioButtonId();
            }
        });

        // 点击确定按钮时更新用户选择
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioButton checkedRB = (RadioButton)dialogLayout.findViewById(sexRBId);
                sex = checkedRB.getText().toString();
                selectResultSex.setText(sex);
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    /**
     * 设置专业对话框
     */
    private void setMajorDialog(){
        // 获取学校ID
        Intent intent = getIntent();
        selectCollegeId = intent.getIntExtra("scid",1001);
        Toast.makeText(SelectActivity.this,selectCollegeId+"",Toast.LENGTH_SHORT).show();

        // 创建对话框 Builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 设置对话框标题
        builder.setTitle("选择专业");

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_major,null);
        builder.setView(dialogLayout);

        // 创建专业数据列表
        final ArrayList<String> majorNameList = new ArrayList<String>();

        // 根据学校ID，发送网络请求，获得专业列表
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://172.16.22.229/api/college/searchMajor";
        // 请求参数：关键词
        RequestParams param = new RequestParams();
        param.put("collegeId",selectCollegeId);
        // 发送网络请求
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONObject response) {
                Log.e("SUCCESS","发送成功!");
                // response为返回的JSON对象
                Log.e("Response:", response.toString());

                JSONObject majorNameListJO = null;
                try {
                    // 获取JSONObject
                    majorNameListJO = new JSONObject(response.toString());
                    // 获取JSONArray
                    JSONArray majorNameListJA = majorNameListJO.getJSONArray("datum");
                    // 给学校名列表赋值
                    for (int j=0; j<majorNameListJA.length();j++){
                        majorNameList.add(j,majorNameListJA.getJSONObject(j).getString("name"));
                    }
                    Log.e("MAJOR", majorNameList.toString());

                    // 设置对话框列表内容
                    WheelView majorWheelView = (WheelView)dialogLayout.findViewById(R.id.major_wheelview);
                    majorWheelView.setWheelAdapter(new ArrayWheelAdapter(SelectActivity.this));
                    majorWheelView.setSkin(com.wx.wheelview.widget.WheelView.Skin.Holo);
                    majorWheelView.setWheelData(majorNameList);
                    majorWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                        @Override
                        public void onItemSelected(int position, Object o) {
                            majorName = majorNameList.get(position);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // 点击确定按钮时更新用户选择
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectResultMajor.setText(majorName);
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    /**
     * 设置入学年份对话框
     */
    private void setEnrollYearDialog(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_enroll_year,null);
        builder.setView(dialogLayout);

        // 获得输入框
        final EditText etYear = (EditText)dialogLayout.findViewById(R.id.dialog_enrollment_year_et);
        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enrollYear = etYear.getText().toString();
            }
        });

        // 设置对话框标题
        builder.setTitle("选择入学年份");

        // 创建 adapter
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectResultYear.setText(enrollYear);
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    /**
     * 设置星座对话框
     */
    private void setConstellationDialog(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_constellation,null);
        builder.setView(dialogLayout);

        // 设置对话框标题
        builder.setTitle("选择星座");

        // 创建 adapter
        final ConstellationAdapter adapter = new ConstellationAdapter(SelectActivity.this);

        // 设置 adapter 和 监听器
        builder.setAdapter(adapter,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                constellation = adapter.getItem(i).toString();
                selectResultConstellation.setText(constellation);
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    /**
     * 设置年龄段对话框
     */
    private void setAgeDialog(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);

        // 设置对话框标题
        builder.setTitle("选择年龄段");

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_age,null);
        builder.setView(dialogLayout);

        // 创建数据列表
        final ArrayList<String> ageList = new ArrayList<String>();
        for (int i = 1; i < 100; i++) {
            ageList.add(i+"");
        }

        WheelView minAgeWheelView = (WheelView)dialogLayout.findViewById(R.id.age_wheelview_min);
        minAgeWheelView.setWheelAdapter(new ArrayWheelAdapter(SelectActivity.this));
        minAgeWheelView.setSkin(com.wx.wheelview.widget.WheelView.Skin.Holo);
        minAgeWheelView.setWheelData(ageList);
        minAgeWheelView.setExtraText("最小年龄", Color.parseColor("#0288ce"), 24, -80);
        minAgeWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                minAge = Integer.parseInt(ageList.get(position));
            }
        });

        WheelView maxAgeWheelView = (WheelView)dialogLayout.findViewById(R.id.age_wheelview_max);
        maxAgeWheelView.setWheelAdapter(new ArrayWheelAdapter(SelectActivity.this));
        maxAgeWheelView.setSkin(com.wx.wheelview.widget.WheelView.Skin.Holo);
        maxAgeWheelView.setWheelData(ageList);
        maxAgeWheelView.setExtraText("最大年龄", Color.parseColor("#0288ce"), 24, -80);
        maxAgeWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                maxAge = Integer.parseInt(ageList.get(position));
            }
        });

        // 点击确定按钮时更新用户选择
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (minAge < maxAge){
                    selectResultAge.setText(minAge+"~"+maxAge);
                }
                else if(minAge == maxAge){
                    selectResultAge.setText(minAge);
                }
                else {
                    Toast.makeText(SelectActivity.this,
                            "最大年龄必须大于最小年龄，请重新选择",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }
}
