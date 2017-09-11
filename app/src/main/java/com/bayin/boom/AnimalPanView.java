package com.bayin.boom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;


/****************************************
 * 功能说明:  生肖轮盘view
 *
 * Author: Created by bayin on 2017/9/11.
 ****************************************/

public class AnimalPanView extends View {
    private static final String TAG = "AnimalPanView";
    private Bitmap mBitmapLeft;
    private Bitmap mBitmapRight;
    private int screenWidth;
    private int screenHeight;
    private int mLeft;
    private int mTop;
    private int index;
    private boolean isPlaying = false;
    private List<Bitmap> mBitmapList;
    private int[] mBitmapResources = {R.mipmap.animal_font_0, R.mipmap.animal_font_1,
            R.mipmap.animal_font_2, R.mipmap.animal_font_3,
            R.mipmap.animal_font_4, R.mipmap.animal_font_5,
            R.mipmap.animal_font_6, R.mipmap.animal_font_7,
            R.mipmap.animal_font_8, R.mipmap.animal_font_9,
            R.mipmap.animal_font_10, R.mipmap.animal_font_11};
    private int mAnimalTop;
    private int mAnimalLeft;

    public AnimalPanView(Context context) {
        this(context, null);
    }

    public AnimalPanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimalPanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        mPaint = new Paint();
        mBitmapLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.lunpan_left);
        mBitmapRight = BitmapFactory.decodeResource(getResources(), R.mipmap.lunpan_right);
        init();
        //轮盘边界
        mLeft = (screenWidth - mBitmapLeft.getWidth()) / 2;
        mTop = (screenHeight - mBitmapLeft.getHeight()) / 2;
    }

    /**
     * 生肖图片
     */
    private void init() {
        if (mBitmapList == null) {
            mBitmapList = new ArrayList<>();
            for (int i = 0; i < mBitmapResources.length; i++) {
                mBitmapList.add(BitmapFactory.decodeResource(getResources(), mBitmapResources[i]));
            }
            int width = mBitmapList.get(0).getWidth();
            int height = mBitmapList.get(0).getHeight();
            mAnimalTop = mTop - height;
            mAnimalLeft = (screenWidth - width) / 2;
            Log.i(TAG, )
        }
    }

    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw 背景
        canvas.drawBitmap(mBitmapLeft, mLeft, mTop, mPaint);
        canvas.drawBitmap(mBitmapRight, mLeft, mTop, mPaint);
        //draw 文字
        drawNormalText(canvas);
//        if (isPlaying) {
//
//        } else {
//            drawNormalText(canvas);
//        }

    }

    private float degreeUnit = 30;
    private float degreeShift = degreeUnit / 2;

    private void drawNormalText(Canvas canvas) {
        mPaint.setAlpha(50);
        canvas.rotate(degreeShift, screenWidth / 2, screenHeight / 2);
        for (int i = 0; i < mBitmapList.size(); i++) {
            canvas.drawBitmap(mBitmapList.get(i), mAnimalLeft, mAnimalTop, mPaint);
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
}
