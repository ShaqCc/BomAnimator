package com.bayin.boom;

import android.animation.ObjectAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bayin.boom.bomb.ColorBallTextView;
import com.bayin.boom.bomb.ParentLayout;

import tyrantgit.explosionfield.ExplosionField;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class BoomActivity extends AppCompatActivity {

    private static final int BOOM = 1;//开始爆炸
    private static final int SHAKE = 2;//开始震动
    private ParentLayout bombLayout;
    private Animation mShakeAnim;
    private View mWhiteFilm;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BOOM:

                    explosionField.explode(bombLayout);
                    rootView.startAnimation(mShakeAnim);
                    bombLayout.setVisibility(View.GONE);
                    mWhiteFilm.setVisibility(View.VISIBLE);
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(mWhiteFilm, "alpha", 0f, 1f, 0f);
                    alpha.setDuration(300);
                    alpha.setInterpolator(new AccelerateDecelerateInterpolator());
                    alpha.start();
                    //show window
                    showPopWindow();
                    break;
                case SHAKE:
//                    bombLayout.startAnimation(mShakeAnim);
                    break;
            }
        }
    };
    private PopupWindow popupWindow;
    private TextView tvResult;
    private ColorBallTextView ball_5;
    private ColorBallTextView ball_4;
    private ColorBallTextView ball_3;
    private ColorBallTextView ball_2;
    private ColorBallTextView ball_1;
    private View rootView;

    private void showPopWindow() {
        if (popupWindow == null)
            initPop();
        if (!popupWindow.isShowing()){
            //set data
            setPopData();
            popupWindow.showAtLocation(mWhiteFilm, Gravity.CENTER,0,0);
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
        return sbuider.replace(sbuider.length()-1,sbuider.length(),"").toString();
    }

    private void closePop(){
        if (popupWindow!=null && popupWindow.isShowing())
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
    }

    private ExplosionField explosionField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);

        rootView = findViewById(R.id.rootview);

        mWhiteFilm = findViewById(R.id.white_film);

        bombLayout = (ParentLayout) findViewById(R.id.bomb_layout);
        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);

        explosionField = ExplosionField.attach2Window(this);

//        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bombLayout.startBomb();
////                bombLayout.startAnimation(mShakeAnim);
//                mHandler.sendEmptyMessageDelayed(BOOM, 3000);
//            }
//        });

        bombLayout.startBomb();
//        mHandler.sendEmptyMessageDelayed(SHAKE,2500);
        mHandler.sendEmptyMessageDelayed(BOOM, 3200);
    }
}
