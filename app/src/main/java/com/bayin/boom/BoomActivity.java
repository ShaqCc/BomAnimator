package com.bayin.boom;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bayin.boom.bomb.ParentLayout;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class BoomActivity extends AppCompatActivity {

    private static final int BOOM = 1;//开始爆炸
    private ParentLayout bombLayout;
    private Animation mShakeAnim;
    private View mWhiteFilm;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BOOM:
                    mWhiteFilm.setVisibility(View.VISIBLE);
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(mWhiteFilm, "alpha", 0f, 1f, 0f);
                    alpha.setDuration(300);
                    alpha.setInterpolator(new AccelerateDecelerateInterpolator());
                    alpha.start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);

        mWhiteFilm = findViewById(R.id.white_film);

        bombLayout = (ParentLayout) findViewById(R.id.bomb_layout);
        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);

        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bombLayout.startBomb();
                bombLayout.startAnimation(mShakeAnim);
                mHandler.sendEmptyMessageDelayed(BOOM,3000);
            }
        });
    }
}
