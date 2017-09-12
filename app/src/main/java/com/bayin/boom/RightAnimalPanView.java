package com.bayin.boom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 右边轮盘
 * <p>
 * Created by Administrator on 2017/9/11.
 */

public class RightAnimalPanView extends View {
    private static final String TAG = "RightAnimalPanView";
    private Bitmap mBitmapRight;
    private int screenWidth;
    private int screenHeight;
    private int mLeft;
    private int mTop;
    private int index = -1;
    private boolean isPlaying = false;
    private List<Bitmap> mBitmapList;
    private int[] mBitmapResources = {
            R.mipmap.animal_font_6, R.mipmap.animal_font_7,
            R.mipmap.animal_font_8, R.mipmap.animal_font_9,
            R.mipmap.animal_font_10, R.mipmap.animal_font_11};
    private int mAnimalTop;
    private int mAnimalLeft;
    private Context mCtx;

    public RightAnimalPanView(Context context) {
        this(context, null);
    }

    public RightAnimalPanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightAnimalPanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        mPaint = new Paint();
        mBitmapRight = BitmapFactory.decodeResource(getResources(), R.mipmap.lunpan_right_2x);
        //轮盘边界
        mLeft = (screenWidth - mBitmapRight.getWidth()) / 2;
        mTop = (screenHeight - mBitmapRight.getHeight()) / 2;
        init();
    }

    /**
     * 生肖图片
     */
    private void init() {
        if (mBitmapList == null) {
            mAnimalPaint = new Paint();
            mBitmapList = new ArrayList<>();
            for (int i = 0; i < mBitmapResources.length; i++) {
                mBitmapList.add(BitmapFactory.decodeResource(getResources(), mBitmapResources[i]));
            }
            int width = mBitmapList.get(0).getWidth();
            int height = mBitmapList.get(0).getHeight();
            mAnimalTop = mTop - height;
            mAnimalLeft = (screenWidth - width) / 2;
            Log.i(TAG, "生肖位置：" + mAnimalLeft + "top：" + mAnimalTop);
        }
    }

    private Paint mPaint;
    private Paint mAnimalPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(getContext());
        canvas.translate(0,-statusBarHeight/2);
        //draw 背景
        canvas.drawBitmap(mBitmapRight, mLeft, mTop, mPaint);
        //draw 文字
        drawNormalText(canvas);
        canvas.restore();
    }

    private float degreeUnit = 30;
    private float degreeOffset = degreeUnit / 2;
    private int animalOffset = 55;

    private void drawNormalText(Canvas canvas) {

        canvas.rotate(degreeOffset, screenWidth / 2, screenHeight / 2);
        for (int i = 0; i < mBitmapList.size(); i++) {
            if (i == index) {
                mAnimalPaint.setAlpha(255);
                canvas.drawBitmap(mBitmapList.get(i), mAnimalLeft, mAnimalTop, mAnimalPaint);
            } else {
                mAnimalPaint.setAlpha(125);
                canvas.drawBitmap(mBitmapList.get(i), mAnimalLeft, mAnimalTop, mAnimalPaint);
            }

            canvas.rotate(degreeUnit, screenWidth / 2, screenHeight / 2);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        invalidate();
    }

    private OnAnimationEndListener listener;

    public void setOnAnimationEndListener(OnAnimationEndListener l) {
        this.listener = l;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (index < mBitmapResources.length - 1) {
                        index++;
                        setIndex(index);
                        handler.sendEmptyMessageDelayed(1, 100);
                    } else {
                        index = -1;
                        handler.sendEmptyMessage(2);
                    }
                    break;
                case 2:
                    //开门
                    if (listener != null) {
                        stop();
                        listener.onAnimationEnd();
                    }
                    break;
            }
        }
    };

    public void startAnimation() {
        handler.sendEmptyMessage(1);
    }

    public void stop() {
        index = -1;
        invalidate();
    }
}
