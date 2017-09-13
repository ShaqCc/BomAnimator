package com.bayin.boom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/13.
 ****************************************/

public class BallActivity extends Activity {

    private View mBallGreen;
    private View mBallBlue;
    private View mBallRed;
    private AnimatorSet mSet;
    private ScaleAnimation mScaleAnimation;
    private AnimatorSet mSet1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ball_layout);
        mBallRed = findViewById(R.id.ball_red);

        mBallBlue = findViewById(R.id.ball_blue);

        mBallGreen = findViewById(R.id.ball_green);

        //大小
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mBallRed, "scaleX", 1f, 1.05f);
        scaleX.setDuration(500);
        scaleX.setRepeatCount(6);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mBallRed, "scaleY", 1f, 1.05f);
        scaleY.setDuration(500);
        scaleY.setRepeatCount(6);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        mSet = new AnimatorSet();
        mSet.play(scaleX).with(scaleY);
        mSet.setInterpolator(new AccelerateDecelerateInterpolator());

        //高矮
        mScaleAnimation = new ScaleAnimation(1f, 1.05f, 1f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,1f);
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setFillAfter(true);
        mScaleAnimation.setInterpolator(new AccelerateInterpolator());
        mScaleAnimation.setRepeatCount(7);
        mScaleAnimation.setRepeatMode(Animation.REVERSE);

        //混合
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(mBallGreen, "scaleX", 1f, 1.05f);
        scaleX1.setDuration(500);
        scaleX1.setRepeatCount(6);
        scaleX1.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(mBallGreen, "scaleY", 1f, 1.05f);
        scaleY1.setDuration(500);
        scaleY1.setRepeatCount(6);
        scaleY1.setRepeatMode(ValueAnimator.REVERSE);
        mSet1 = new AnimatorSet();
        mSet1.play(scaleX1).with(scaleY1);
        mSet1.setInterpolator(new AccelerateDecelerateInterpolator());



        findViewById(R.id.bt_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start
                mSet.start();
                mBallBlue.startAnimation(mScaleAnimation);

                mSet1.start();
                mBallGreen.startAnimation(mScaleAnimation);
            }
        });
    }
}
