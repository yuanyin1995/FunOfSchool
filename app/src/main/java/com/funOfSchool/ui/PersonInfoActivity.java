package com.funOfSchool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.adapter.ConstellationAdapter;
import com.funOfSchool.adapter.MajorAdapter;
import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;
import com.funOfSchool.util.FileUtil;
import com.funOfSchool.util.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.funOfSchool.util.ApiUtils.API_ACCOUNT_PROFILE;
import static com.funOfSchool.util.ApiUtils.API_ACCOUNT_USER_PROFILE;
import static com.funOfSchool.util.AppUtils.HOST;
import static com.funOfSchool.util.AppUtils.getToken;

/**
 * Created by lenovo on 2016/12/10.
 */
public class PersonInfoActivity extends Activity {
    private ImageButton btnBack; //返回按钮
    private Context mContext;
    private CircleImageView avatarImg;// 头像控件
    private SelectPicPopupWindow menuWindow; // 弹出框
    private String imgUrl;//"Http://10.141.230.100:8080/api/fs/upload?token=c5b4f1079ca24e71a25e0f3b06e5de642dNRr3";
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    static public String urlpath="null";	//相片储存路径
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    //个人资料的各个控件以及值
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvYear;
    private TextView tvStars;
    private TextView tvPoint;
    private TextView tvCollstellation;
    private TextView tvSchool;
    private TextView tvMajor;
    private RelativeLayout selectSex;
    private RelativeLayout selectDate;
    private RelativeLayout selectYear;
    private RelativeLayout selectName;
    private RelativeLayout selectConstellation;
    private RelativeLayout selectSchool;
    private RelativeLayout selectMajor;
    // 定义记录用户所选日期的变量
    private int year;
    private int month;
    private int day;
    private String travelDate = null;
    // 记录用户所选性别的变量
    private int sexRBId;
    private String sex;
    private Integer sexCode;

    // 记录用户所选入学年份的变量
    private String enrollYear;
    // 记录用户给自己起的名字的变量
    private String enrollName;
    // 记录用户所选星座的变量
    private String constellation;
    //要上传的头像
    Bitmap photo;
    //drawable（头像用）
    Drawable drawable;
    // 定义记录用户更换头像时的时间
    private int h;
    private int m;
    private int s;
    private String alterAvatar = null;
    // 所选学校的ID
    private int selectCollegeId;
    private String collegeIdStr;
    MajorAdapter adapter;
    /*  所选学校ID  */
    int collegeId;
    private ArrayAdapter<String> ada;
    /*  学校名称列表  */
    private List<String> collegeNameList = new ArrayList<String>();
    // 创建专业数据列表
    private ArrayList<String> majorNameList = new ArrayList<String>();
    private ArrayList<String> majorIdList = new ArrayList<String>();
    private RelativeLayout rlTitle;
    // 记录用户所选专业的变量
    private String majorName;
    private String majorId;
    // 记录用户所选学校的变量
    private String collegeName;
    //
    private View parentView;

    private String userId;
    //我的评价按钮
    private RelativeLayout myComment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        //  获得各控件
        findView();
        //  为各按钮设置监听器
        setListener();
        //通过token获取个人信息
        getInfo();

        mContext = PersonInfoActivity.this;
        initViews();
    }
    private void findView(){
        btnBack = (ImageButton)findViewById(R.id.back);
        tvName = (TextView)findViewById(R.id.value_name);
        tvSex = (TextView)findViewById(R.id.value_sex);
        tvBirthday = (TextView)findViewById(R.id.value_birthday);
        tvSchool = (TextView)findViewById(R.id.value_school);
        tvYear = (TextView)findViewById(R.id.value_year);
        tvCollstellation = (TextView)findViewById(R.id.value_constellation);
        tvStars=(TextView)findViewById(R.id.stars);
        tvPoint=(TextView)findViewById(R.id.point);
        tvMajor=(TextView)findViewById(R.id.value_major);
        selectSex = (RelativeLayout)findViewById(R.id.sexClick);
        selectDate = (RelativeLayout)findViewById(R.id.dateClick);
        selectYear = (RelativeLayout)findViewById(R.id.yearClick);
        selectName = (RelativeLayout)findViewById(R.id.nameClick);
        selectConstellation = (RelativeLayout)findViewById(R.id.constellationClick);
        selectSchool = (RelativeLayout)findViewById(R.id.schoolClick);
        selectMajor = (RelativeLayout)findViewById(R.id.majorClick);
        avatarImg = (CircleImageView) findViewById(R.id.avatarImg);
        parentView = (View)findViewById(R.id.personinfo);
        myComment = (RelativeLayout)findViewById(R.id.my_comment);
    }
    private void setListener(){
        Listener listener = new Listener();
        btnBack.setOnClickListener(listener);
        selectSex.setOnClickListener(listener);
        selectDate.setOnClickListener(listener);
        selectYear.setOnClickListener(listener);
        selectName.setOnClickListener(listener);
        selectConstellation.setOnClickListener(listener);
        selectSchool.setOnClickListener(listener);
        selectMajor.setOnClickListener(listener);
        myComment.setOnClickListener(listener);
    }

    private void getInfo(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = HOST+API_ACCOUNT_PROFILE;//"http://10.7.82.168:8080/api/account/profile/getProfile";
        RequestParams param = new RequestParams();
        userId = getIntent().getStringExtra("userId");
        if (userId == null){
            param.put("token",AppUtils.getToken(PersonInfoActivity.this));
        }else {
            param.put("userId", userId);
            url = HOST + API_ACCOUNT_USER_PROFILE;
        }

        Log.e("url",url);
        client.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    Log.e("response",response.toString());
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    String userName = isNull(profile1.getString("userName"));
                    String birthday = isNull(profile1.getString("birthday"));
                    String schoolName = isNull(profile1.getString("schoolName"));
                    String enrollment = isNull(profile1.getString("enrollment"));
                    String constellation = isNull(profile1.getString("constellation"));
                    String stars = isNull(profile1.getString("stars"));
                    String point = isNull(profile1.getString("point"));
                    String majorName = isNull(profile1.getString("majorName"));


                    tvName.setText(userName);
                    String sex = isNull(profile1.getString("sex"));

                    if(sex.equals("0")){
                        tvSex.setText("男");
                    }else if (sex.equals("1")){
                        tvSex.setText("女");
                    }else {
                        tvSex.setText(sex);
                    }
                    tvBirthday.setText(birthday);
                    tvSchool.setText(schoolName);
                    tvYear.setText(enrollment);
                    tvCollstellation.setText(constellation);
                    tvStars.setText(stars);
                    tvPoint.setText("积分|"+point);
                    tvMajor.setText(majorName);
                    Log.i("profileImage",profile1.getString("profileImage"));
                    Glide.with(PersonInfoActivity.this).load(AppUtils.HOST+profile1.getString("profileImage")).into(avatarImg);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String isNull(String m) {
        if (m == null || m.equals("null") || m.isEmpty()){
            return "未填写";
        }else {
            return m;
        }
    }

    private class Listener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.sexClick:
                    setSexDialog();
                    break;
                case R.id.dateClick:
                    setDateDialog();
                    break;
                case R.id.yearClick:
                    setEnrollYearDialog();
                    break;
                case R.id.nameClick:
                    setNameDialog();
                    break;
                case R.id.constellationClick:
                    setConstellation();
                    break;
                case R.id.schoolClick:
                    searchCollege();
                    break;
                case R.id.majorClick:
                    setMajorDialog();
                    break;
                case R.id.my_comment:
                    Intent intent_comment = new Intent(PersonInfoActivity.this,CommentListActivity.class);
                    if (userId == null){
                        intent_comment.putExtra("token",AppUtils.getToken(getApplicationContext()));
                    }else {
                        intent_comment.putExtra("userId",userId);
                    }

                    startActivity(intent_comment);
                    break;
            }
        }
    }
    /**
     * 根据用户输入的关键词获取大学列表
     */
    private void searchCollege(){
//        Toast.makeText(PersonInfoActivity.this,"点击了切换学校",Toast.LENGTH_SHORT).show();
        // 创建对话框 Builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_school,null);
        builder.setView(dialogLayout);
        /*  搜索栏 */
        final AutoCompleteTextView etSearch=(AutoCompleteTextView)dialogLayout.findViewById(R.id.info_et_search);;
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 根据关键词获取学校下拉列表
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST + ApiUtils.API_COLLEGE_NAMELIST;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("keyWord",etSearch.getText());
                // 发送网络请求
                client.post(url, param, new JsonHttpResponseHandler() {
                    // 发出网络请求前绑定adapter
                    @Override
                    public void onStart() {
                        super.onStart();
                        ada = new ArrayAdapter<>(
                                getApplicationContext(),
                                R.layout.college_dropdown_item,
                                collegeNameList);
                        etSearch.setAdapter(ada);
                        ada.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, JSONObject response) {
                        Log.e("pSUCCESS","发送成功!");
                        // response为返回的JSON对象
                        Log.e("pResponse:", response.toString());

                        JSONObject collegeNameListJO = null;
                        try {
                            // 获取JSONObject
                            collegeNameListJO = new JSONObject(response.toString());
                            // 获取JSONArray
                            JSONArray collegeNameListJA = collegeNameListJO.getJSONArray("datum");
                            // 给学校名列表赋值
                            for (int j=0; j<collegeNameListJA.length();j++){
                                collegeNameList.add(j,collegeNameListJA.getJSONObject(j).getString("name"));
                            }
                            builder.setAdapter(adapter,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    collegeName = collegeNameList.get(i).toString();
                                    Toast.makeText(PersonInfoActivity.this,collegeName+"",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 更新列表
                        ada.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable editable) {
                collegeName = etSearch.getText().toString();
                // 清除上次请求的学校
                collegeNameList.clear();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取最终学校名称
                tvSchool.setText(etSearch.getText().toString());
                Toast.makeText(PersonInfoActivity.this,"学校修改成功",Toast.LENGTH_SHORT).show();
            }
        });
        // 创建并显示对话框
        builder.create();
        builder.show();
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据学校名称，获得所选学校ID
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST + ApiUtils.API_COLLEGE_LALO_SCENE;
                // 请求参数：学校名称
                RequestParams param = new RequestParams();
                param.put("collegeName",collegeName);
                //发送请求
                client.post(url, param, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONObject collegeNameJO = null;
                        try {
                            // 获取JSONObject
                            collegeNameJO = new JSONObject(response.toString());
                            // 获取JSONArray
                            JSONArray collegeNameJA = collegeNameJO.getJSONArray("datum");
                            // 给所选学校经纬度和ID赋值
                            collegeId = collegeNameJA.getJSONObject(0).getInt("coid");
                            selectCollegeId = collegeId;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    /**
     * 设置专业对话框
     */
    private void setMajorDialog(){
        collegeIdStr = selectCollegeId+"";
        // 根据学校ID，发送网络请求，获得专业列表
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_COLLEGE_MARJOR;
        // 请求参数：关键词
        RequestParams param = new RequestParams();
        param.put("collegeId",selectCollegeId);
        // 发送网络请求
        client.post(url, param, new JsonHttpResponseHandler() {
            // 发出网络请求前绑定adapter
            @Override
            public void onStart() {
                super.onStart();
                adapter = new MajorAdapter(PersonInfoActivity.this,majorNameList);
                adapter.notifyDataSetChanged();
            }

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
                    // 给专业名列表赋值
                    for (int j=0; j<majorNameListJA.length();j++){
                        majorNameList.add(j,majorNameListJA.getJSONObject(j).getString("name"));
                        majorIdList.add(j,majorNameListJA.getJSONObject(j).getString("scid"));
                    }
                    Log.e("MAJOR", majorNameList.toString());
                    Log.e("MAJORID", majorIdList.toString());

                    // 创建对话框 Builder
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
                    // 加载对话框布局
                    final View dialogLayout =
                            getLayoutInflater().inflate(R.layout.dialog_major,null);
                    builder.setView(dialogLayout);
                    Log.e("acount", adapter.getCount()+"");
                    Log.e("aitem", adapter.getItem(1).toString());
                    builder.setAdapter(adapter,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            majorName = adapter.getItem(i).toString();
                            majorId = majorIdList.get(i);
                            tvMajor.setText(majorName);
                            AsyncHttpClient client = new AsyncHttpClient();
                            String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                            RequestParams param = new RequestParams();
                            param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                            param.put("majorId",majorId);
                            client.post(url, param, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Toast.makeText(PersonInfoActivity.this,"成功修改专业",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    // 创建并显示对话框
                    builder.create();
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 设置星座对话框
     */
    private void setConstellation(){
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_constellation,null);
        builder.setView(dialogLayout);
        // 创建 adapter
        final ConstellationAdapter adapter = new ConstellationAdapter(PersonInfoActivity.this);
        // 设置 adapter 和 监听器
        builder.setAdapter(adapter,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                constellation = adapter.getItem(i).toString();
                tvCollstellation.setText(constellation);
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                param.put("constellation",constellation);
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(PersonInfoActivity.this,"成功修改了星座",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    private void setNameDialog() {
        // 创建对话框 Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);

        // 加载对话框布局
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_enroll_name,null);
        builder.setView(dialogLayout);

        // 获得输入框
        final EditText etName = (EditText)dialogLayout.findViewById(R.id.dialog_enrollment_year_et);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enrollName = etName.getText().toString();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvName.setText(enrollName);
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                param.put("userName",enrollName);
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(PersonInfoActivity.this,"成功修改昵称咯！",Toast.LENGTH_SHORT).show();
                    }
                });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);

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
        // builder.setTitle("选择入学年份");

        // 创建 adapter
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvYear.setText(enrollYear);
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                param.put("enrollment",enrollYear);
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(PersonInfoActivity.this,"成功修改了入学年份",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }

    private void setDateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_date,null);
        builder.setView(dialogLayout);
        DatePicker datePicker = (DatePicker)dialogLayout.findViewById(R.id.dialog_date_datePicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                PersonInfoActivity.this.year = year;
                PersonInfoActivity.this.month = (month+1);
                PersonInfoActivity.this.day = day;
            }
        });
        // 点击确定按钮时更新用户选择的日期
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                travelDate = year+"-"+month+"-"+day;
                tvBirthday.setText(travelDate);
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                param.put("birthday",travelDate);
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(PersonInfoActivity.this,"成功修改了生日",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 创建并显示对话框
        builder.create();
        builder.show();
    }
    private void setSexDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        final View dialogLayout =
                getLayoutInflater().inflate(R.layout.dialog_sex,null);
        builder.setView(dialogLayout);
        RadioGroup sexRG = (RadioGroup)dialogLayout.findViewById(R.id.dialog_sex_rg);
        sexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexRBId = radioGroup.getCheckedRadioButtonId();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                RadioButton checkedRB = (RadioButton)dialogLayout.findViewById(sexRBId);
                sex = checkedRB.getText().toString();
                tvSex.setText(sex);
                if (sex.equals("男")){
                    sexCode = 0;
                }
                else {
                    sexCode = 1;
                }
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                param.put("sex",sexCode);
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(PersonInfoActivity.this,"成功修改了性别！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        // 创建并显示对话框
        builder.create();
        builder.show();
    }
    private void initViews() {
        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(mContext,itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.personinfo),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING://　保存裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(null, photo);
            Calendar calendar = Calendar.getInstance();
            h = calendar.get(Calendar.HOUR);
            m = calendar.get(Calendar.MINUTE);
            s = calendar.get(Calendar.SECOND);
            alterAvatar = h+"-"+m+"-"+s;
            Log.i("alterAvatar",alterAvatar);
            //调用saveFile的方法获取裁剪出的图片的本地路径
            urlpath = FileUtil.saveFile(mContext,alterAvatar+".jpg",photo);
            Log.i("裁剪出的图片的本地路径",urlpath);
            //给头像空间set新裁剪出的图片
            avatarImg.setImageDrawable(drawable);
            imgUrl = AppUtils.HOST+ApiUtils.API_ACCOUNT_LOAD+"?token="+getToken(PersonInfoActivity.this);
            //创建网络请求——上传头像
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            File file = new File(urlpath);
            try {
                //放入文件
                params.put("profile_picture", file);
                Log.i("文件存在",urlpath);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("文件不存在----------");
            }
            //执行post请求
            client.post(imgUrl,params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject responseBody) {
                    if (statusCode == 200) {
                        Toast.makeText(getApplicationContext(), "头像上传成功", Toast.LENGTH_SHORT)
                                .show();
                        try {
                            Log.i("responBody",responseBody.toString());
                            JSONObject avatarUrl1 = responseBody.getJSONObject("datum");
                            Log.i("profile_picture",avatarUrl1.getString("profile_picture"));
                            //修改个人头像的url
                            AsyncHttpClient client = new AsyncHttpClient();
                            String url = HOST+API_ACCOUNT_PROFILE;//"http://10.7.82.168:8080/api/account/profile/getProfile";
                            RequestParams param = new RequestParams();
                            Log.i("profileImage",avatarUrl1.getString("profile_picture"));
                            param.put("token",AppUtils.getToken(PersonInfoActivity.this));
                            param.put("profileImage",avatarUrl1.getString("profile_picture"));
                            client.post(url,param, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode,Header[] headers, JSONObject response){
//                                    Toast.makeText(PersonInfoActivity.this,"已修改头像至数据库",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
