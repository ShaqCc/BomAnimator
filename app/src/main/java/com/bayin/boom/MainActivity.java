package com.bayin.boom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bayin.boom.animal.AnimalViewPager;
import com.bayin.boom.animal.BallTextView;
import com.bayin.boom.animal.LeftAnimalPanView;
import com.bayin.boom.animal.RightAnimalPanView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BREAK = -1;//handler 终止跑马灯tag，开大门
    private static final int TEXTBALL = 2;//开奖球开始弹


    private View imgLeftDoor;
    private View imgRightDoor;
    private LeftAnimalPanView leftPan;
    private RightAnimalPanView rightPan;
    private boolean isOpen = false;
    private int width;
    private BallTextView tvBallText;
    private long overtime = 2000;//跑马灯计时
    private MyCount myCount;
    private boolean marqueenEnd = false;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BREAK:
                    //终止跑马灯
                    marqueenEnd = true;
                    rightAnimator.end();
                    leftAnimator.end();
                    myCount.cancel();
                    myCount = null;
                    //终止轮播
                    mAnimalPager.stopLoop();
                    //开大门
                    openDoor();
                    break;
                case TEXTBALL:
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.05f, 1f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
                    scaleAnimation.setDuration(500);
                    scaleAnimation.setFillAfter(true);
                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                    scaleAnimation.setRepeatCount(7);
                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                    tvBallText.startAnimation(scaleAnimation);
                    break;
            }
        }
    };
    private ObjectAnimator rightAnimator;
    private ObjectAnimator leftAnimator;
    private AnimalViewPager mAnimalPager;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        //幕后球字
        tvBallText = (BallTextView) findViewById(R.id.tv_result);

        //左边的门
        imgLeftDoor = findViewById(R.id.img_left_door);
        //左边轮盘
        leftPan = (LeftAnimalPanView) findViewById(R.id.leftView);

        //右边门
        imgRightDoor = findViewById(R.id.img_right_door);
        //右边轮盘
        rightPan = (RightAnimalPanView) findViewById(R.id.rightView);
        //中间的动物
        mAnimalPager = (AnimalViewPager) findViewById(R.id.animalPager);

        //跑马灯动画
        initAnimation();


        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    //关门
                    closeDoor();
                } else {
                    myCount = new MyCount(overtime, 1000);
                    myCount.start();
                    //跑马灯
                    marqueenEnd = false;
                    rightAnimator.start();
                    mAnimalPager.startLoop();
                }
                isOpen = !isOpen;
            }
        });

        findViewById(R.id.bt_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }

    private void showPopWindow() {
        mAnimalPager.resetResult();
        if (popupWindow==null){
            initPop();
        }
        if (!popupWindow.isShowing()){
            popupWindow.showAtLocation(mAnimalPager, Gravity.CENTER,0,0);
        }
    }

    private void initPop() {
        popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_set_animal_result, null, false);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(inflate);
        inflate.findViewById(R.id.iv_shu).setOnClickListener(this);
        inflate.findViewById(R.id.iv_niu).setOnClickListener(this);
        inflate.findViewById(R.id.iv_hu).setOnClickListener(this);
        inflate.findViewById(R.id.iv_tu).setOnClickListener(this);
        inflate.findViewById(R.id.iv_long).setOnClickListener(this);
        inflate.findViewById(R.id.iv_she).setOnClickListener(this);
        inflate.findViewById(R.id.iv_ma).setOnClickListener(this);
        inflate.findViewById(R.id.iv_yang).setOnClickListener(this);
        inflate.findViewById(R.id.iv_hou).setOnClickListener(this);
        inflate.findViewById(R.id.iv_ji).setOnClickListener(this);
        inflate.findViewById(R.id.iv_gou).setOnClickListener(this);
        inflate.findViewById(R.id.iv_zhu).setOnClickListener(this);
    }

    private void initAnimation() {
        rightAnimator = ObjectAnimator.ofInt(rightPan, "index", 0, 6).setDuration(600);
        rightAnimator.setInterpolator(new LinearInterpolator());
        leftAnimator = ObjectAnimator.ofInt(leftPan, "index", 5, -1).setDuration(600);
        leftAnimator.setInterpolator(new LinearInterpolator());
        rightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!marqueenEnd) {
                    rightPan.stop();
                    leftAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        leftAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!marqueenEnd) {
                    leftPan.stop();
                    rightAnimator.start();
                }
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
     * 开门
     */
    private void openDoor() {
        imgLeftDoor.animate().translationX(-width / 2).setDuration(2000);
        leftPan.animate().translationX(-width / 2).setDuration(2000);
        mAnimalPager.animate().translationX(-width / 2).setDuration(2000);
        imgRightDoor.animate().translationX(width / 2).setDuration(2000);
        rightPan.animate().translationX(width / 2).setDuration(2000);
        tvBallText.setBallText(RandomUtils.getRandom(100));
        mHandler.sendEmptyMessageDelayed(TEXTBALL, 700);
    }

    private void closeDoor() {
        //关门
        imgLeftDoor.animate().translationX(0).setDuration(200);
        leftPan.animate().translationX(0).setDuration(200);
        mAnimalPager.animate().translationX(0).setDuration(200);
        imgRightDoor.animate().translationX(0).setDuration(200);
        rightPan.animate().translationX(0).setDuration(200);
//        mAnimalPager.resetResult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shu:
                mAnimalPager.setEndAnimal(0);
                break;
            case R.id.iv_niu:
                mAnimalPager.setEndAnimal(1);
                break;
            case R.id.iv_hu:
                mAnimalPager.setEndAnimal(2);
                break;
            case R.id.iv_tu:
                mAnimalPager.setEndAnimal(3);
                break;
            case R.id.iv_long:
                mAnimalPager.setEndAnimal(4);
                break;
            case R.id.iv_she:
                mAnimalPager.setEndAnimal(5);
                break;
            case R.id.iv_ma:
                mAnimalPager.setEndAnimal(6);
                break;
            case R.id.iv_yang:
                mAnimalPager.setEndAnimal(7);
                break;
            case R.id.iv_hou:
                mAnimalPager.setEndAnimal(8);
                break;
            case R.id.iv_ji:
                mAnimalPager.setEndAnimal(9);
                break;
            case R.id.iv_gou:
                mAnimalPager.setEndAnimal(10);
                break;
            case R.id.iv_zhu:
                mAnimalPager.setEndAnimal(11);
                break;
        }
        closePop();
    }

    private void closePop() {
        if (popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    private class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            mHandler.sendEmptyMessage(BREAK);
        }
    }
}
