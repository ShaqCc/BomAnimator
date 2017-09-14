package com.bayin.boom.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.bayin.boom.R;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/14.
 ****************************************/

public class ColorBallTextView extends android.support.v7.widget.AppCompatTextView {

    private int mHeight;
    private int mWidth;

    public ColorBallTextView(Context context) {
        this(context,null);
    }

    public ColorBallTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorBallTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.mipmap.color_ball);
        mWidth = background.getWidth();
        mHeight = background.getHeight();
        setBackgroundResource(R.mipmap.color_ball);
        setTextSize(50);
        setGravity(Gravity.CENTER);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextColor(Color.parseColor("#E43130"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }
}
