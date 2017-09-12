package com.bayin.boom.animal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bayin.boom.R;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class AnimalViewPager extends ViewPager implements ViewPager.OnPageChangeListener {

    private static final int START = 1;
    private static final int STOP = 0;
    private int currentPosition;
    private static int[] drawableRes = {R.mipmap.shuxaing_shu, R.mipmap.shuxaing_niu,
            R.mipmap.shuxaing_hu, R.mipmap.shuxaing_tu,
            R.mipmap.shuxaing_long, R.mipmap.shuxaing_she,
            R.mipmap.shuxaing_ma, R.mipmap.shuxaing_yang,
            R.mipmap.shuxaing_hou, R.mipmap.shuxaing_ji,
            R.mipmap.shuxaing_dog, R.mipmap.shuxaing_zhu};
    private int mWidth;
    private int mHeight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START:
                    if (!shouldStop) {
                        setCurrentItem(++currentPosition, true);
                        mHandler.sendEmptyMessageDelayed(START, 100);
                    } else return;
                    break;
                case STOP:

                    break;
            }
        }
    };


    public AnimalViewPager(Context context) {
        this(context, null);
    }

    public AnimalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取宽高
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.shuxaing_dog);
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        setAdapter(new MyAdapter());
    }

    private boolean shouldStop = false;

    /**
     * 开始轮播
     */
    public void startLoop() {
        shouldStop = false;
        mHandler.sendEmptyMessage(START);
    }

    public void stopLoop() {
        shouldStop = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (currentPosition == getAdapter().getCount() - 1) {
                setCurrentItem(1, false);
            } else if (currentPosition == 0) {
                setCurrentItem(getAdapter().getCount() - 2, false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private static class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = LayoutInflater.from(container.getContext()).inflate(R.layout.item_imageview, container, false);
            ImageView image = (ImageView) inflate.findViewById(R.id.imageview);
//            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) image.getLayoutParams();
//            layoutParams.setMargins(0, ScreenUtils.getStatusBarHeight(container.getContext()), 0, 0);
//            image.setLayoutParams(layoutParams);
            image.setImageResource(drawableRes[position % drawableRes.length]);
            container.addView(inflate);
            return inflate;
        }
    }
}
