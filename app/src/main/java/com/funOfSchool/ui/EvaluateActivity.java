package com.funOfSchool.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.funOfSchool.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class EvaluateActivity extends Activity{
    private String guade_evaluate;//带领人评价
    private String school_evaluate;//学校评价
    private EditText et1;   //带领者评价输入框
    private EditText et2;   //学校评价输入框
    private Button b1;  //提交按钮
    private GridView gridView;              //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private RatingBar guade_ratingbar;//带领人本次评分星星个数
    private RatingBar school_ratingbar;//学校本次评分星星个数
    private int guade_score;//带领人评分
    private int school_score;//学校评分
    private RelativeLayout R1;
    private int height;
    private int width;
    //测试 定义 token
    private String token = "e4b5ca189b7e4c7a8dff29b1c6704c1do39dTR";
    private String portraiturl;

    //根据url加载图片
    private static final String imgUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4155302816,1201715785&fm=116&gp=0.jpg";
    public ImageView Iv ;
    private ProgressDialog mDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        Iv = (ImageView)findViewById(R.id.Iv_evaluate_portrait);

//        //测试获取头像url
        getPortrait();

        //根据url载入图片
        new ImageAsynTask().execute();

        //多行输入
        Edittext_input();
        //GridView设定
        GridViewsetting();
        //点击事件
        b1_Onclick();
        //设置按钮大小
        setTitlehigh();

    }


    //根据url加载显示图片
    private class ImageAsynTask extends AsyncTask<Void, Void, Drawable> {

        @Override
        protected Drawable doInBackground (Void... params) {
            String url = "http://img1.3lian.com/img2011/07/20/05.jpg";
            return loadImages(url);
        }

        @Override
        protected void onPostExecute (Drawable result) {
            super.onPostExecute(result);
            Iv.setImageDrawable(result);
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }

    public Drawable loadImages(String url) {
        try {
            return Drawable.createFromStream((InputStream)(new URL(url)).openStream(), "test");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //测试：获取带领人头像url
    private void getPortrait(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://10.7.88.109:8080/api/account/profile/getProfile";
        RequestParams param = new RequestParams();
        param.put("token",token);
        client.get(url, param, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    portraiturl = profile1.getString("profileImage");
                    Log.e( portraiturl, portraiturl);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    //设置按钮大小
    private void setTitlehigh(){
        R1 = (RelativeLayout) findViewById(R.id.RL_evaluate_button);
        //获取高度
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        //设置标题栏高度
        android.view.ViewGroup.LayoutParams lp = R1.getLayoutParams();
        lp.height = (height/16);
        lp.width = (2*width/3);
    }

    //提交按钮点击事件处理
    private void b1_Onclick(){
        b1=(Button)findViewById(R.id.Bt_evaluate_submit);
        //RatingBar星级评分
        guade_ratingbar = (RatingBar)findViewById(R.id.rtb_guadeRatingBar);
        school_ratingbar = (RatingBar)findViewById(R.id.rtb_schoolRatingBar);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框内容存入字符串内
                guade_evaluate = et1.getText().toString();
                school_evaluate = et2.getText().toString();
                //获取星级评分
                RatingBardeal();
                //跳转
                Intent intent = new Intent(EvaluateActivity.this,GGL_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //获取星级评价的评分
    private void RatingBardeal(){
        //获取进度
        int guade_result = guade_ratingbar.getProgress();
        int school_result = school_ratingbar.getProgress();
        //获取星级
        float guade_rating = guade_ratingbar.getRating();
        float school_rating = school_ratingbar.getRating();
        //获取每次至少需改变多少个星级
        float guade_step = guade_ratingbar.getStepSize();
        float school_step = school_ratingbar.getStepSize();
        //toast输出
//        Log.i("星级平分条","step="+guade_step+"result"+guade_result+"rating"+guade_rating);
//        Log.i("星级平分条","step="+school_step+"result"+school_result+"rating"+school_rating);
        //获取学校及带领人的评分
        guade_score = (int)guade_rating;
        school_score = (int)school_rating;
    }
    //设置Edittext为多行输入
    private void Edittext_input(){
        et1 = (EditText)findViewById(R.id.Et_evaluate_et1);
        et2 = (EditText)findViewById(R.id.Et_evaluate_et2);
        //设置EditText的显示方式为多行文本输入
        et1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et2.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        et1.setGravity(Gravity.TOP);
        et2.setGravity(Gravity.TOP);
        //改变默认的单行模式
        et1.setSingleLine(false);
        et2.setSingleLine(false);
        //水平滚动设置为False
        et1.setHorizontallyScrolling(false);
        et2.setHorizontallyScrolling(false);
    }
    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }  //end if 打开图片
    }
    //GridView设定
    protected void GridViewsetting(){
        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //获取控件对象
        gridView = (GridView) findViewById(R.id.gridView);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.evaluate_photo);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);

        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.evaluate_griditem_addpic, new String[] { "itemImage"}, new int[] { R.id.imageView});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if( imageItem.size() == 4 && position ==0 ) { //第一张为默认图片
                    Toast.makeText(EvaluateActivity.this, "图片数3张已满:)", Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为+ 0对应0张图片
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                }
                else {
                    dialog(position);
                }
            }
        });
    }
    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp= BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.evaluate_griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EvaluateActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
