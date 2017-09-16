package com.bayin.boom.newbomb;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.bayin.boom.Constans;
import com.bayin.boom.NumberUtil;
import com.bayin.boom.R;
import com.bayin.boom.ScreenUtils;
import com.bayin.boom.bomb.BombView;
import com.bayin.boom.bomb.FlowerLayout;
import com.bayin.boom.bomb.TimeView;

/**
 * 整个场景view
 * 宽高比：1  :  1.38
 * 时钟位置：left 1:2.7  top:1:2.34
 * Created by Administrator on 2017/9/15.
 */

public class RootLayout extends FrameLayout {

    private static final String TAG = "RootLayout";
    private int screenWidth;
    private int screenHeight;
    private int realHeight;
    private Paint paint;
    private Bitmap bitmapBomb;
    private TimeView timeView;
    private double clockLeft;
    private double clockTop;
    private FlowerLayout flowerLayout;
    private double fireLeft;
    private double fireTop;
    private int bombTop;
    boolean inited = false;
    private ObjectAnimator mProgressAnimator;
    private int offset = 5;
    private ObjectAnimator numberAlpha;
    private int overtime = 2;
    private int[] numberRes = {R.mipmap.time_01_2x, R.mipmap.time_02_2x, R.mipmap.time_03_2x};
    private Animation shakeAnima;

    public RootLayout(@NonNull Context context) {
        this(context, null);
    }


    public RootLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private final int START = 0;
    private final int TIME_COUNT = 1;
    private final int START_FIRE = 2;
    private final int CHANGE_NUMBER = 3;
    private static final int STOP = -999;
    private boolean isAborted = false;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START:
                    //启动
                    //1.时钟轮盘转动
                    mProgressAnimator.start();
                    //2.一秒后，开始数字变化 3-2-1
                    if (!isAborted)
                        sendEmptyMessageDelayed(TIME_COUNT, 1000);
                    break;
                case TIME_COUNT:
                    //倒计时动画
                    numberAlpha.start();
                    break;
                case CHANGE_NUMBER:
                    if (!isAborted) {
                        overtime--;
                        if (overtime >= 0)
                            timeView.getNumImageView().setImageResource(numberRes[overtime]);
                        if (overtime == 0) {
                            //开启烟花动画
                            sendEmptyMessage(START_FIRE);
                            numberAlpha.end();
                            timeView.getNumImageView().setAlpha(1f);
                        }
                    }
                    break;
                case START_FIRE:
                    //显示烟花
                    flowerLayout.setVisibility(VISIBLE);
                    flowerLayout.startFire(1500);
                    flowerLayout.animate().translationXBy(10).translationYBy(40).setDuration(1000).start();
                    //屏幕开始震动
                    startAnimation(shakeAnima);
                    break;
                case STOP:
                    //人为中断动画
                    mProgressAnimator.end();
                    numberAlpha.end();
                    flowerLayout.stop();
                    flowerLayout.setVisibility(INVISIBLE);
                    shakeAnima.reset();
                    break;
            }
        }
    };

    public RootLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        paint = new Paint();
        //屏幕参数
        int[] screenSize = ScreenUtils.getScreenSize(context, false);
        screenWidth = screenSize[0];
        screenHeight = screenSize[1];

        realHeight = (int) NumberUtil.mul(screenWidth, Constans.BASE_RATIO);
        Log.w(TAG, "width= " + screenWidth + "height=" + realHeight);

        //获取炸弹图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bomb);
        bitmapBomb = ThumbnailUtils.extractThumbnail(bitmap, screenWidth, screenWidth);
        bombTop = realHeight - bitmapBomb.getHeight();

        //添加时钟
        timeView = new TimeView(context);
        addView(timeView);
        clockLeft = NumberUtil.mul(0.35, screenWidth) - offset;
        clockTop = NumberUtil.mul(0.24, screenWidth) + bombTop - offset;
        Log.w(TAG, "时钟位置：" + clockLeft + "  " + clockTop);

        //添加火花
        flowerLayout = new FlowerLayout(context);
        addView(flowerLayout);
        fireLeft = NumberUtil.mul(0.06, screenWidth);
        fireTop = bombTop - NumberUtil.mul(realHeight, 0.13);


        //初始化动画
        initAimation();
    }

    /**
     * 初始化动画
     */
    private void initAimation() {
        //1，轮盘转动
        mProgressAnimator = ObjectAnimator.ofInt(timeView.getmKeduview(), "progress", 0, 24);
        mProgressAnimator.setDuration(3000);
        //震动动画
        shakeAnima = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        //数字动画
        numberAlpha = ObjectAnimator.ofFloat(timeView.getNumImageView(), "alpha", 1f, 0f);
        numberAlpha.setDuration(1000);
        numberAlpha.setRepeatCount(2);
        numberAlpha.setRepeatMode(ValueAnimator.RESTART);
        numberAlpha.setInterpolator(new AccelerateDecelerateInterpolator());
        numberAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mHandler.sendEmptyMessage(CHANGE_NUMBER);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画炸弹
        canvas.drawBitmap(bitmapBomb, 0, bombTop, paint);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(screenWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //摆放时钟
        timeView.layout((int) clockLeft, (int) clockTop, right, bottom);
        //摆放火花
        flowerLayout.layout((int) fireLeft, (int) fireTop, screenWidth, realHeight);

        if (!inited) {
            flowerLayout.setVisibility(INVISIBLE);
            inited = true;
        }
    }

    public void startTimeCount() {
        mHandler.sendEmptyMessage(START);
    }

    /**
     * 终止动画
     */
    public void stop() {
        isAborted = true;
        mHandler.sendEmptyMessage(STOP);
    }

}
