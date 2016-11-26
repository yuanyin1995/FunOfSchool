package com.funOfSchool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.funOfSchool.util.wheelview.adapter.ArrayWheelAdapter;
import com.funOfSchool.util.wheelview.widget.WheelView;

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
    private Button selectBtnAccept;
    private Button selectBtnRefuse;
    private Button selectBtnTochat;
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
    // 记录用户所选入学年份的变量
    private String enrollYear;
    // 记录用户所选星座的变量
    private String constellation;
    // 记录用户所选年龄段的变量
    private int minAge;
    private int maxAge;
    // 记录用户所填备注的变量
    private String remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        findView();

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
        selectRemark = (EditText)findViewById(R.id.select_remark);
        selectBtnAccept = (Button)findViewById(R.id.select_btn_accept);
        selectBtnRefuse = (Button)findViewById(R.id.select_btn_refuse);
        selectBtnTochat = (Button)findViewById(R.id.select_btn_tochat);
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
        selectBtnAccept.setOnClickListener(listener);
        selectBtnRefuse.setOnClickListener(listener);
        selectBtnTochat.setOnClickListener(listener);

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
        minAgeWheelView.setSkin(com.funOfSchool.util.wheelview.widget.WheelView.Skin.Holo);
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
        maxAgeWheelView.setSkin(com.funOfSchool.util.wheelview.widget.WheelView.Skin.Holo);
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
