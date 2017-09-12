package com.bayin.boom.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bayin.boom.R;
import com.bayin.boom.ScreenUtils;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class BombLayout extends FrameLayout {

    private static final String TAG = "BombLayout";
    private Bitmap mBitmapBomb;
    private int mMBombTop;
    private int mBombLeft;
    private ImageView mImageBomb;
    private TimeView mTimeView;
    private int mScreenWidth;
    private int mScreenHeight;
    private int offset;//刻度的偏移量

    public BombLayout(@NonNull Context context) {
        this(context, null);
    }

    public BombLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BombLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        //屏幕尺寸
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        mBitmapBomb = BitmapFactory.decodeResource(getResources(), R.mipmap.bomb,options);
        if (options.outWidth<mScreenWidth){
            options.inSampleSize = options.outWidth/mScreenWidth;
        }
        options.inJustDecodeBounds = false;
        mBitmapBomb = BitmapFactory.decodeResource(getResources(),R.mipmap.bomb,options);
        mPaint = new Paint();

        //炸弹的范围
        mMBombTop = (mScreenHeight - mBitmapBomb.getHeight()) / 2;
        if (mScreenWidth > mBitmapBomb.getWidth()) {
            mBombLeft = (mScreenWidth - mBitmapBomb.getWidth()) / 2;
        } else mBombLeft = 0;

        //设置偏移量
        offset = ScreenUtils.getFormatWidth(13, mScreenWidth);

        //添加刻度view
        mTimeView = new TimeView(context);
        addView(mTimeView);
    }

    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmapBomb, mBombLeft, mMBombTop, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    /*
        刻度盘位置：left：37.1%，top：33.3%
         */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int timeLeft = (int) (mScreenWidth * 0.35)+offset;
        int timeTop = (int) (mScreenHeight * 0.33)+offset;
//        Log.i(TAG, "时间板范围：" + timeLeft + "  " + timeTop + "   " + (timeLeft + mTimeView.getMeasuredWidth()) + "   " + (timeTop + mTimeView.getMeasuredHeight()));
        Log.i(TAG, "时间板范围：" + timeLeft + "  " + timeTop + "   " + right + "   " + bottom);
        mTimeView.layout(timeLeft, timeTop, right, timeTop +bottom);

    }
}
