package com.bayin.boom.bomb;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bayin.boom.R;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class TimeView extends FrameLayout {

    private View mRootview;
    private ImageView mIvNumber;
    private KeduBackgroundView mKeduview;

    public TimeView(Context context) {
        this(context,null);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootview = LayoutInflater.from(context).inflate(R.layout.include_timecount_layout, this, true);
        mKeduview = (KeduBackgroundView) mRootview.findViewById(R.id.keduview);
        mIvNumber = (ImageView) mRootview.findViewById(R.id.iv_number);
    }

    public void setKeduviewRadius(int radius){
        mKeduview.setRadius(radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mKeduview.getRadius()*2,mKeduview.getRadius()*2);
    }
}
