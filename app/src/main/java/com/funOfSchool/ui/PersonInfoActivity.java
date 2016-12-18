package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Context;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.funOfSchool.R;
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

/**
 * Created by lenovo on 2016/12/10.
 */
public class PersonInfoActivity extends Activity {
    private String token="15dd6fad4f374a4bbda926d8d0c6d292L28uP1"; //临时测试使用
    private ImageButton btnBack; //返回按钮
    private Context mContext;
    private CircleImageView avatarImg;// 头像控件
    private SelectPicPopupWindow menuWindow; // 弹出框
    static public String imgUrl = "Http://10.7.92.87:8080/api/fs/upload?token=15dd6fad4f374a4bbda926d8d0c6d292L28uP1";
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
    private TextView tvMajor;
    private TextView tvYear;
    private TextView tvCollstellation;
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
        tvMajor = (TextView)findViewById(R.id.value_major);
        tvYear = (TextView)findViewById(R.id.value_year);
        tvCollstellation = (TextView)findViewById(R.id.value_constellation);
    }
    private void setListener(){
        PersoninfoListener personinfoListener = new PersoninfoListener();
        btnBack.setOnClickListener(personinfoListener);
    }
    private void getInfo(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://10.7.82.168:8080/api/account/profile/getProfile";
        RequestParams param = new RequestParams();
        param.put("token",token);
        client.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    //Log.e("response",response.toString());
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
                    tvMajor.setText(profile1.getString("majorName"));
                    tvYear.setText(profile1.getString("enrollment"));
                    tvCollstellation.setText(profile1.getString("constellation"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private class PersoninfoListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
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
        }
    }
}
