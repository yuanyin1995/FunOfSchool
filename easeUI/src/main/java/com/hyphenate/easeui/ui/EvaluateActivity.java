package com.hyphenate.easeui.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
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

import com.hyphenate.easeui.R;

import java.util.ArrayList;
import java.util.HashMap;

public class EvaluateActivity extends Activity {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_show);
        //获得id

        //多行输入
        Edittext_input();
        //GridView设定
        GridViewsetting();
        //点击事件
        b1_Onclick();
        //设置按钮大小
        setTitlehigh();
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
//        lp.height = (height/10);
        lp.height = (height/15);
        lp.width = (7*width/10);
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
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
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
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.evaluate_show);
        //获取控件对象
        gridView = (GridView) findViewById(R.id.gridView);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.evaluate_add_photo);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.evaluate_show, new String[] { "itemImage"}, new int[] { R.id.imageView});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         */
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

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if( imageItem.size() == 4 && position ==0 ) { //第一张为默认图片
                    Toast.makeText(EvaluateActivity.this, "图片数3张已满", Toast.LENGTH_SHORT).show();
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
                    imageItem, R.layout.evaluate_show,
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
