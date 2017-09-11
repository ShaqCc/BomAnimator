package com.bayin.boom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/11.
 */

public class BallTextView extends android.support.v7.widget.AppCompatTextView {
    private int[] arrBg = {R.mipmap.ball_blue,R.mipmap.ball_green,R.mipmap.ball_red};

    public BallTextView(Context context) {
        this(context,null);
    }

    public BallTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(Color.WHITE);
        setTextSize(50);
        setGravity(Gravity.CENTER);
    }

    public  void setBallText(int num){
        this.setText(String.valueOf(num));
        switch (num%3){
            case 0:
                setBackgroundResource(arrBg[0]);
                break;
            case 1:
                setBackgroundResource(arrBg[1]);
                break;
            case 2:
                setBackgroundResource(arrBg[2]);
                break;
            default:
                setBackgroundResource(arrBg[0]);
                break;
        }
    }
}
