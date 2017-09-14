package com.bayin.boom.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bayin.boom.R;
import com.bayin.boom.ScreenUtils;

/****************************************
 * 功能说明:  1.直径占比 30% width
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class KeduBackgroundView extends View {

    private Bitmap mBitmapDark;
    private Bitmap mBitmapLight;
    private Paint mPaint;
    private float degreeUnit = 15f;
    private int mRadius;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mLeft;
    private int mBaseAlpha = 100;
    private int mTop;
    private int mViewWidth;

    public KeduBackgroundView(Context context) {
        this(context, null);
    }

    public KeduBackgroundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeduBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mRadius = (int) (mScreenWidth * 0.15);

        mBitmapLight = BitmapFactory.decodeResource(getResources(), R.mipmap.kedu_2x_light);
        mBitmapDark = BitmapFactory.decodeResource(getResources(), R.mipmap.kedu_2x_dark);

        mLeft = (mScreenWidth - mBitmapLight.getWidth()) / 2;
        mTop = mScreenHeight / 2 - mRadius;
        mPaint = new Paint();

        //计算view的宽度
        mViewWidth = (mBitmapLight.getWidth() + mRadius) * 2;

    }

    //    private boolean isStart = false;
    private int progress = 0;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (int i = 0; i < 24; i++) {
            mPaint.setAlpha(125);
            canvas.drawBitmap(mBitmapLight, mViewWidth / 2 - mBitmapLight.getWidth() / 2, ScreenUtils.getFormatWidth(30, mScreenWidth), mPaint);
            canvas.rotate(degreeUnit, mViewWidth / 2, mViewWidth / 2);
        }
        for (int i = 0; i < progress; i++) {
            mPaint.setAlpha(255);
            canvas.drawBitmap(mBitmapDark, mViewWidth / 2 - mBitmapLight.getWidth() / 2, ScreenUtils.getFormatWidth(30, mScreenWidth), mPaint);
            canvas.rotate(degreeUnit, mViewWidth / 2, mViewWidth / 2);
        }
        canvas.restore();
    }

    private int getAlpha(int index) {
        if (index > 18) return 255;
        return mBaseAlpha + index * 155 / 24;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewWidth);
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    public int getRadius() {
        return mRadius;
    }
}
