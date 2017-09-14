package com.bayin.boom.bomb;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bayin.boom.R;
import com.bayin.boom.ScreenUtils;

import tyrantgit.explosionfield.ExplosionField;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class BombLayout extends FrameLayout {

    private static final String TAG = "BombLayout";
    private static final int CHANGE = 1;//更换数字
    private static final int TIMEOVER = 0;//时间到
    private static final int COUNTTIME = 2;//倒计时开始变化
    private int overtime = 2;

    private Bitmap mBitmapBomb;
    private int mMBombTop;
    private int mBombLeft;
    private TimeView mTimeView;
    private int mScreenWidth;
    private int mScreenHeight;
    private int offset;//刻度的偏移量
    private int[] numberRes = {R.mipmap.time_01_2x, R.mipmap.time_02_2x, R.mipmap.time_03_2x};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNTTIME:
                    objAlpha.start();
                    break;
                case CHANGE:
                    overtime--;
                    mTimeView.getNumImageView().setImageResource(numberRes[overtime]);
                    if (overtime == 0){
                        sendEmptyMessageDelayed(TIMEOVER, 1000);
                    }
                    break;
                case TIMEOVER:
                    overtime = 2;
                    mTimeView.getNumImageView().setImageResource(numberRes[overtime]);

                    break;
            }
        }
    };
    private ObjectAnimator objAlpha;
    private ObjectAnimator mProgressAnimator;

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
        //获取炸弹图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bomb);
        mBitmapBomb = ThumbnailUtils.extractThumbnail(bitmap, mScreenWidth, mScreenWidth);
        mPaint = new Paint();
        //炸弹的范围
        mMBombTop = (mScreenHeight - mBitmapBomb.getHeight()) / 2;
        if (mScreenWidth > mBitmapBomb.getWidth()) {
            mBombLeft = (mScreenWidth - mBitmapBomb.getWidth()) / 2;
        } else mBombLeft = 0;

        //设置偏移量
        offset = ScreenUtils.getFormatWidth(22, mScreenWidth);
        //添加刻度view
        mTimeView = new TimeView(context);
        addView(mTimeView);
    }

    public void startTimeCount(int time) {
        overtime = time;
        if (mProgressAnimator == null || objAlpha == null)
            initAnim();
        mProgressAnimator.start();
        mHandler.sendEmptyMessageDelayed(COUNTTIME,1000);
    }

    private void initAnim() {
        mProgressAnimator = ObjectAnimator.ofInt(mTimeView.getmKeduview(), "progress", 0, 24);
        mProgressAnimator.setDuration(3000);
        //数字动画
        objAlpha = ObjectAnimator.ofFloat(mTimeView.getNumImageView(), "alpha", 1f, 0f);
        objAlpha.setDuration(1000);
        objAlpha.setRepeatCount(2);
        objAlpha.setRepeatMode(ValueAnimator.RESTART);
        objAlpha.setInterpolator(new DecelerateInterpolator());

        objAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTimeView.getNumImageView().setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mHandler.sendEmptyMessage(CHANGE);
            }
        });
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int timeLeft = (int) (mScreenWidth * 0.35);
        int timeTop = (int) (mScreenHeight * 0.33) + offset;
        Log.i(TAG, "时间板范围：" + timeLeft + "  " + timeTop + "   " + right + "   " + bottom);
        //设置倒计时位置
        mTimeView.layout(timeLeft, timeTop, right, timeTop + bottom);
        //设置烟花位置
//        int flowerLeft = ScreenUtils.getFormatWidth(100,mScreenWidth);
//        int flowerTop = ScreenUtils.getFormatHeight(150,mScreenHeight);
//        mFlowerLayout.layout(flowerLeft, flowerTop, right, bottom);
    }
}
