package com.bayin.boom.bomb;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.bayin.boom.ScreenUtils;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/14.
 ****************************************/

public class ParentLayout extends FrameLayout {

    private BombLayout mZhaDan;
    private FlowerLayout mFlowerLight;
    private int mScreenWidth;
    private int mScreenHeight;

    public ParentLayout(@NonNull Context context) {
        this(context, null);
    }

    public ParentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParentLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);

        mZhaDan = new BombLayout(context);
        addView(mZhaDan);
        mFlowerLight = new FlowerLayout(context);
        addView(mFlowerLight);
    }

    public void startBomb() {

        mZhaDan.startTimeCount(2);

        mFlowerLight.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlowerLight.setVisibility(VISIBLE);
                mFlowerLight.startFire(1);
                //捻子往下走
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.05f,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.04f);
                translateAnimation.setDuration(1000);
                translateAnimation.setFillAfter(false);
                mFlowerLight.startAnimation(translateAnimation);
            }
        }, 2000);
    }

    private boolean init = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!init) {
            mFlowerLight.setVisibility(INVISIBLE);
            init = true;
        }

        //炸弹位置
        int zhadanTop = ScreenUtils.getFormatHeight(200, mScreenHeight);
        mZhaDan.layout(0, zhadanTop, right, bottom);
        //火花位置
        int flowerLeft = ScreenUtils.getFormatWidth(100, mScreenWidth);
        int flowerTop = ScreenUtils.getFormatHeight(350, mScreenHeight);
        mFlowerLight.layout(flowerLeft, flowerTop, right, bottom);
    }
}
