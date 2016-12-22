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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
import com.funOfSchool.adapter.ConstellationAdapter;
import com.funOfSchool.adapter.MajorAdapter;
import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;
import com.funOfSchool.util.FileUtil;
import com.funOfSchool.util.MyAsyncTask;
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

import static com.funOfSchool.ui.http.AsyncHttpClients.client;
import static com.funOfSchool.util.ApiUtils.API_ACCOUNT_PROFILE;
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
    private Context c = PersonInfoActivity.this;
    //static public String imgUrl = AppUtils.HOST+ApiUtils.API_ACCOUNT_LOAD+"?"+c;//"Http://10.141.230.100:8080/api/fs/upload?token=c5b4f1079ca24e71a25e0f3b06e5de642dNRr3";
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    static public String urlpath;	//相片储存路径
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    //个人资料的各个控件以及值
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvSchool;
    private TextView tvYear;
    private TextView tvCollstellation;
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
        selectSex = (RelativeLayout)findViewById(R.id.sexClick);
        selectDate = (RelativeLayout)findViewById(R.id.dateClick);
        selectYear = (RelativeLayout)findViewById(R.id.yearClick);
        selectName = (RelativeLayout)findViewById(R.id.nameClick);
        selectConstellation = (RelativeLayout)findViewById(R.id.constellationClick);
        selectSchool = (RelativeLayout)findViewById(R.id.schoolClick);
        selectMajor = (RelativeLayout)findViewById(R.id.majorClick);
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
    }

    private void getInfo(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = HOST+API_ACCOUNT_PROFILE;//"http://10.7.82.168:8080/api/account/profile/getProfile";
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(PersonInfoActivity.this));
        Log.e("url",url);
        client.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    Log.e("response",response.toString());
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    tvName.setText(profile1.getString("userName"));
                    int sex = profile1.getInt("sex");
                    if(sex == 0){
                        tvSex.setText("男");
                    }else{
                        tvSex.setText("女");
                    }
                    tvBirthday.setText(profile1.getString("birthday"));
                    tvSchool.setText(profile1.getString("schoolName"));
                    tvYear.setText(profile1.getString("enrollment"));
                    tvCollstellation.setText(profile1.getString("constellation"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

                    break;
                case R.id.majorClick:

                    break;
            }
        }
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

        // 设置对话框标题
        // builder.setTitle("选择星座");

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

        // 创建 adapter
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvName.setText(enrollName);
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST+ ApiUtils.API_ACCOUNT_PROFILE;
                // 请求参数：关键词
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
        avatarImg = (CircleImageView) findViewById(R.id.avatarImg);
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
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            Log.i("图片的地址！！",urlpath);
            avatarImg.setImageDrawable(drawable);

            // 新线程后台上传服务器
            MyAsyncTask asyncTask = new MyAsyncTask(PersonInfoActivity.this);
            asyncTask.execute(20);
            Log.i("!!!Token:",getToken(PersonInfoActivity.this));
        }
    }
}
