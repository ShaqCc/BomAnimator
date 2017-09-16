package com.bayin.boom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bayin.boom.bomb.ColorBallTextView;
import com.bayin.boom.newbomb.RootLayout;

import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by Administrator on 2017/9/15.
 */

public class NewBombActivity extends Activity {

    private RootLayout rootlayout;
    private ExplosionField explosionField;
    private View viewFilm;
    private ObjectAnimator y_1;
    private ObjectAnimator y_2;
    private ObjectAnimator y_3;
    private ObjectAnimator y_4;
    private ObjectAnimator y_5;
    private AnimatorSet ball_1_set;
    private AnimatorSet ball_2_set;
    private AnimatorSet ball_3_set;
    private AnimatorSet ball_4_set;
    private AnimatorSet ball_5_set;
    private ObjectAnimator r_1;
    private ObjectAnimator r_2;
    private ObjectAnimator r_3;
    private ObjectAnimator r_4;
    private ObjectAnimator r_5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bomb_layout);
        rootlayout = (RootLayout) findViewById(R.id.rootlayout);
        viewFilm = findViewById(R.id.view_film);
        rootlayout.startTimeCount();
        explosionField = ExplosionField.attach2Window(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                explosionField.explode(rootlayout);
                viewFilm.setAlpha(0.7f);
                viewFilm.setVisibility(View.VISIBLE);
                viewFilm.animate().alpha(0f).setDuration(200).start();
                showPopWindow();
            }
        }, 4600);
    }


    private PopupWindow popupWindow;
    private TextView tvResult;
    private ColorBallTextView ball_5;
    private ColorBallTextView ball_4;
    private ColorBallTextView ball_3;
    private ColorBallTextView ball_2;
    private ColorBallTextView ball_1;

    private void showPopWindow() {
        if (popupWindow == null)
            initPop();
        if (!popupWindow.isShowing()) {
            //set data
            setPopData();
            //窗口变暗
            setWindowDark();
            popupWindow.showAtLocation(viewFilm, Gravity.CENTER, 0, 0);
            //小球滚动动画
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //小球滚动动画
                    ball_1.animate().rotation(-30).translationXBy(-45).setInterpolator(new BounceInterpolator()).setDuration(1500).start();
                    ball_2.animate().rotation(-10).translationXBy(-10).setInterpolator(new LinearInterpolator()).setDuration(500).start();
                    ball_3.animate().rotation(20).translationXBy(30).setInterpolator(new LinearInterpolator()).setDuration(1000).start();
                    ball_4.animate().rotation(-10).translationXBy(-15).setInterpolator(new LinearInterpolator()).setDuration(500).start();
                    ball_5.animate().rotation(20).translationXBy(30).setInterpolator(new BounceInterpolator()).setDuration(1000).start();
                    //滚动结束开始循环浮动
                    ball_1_set.playTogether(y_1, r_1);
                    ball_1_set.setStartDelay(1400);
                    ball_1_set.start();

                    ball_2_set.playTogether(y_2, r_2);
                    ball_2_set.setStartDelay(400);
                    ball_2_set.start();

                    ball_3_set.playTogether(y_3, r_3);
                    ball_3_set.setStartDelay(900);
                    ball_3_set.start();

                    ball_4_set.playTogether( y_4, r_4);
                    ball_4_set.setStartDelay(400);
                    ball_4_set.start();

                    ball_5_set.playTogether(y_5, r_5);
                    ball_5_set.setStartDelay(900);
                    ball_5_set.start();
                }
            }, 800);
        }
    }

    private void setPopData() {
        int[] arrResult = RandomUtils.getRandomBall(5, 10);
        tvResult.setText(getResult(arrResult));
        ball_1.setText(String.valueOf(arrResult[0]));
        ball_2.setText(String.valueOf(arrResult[1]));
        ball_3.setText(String.valueOf(arrResult[2]));
        ball_4.setText(String.valueOf(arrResult[3]));
        ball_5.setText(String.valueOf(arrResult[4]));
    }

    private String getResult(int[] arrResult) {
        StringBuilder sbuider = new StringBuilder();
        for (int i = 0; i < arrResult.length; i++) {
            sbuider.append(arrResult[i]).append("，");
        }
        return sbuider.replace(sbuider.length() - 1, sbuider.length(), "").toString();
    }

    private void closePop() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    private void initPop() {
        popupWindow = new PopupWindow(this);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View inflate = View.inflate(this, R.layout.boom_result_layout, null);
        tvResult = (TextView) inflate.findViewById(R.id.tv_result);
        ball_1 = (ColorBallTextView) inflate.findViewById(R.id.reslut_ball_1);
        ball_2 = (ColorBallTextView) inflate.findViewById(R.id.reslut_ball_2);
        ball_3 = (ColorBallTextView) inflate.findViewById(R.id.reslut_ball_3);
        ball_4 = (ColorBallTextView) inflate.findViewById(R.id.reslut_ball_4);
        ball_5 = (ColorBallTextView) inflate.findViewById(R.id.reslut_ball_5);
        inflate.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                finish();
            }
        });
        popupWindow.setContentView(inflate);
        //animation
        y_1 = ObjectAnimator.ofFloat(ball_1, "translationY", 0f, 10f);
        y_1.setDuration(800);
        y_1.setRepeatCount(1000);
        y_1.setRepeatMode(ValueAnimator.REVERSE);
        r_1 = ObjectAnimator.ofFloat(ball_1, "rotation", -30, -20);
        r_1.setDuration(800);
        r_1.setRepeatCount(1000);
        r_1.setRepeatMode(ValueAnimator.REVERSE);
        ball_1_set = new AnimatorSet();
        ball_1_set.setInterpolator(new AccelerateDecelerateInterpolator());
        ball_1_set.setDuration(800);


        y_2 = ObjectAnimator.ofFloat(ball_2, "translationY", 0f, 16f);
        y_2.setDuration(800);
        y_2.setRepeatCount(1000);
        y_2.setRepeatMode(ValueAnimator.REVERSE);
        r_2 = ObjectAnimator.ofFloat(ball_2, "rotation",-10, -25);
        r_2.setDuration(800);
        r_2.setRepeatCount(1000);
        r_2.setRepeatMode(ValueAnimator.REVERSE);
        ball_2_set = new AnimatorSet();
        ball_2_set.setInterpolator(new AccelerateDecelerateInterpolator());
        ball_2_set.setDuration(800);

        y_3 = ObjectAnimator.ofFloat(ball_3, "translationY", 0f, 15f);
        y_3.setDuration(800);
        y_3.setRepeatCount(1000);
        y_3.setRepeatMode(ValueAnimator.REVERSE);
        r_3 = ObjectAnimator.ofFloat(ball_3, "rotation",20, 30);
        r_3.setDuration(800);
        r_3.setRepeatCount(1000);
        r_3.setRepeatMode(ValueAnimator.REVERSE);
        ball_3_set = new AnimatorSet();
        ball_3_set.setInterpolator(new AccelerateDecelerateInterpolator());
        ball_3_set.setDuration(800);

        y_4 = ObjectAnimator.ofFloat(ball_4, "translationY", 0f, 12f);
        y_4.setDuration(800);
        y_4.setRepeatCount(1000);
        y_4.setRepeatMode(ValueAnimator.REVERSE);
        r_4 = ObjectAnimator.ofFloat(ball_4, "rotation", -10, -20);
        r_4.setDuration(800);
        r_4.setRepeatCount(1000);
        r_4.setRepeatMode(ValueAnimator.REVERSE);
        ball_4_set = new AnimatorSet();
        ball_4_set.setInterpolator(new AccelerateDecelerateInterpolator());
        ball_4_set.setDuration(800);

        y_5 = ObjectAnimator.ofFloat(ball_5, "translationY", 0f, 16f);
        y_5.setDuration(800);
        y_5.setRepeatCount(1000);
        y_5.setRepeatMode(ValueAnimator.REVERSE);
        r_5 = ObjectAnimator.ofFloat(ball_5, "rotation", 20, 45);
        r_5.setDuration(800);
        r_5.setRepeatCount(1000);
        r_5.setRepeatMode(ValueAnimator.REVERSE);
        ball_5_set = new AnimatorSet();
        ball_5_set.setInterpolator(new AccelerateDecelerateInterpolator());
        ball_5_set.setDuration(800);


    }

    private void setWindowDark() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}
