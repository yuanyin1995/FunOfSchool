package com.funOfSchool.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class GuaGuaKa extends View {
    private Rect mTextBound = new Rect();
    public int randomNum;
    public String randonText;

    /**
     * 绘制线条的Paint,即用户手指绘制Path
     */
    private Paint mOutterPaint = new Paint();
    private Paint mBackPint = new Paint();
    /**
     * 记录用户绘制的Path
     */
    private Path mPath = new Path();
    /**
     * 内存中创建的Canvas
     */
    private Canvas mCanvas1;
    private Canvas mCanvas2;
    /**
     * mCanvas绘制内容在其上
     */
    private Bitmap mBitmap;
    private Bitmap mBackBitmap;
    private int mLastX;
    private int mLastY;
    private int gravity;

    public GuaGuaKa(Context context)
    {
        this(context, null);
    }

    public GuaGuaKa(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        mPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 初始化bitmap
        mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        mBackBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        mCanvas1 = new Canvas(mBitmap);
        mCanvas2 = new Canvas(mBackBitmap);
        // 初始化canvas的绘制用的画笔
        setUpOutterPaint();
        // 设置画笔宽度
        mOutterPaint.setStrokeWidth(20);
        //绘制这改成
        mCanvas1.drawColor(Color.parseColor("#c0c0c0"));
    }
    /**
     * 初始化canvas的绘制用的画笔
     */
    private void setUpOutterPaint()
    {
        testRandom();
        introduction();
        mBackPint.setTextSize(50);
        mBackPint.getTextBounds(randonText, 0, randonText.length(), mTextBound);
        mOutterPaint.setColor(Color.RED);
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // canvas.drawBitmap(mBackBitmap, 0, 0, null);
        //绘制奖项
        canvas.drawText(randonText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mBackPint);

        drawPath();
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制线条
     */
    private void drawPath()
    {
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas1.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);
                if (dx > 3 || dy > 3) {
                    mPath.lineTo(x, y);
                }
                mLastX = x;
                mLastY = y;
                break;
        }


        invalidate();
        return true;
    }
    //刮刮乐随机生成奖品操作
    //随机生成1-3的数
    private void testRandom(){
        Random random=new Random();
        randomNum = random.nextInt(3) + 1;

    }
    //将随机生成的数传入textview
    private void introduction(){
        if(randomNum==1){
            randonText = "一等奖";
        }else if(randomNum== 2){
            randonText = "二等奖";
        }else {
            randonText = "三等奖";
        }
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}
