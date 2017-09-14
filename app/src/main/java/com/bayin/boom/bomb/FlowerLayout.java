package com.bayin.boom.bomb;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.bayin.boom.R;
import com.bayin.boom.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/14.
 ****************************************/

public class FlowerLayout extends FrameLayout {
    private static final int END = 444;
    private static String TAG = "FlowerLayout";
    private View mBgStar;
    private View mFlowrLight;
    private View mCenterFire;
    private AnimatorSet mAnimatorSet;
    private List<ColorBallTextView> mBallList = new ArrayList<>();
    private AnimationSet animationSet;

    public FlowerLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlowerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.test_flower_layout, this, true);
        mBgStar = inflate.findViewById(R.id.bg_star);
        mFlowrLight = inflate.findViewById(R.id.flower_light);
        mCenterFire = findViewById(R.id.center_fire);
        initAnimation();
        //开奖数字
        ColorBallTextView ball_1 = (ColorBallTextView) inflate.findViewById(R.id.ball_1);
        ColorBallTextView ball_2 = (ColorBallTextView) inflate.findViewById(R.id.ball_2);
        ColorBallTextView ball_3 = (ColorBallTextView) inflate.findViewById(R.id.ball_3);
        ColorBallTextView ball_4 = (ColorBallTextView) inflate.findViewById(R.id.ball_4);
        mBallList.add(ball_1);
        mBallList.add(ball_2);
        mBallList.add(ball_3);
        mBallList.add(ball_4);
        int[] randomNum = RandomUtils.getRandomBall(4, 10);
        for (int i = 0; i < randomNum.length; i++) {
            setScale(mBallList.get(i), 0.6f);
            mBallList.get(i).setText(String.valueOf(randomNum[i]));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 比例缩放
     *
     * @param view
     * @param scale
     */
    private void setScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    /**
     * 显示，隐藏球
     * @param visible
     */
    private void setBallVisible(boolean visible){
        if (visible){
            for (int i = 0; i < mBallList.size(); i++) {
                mBallList.get(i).setVisibility(VISIBLE);
            }
        }else {
            for (int i = 0; i < mBallList.size(); i++) {
                mBallList.get(i).setVisibility(GONE);
            }
        }
    }
    /**
     * 初始化动画
     */
    private void initAnimation() {
        mAnimatorSet = new AnimatorSet();
        //中心火花
        ObjectAnimator centerScaleX = ObjectAnimator.ofFloat(mCenterFire, "scaleX", 1f, 0.8f, 1.5f, 0.9f);
        centerScaleX.setDuration(400);
        centerScaleX.setRepeatCount(10);
        centerScaleX.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator centerScaleY = ObjectAnimator.ofFloat(mCenterFire, "scaleY", 1f, 0.8f, 1.5f, 0.9f);
        centerScaleY.setDuration(400);
        centerScaleY.setRepeatCount(10);
        centerScaleY.setRepeatMode(ValueAnimator.REVERSE);

        //背景星星
        ObjectAnimator alphaStar = ObjectAnimator.ofFloat(mBgStar, "alpha", 1f, 0.5f);
        alphaStar.setDuration(100);
        alphaStar.setRepeatCount(20);
        alphaStar.setRepeatMode(ValueAnimator.REVERSE);

        //火星四射
        ObjectAnimator lightScaleX = ObjectAnimator.ofFloat(mFlowrLight, "scaleX", 0f, 1f);
        lightScaleX.setDuration(500);
        lightScaleX.setRepeatCount(10);
        lightScaleX.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator lightScaleY = ObjectAnimator.ofFloat(mFlowrLight, "scaleY", 0f, 1f);
        lightScaleY.setDuration(500);
        lightScaleY.setRepeatCount(10);
        lightScaleY.setRepeatMode(ValueAnimator.RESTART);

        //火星alpha
        ObjectAnimator lightAlpha = ObjectAnimator.ofFloat(mFlowrLight, "alpha", 0.5f, 1f);
        lightAlpha.setDuration(1000);
        lightAlpha.setRepeatCount(6);
        lightAlpha.setRepeatMode(ValueAnimator.RESTART);

        //爆炸
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 3f, 1f, 3f,
                Animation.RELATIVE_TO_SELF, 0.25f, Animation.RELATIVE_TO_SELF, 0.15f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);

        animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);


        mAnimatorSet.play(centerScaleX).with(centerScaleY)
                .with(alphaStar).with(lightScaleX)
                .with(lightScaleY).with(lightAlpha);

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                reset();
                //开始爆炸动画
                startAnimation(animationSet);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 重置
     */
    private void reset() {
        mCenterFire.setScaleX(1f);
        mCenterFire.setScaleY(1f);
        mFlowrLight.setAlpha(1f);
        mFlowrLight.setScaleX(1f);
        mFlowrLight.setScaleY(1f);
        mBgStar.setAlpha(1f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBgStar.getMeasuredWidth(), mBgStar.getMeasuredHeight());

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case END:
                    //停止动画
                    stop();
                    break;
            }
        }
    };

    /**
     * 开始燃烧
     */
    public void startFire(int time) {
        mAnimatorSet.start();
        mHandler.sendEmptyMessageDelayed(END, time * 1000);
    }

    public void stop() {
        mAnimatorSet.end();
    }
}
