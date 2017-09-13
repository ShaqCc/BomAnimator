package com.bayin.boom.animal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.bayin.boom.R;

/**
 * Created by Administrator on 2017/9/11.
 */

public class BallTextView extends android.support.v7.widget.AppCompatTextView {
    private int[] arrBg = {R.mipmap.blue_litter,R.mipmap.green_little,R.mipmap.red_little};

    public BallTextView(Context context) {
        this(context,null);
    }

    public BallTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(Color.WHITE);
        setTextSize(40);
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
