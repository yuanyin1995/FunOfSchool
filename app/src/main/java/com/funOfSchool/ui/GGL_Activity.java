package com.funOfSchool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funOfSchool.R;

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
        //按钮点击事件
        Onclick();
    }
    //按钮点击事件
    private void Onclick(){
        Ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
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
}
