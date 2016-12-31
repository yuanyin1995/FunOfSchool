package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GGL_Activity extends Activity {
    private RelativeLayout RL_lottery_content;
    private RelativeLayout RL_lottery;
    private RelativeLayout RL_lottery_scratch;
    private ImageView redpacket;
    private GuaGuaKa guaguaka;
    private TextView Tv1;
    private TextView Tv2;
    private TextView Tv3;
    private ImageButton Ib1;
    public String result[];
//    public EmojiRainLayout  mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ggl);
        setid();
        //获取屏幕高度和宽度
        getscreen();
        //设置各部件高度和宽度
        setscreen();
        //设置图片旋转
        rotate();
        //设置字体加粗
        character();
        //红包雨效果
//        vallid();
        getRemainprize();
        //按钮点击事件
        Onclick();

    }

//    //红包雨
//    private void vallid(){
//        // bind view
//        mContainer = (EmojiRainLayout) findViewById(R.id.group_emoji_container);
//        // add emoji sources
//        mContainer.addEmoji(R.mipmap.p1);
//        mContainer.addEmoji(R.mipmap.p2);
//        mContainer.addEmoji(R.mipmap.p3);
//        mContainer.addEmoji(R.mipmap.p4);
//        mContainer.addEmoji(R.mipmap.p5);
//        // set emojis per flow, default 6
//        mContainer.setPer(10);
//        // set total duration in milliseconds, default 8000
//        mContainer.setDuration(7200);
//        // set average drop duration in milliseconds, default 2400
//        mContainer.setDropDuration(2400);
//        // set drop frequency in milliseconds, default 500
//        mContainer.setDropFrequency(500);
//        mContainer.startDropping();
//    }

    //按钮点击事件
    private void Onclick(){
        Ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(GGL_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setid(){
        Tv1 = (TextView)findViewById(R.id.Tv1);
        Tv2 = (TextView)findViewById(R.id.Tv2);
        Tv3 = (TextView)findViewById(R.id.Tv3);
        Ib1 = (ImageButton)findViewById(R.id.ib1_ggl_back);
        RL_lottery_content = (RelativeLayout)findViewById(R.id.RL_lottery_content);
        RL_lottery = (RelativeLayout)findViewById(R.id.RL_lottery);
        RL_lottery_scratch = (RelativeLayout)findViewById(R.id.RL_lottery_scratch) ;
        redpacket = (ImageView)findViewById(R.id.redpacket);
        guaguaka = (GuaGuaKa)findViewById(R.id.guagauka);
    }
    //获取屏幕高度和宽度
    private void getscreen(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //赋值
        GGL_Constant.displayWidth = displayMetrics.widthPixels;
        GGL_Constant.displayHeight = displayMetrics.heightPixels;
    }
    //各部件高宽度设置
    private void setscreen(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (GGL_Constant.displayWidth * 0.70f + 0.5f),
                (int) (GGL_Constant.displayHeight * 0.20f + 0.5f)
        );

        RL_lottery_content.setLayoutParams(params);
        RL_lottery.setGravity(Gravity.CENTER);

    }
    //设置图片旋转
    public void rotate(){
        redpacket.setPivotX(redpacket.getWidth()/2);
        redpacket.setPivotY(redpacket.getHeight()/2);
        redpacket.setRotation(45);
    }
    //汉字字体加粗
    public void character(){
        TextPaint tp1 = Tv1.getPaint();
        tp1.setFakeBoldText(true);
        TextPaint tp2 = Tv2.getPaint();
        tp2.setFakeBoldText(true);
        TextPaint tp3 = Tv3.getPaint();
        tp3.setFakeBoldText(true);
    }

    //刮刮乐随机生成奖品操作
    //随机生成1-3的数
    private int prizeRandom(int size){
        Random random=new Random();
        return random.nextInt(size + 1);
    }
    //解析
    private void getRemainprize(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + "api/prize/getRemainPrizeList";
        RequestParams param = new RequestParams();
        client.get(url, param, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray datum = response.getJSONArray("datum");
                    int iSize = datum.length();
                    int randomNum = prizeRandom(iSize);
                    if (randomNum > iSize){
                        guaguaka.setText("谢谢惠顾"); ;
//                        AppUtils.showShort(getApplicationContext(),"下次继续努力");
                    }else {
                        String prize = datum.getJSONObject(randomNum).getString("prizeName");
                        guaguaka.setText(prize);
//                        AppUtils.showShort(getApplicationContext(),"恭喜您获得  " + prize);
                        AsyncHttpClient client = new AsyncHttpClient();
                        String url = AppUtils.HOST + "api/prize/managePrize";
                        RequestParams param = new RequestParams();
                        param.put("token",AppUtils.getToken(getApplicationContext()));
                        param.put("remainPrizeId",datum.getJSONObject(randomNum).getString("remainPrizeId"));
                        client.get(url,param,new JsonHttpResponseHandler(){});
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent =new Intent(GGL_Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
