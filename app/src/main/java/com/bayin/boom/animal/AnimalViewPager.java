package com.bayin.boom.animal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.bayin.boom.R;

import java.lang.reflect.Field;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class AnimalViewPager extends ViewPager {

    private static final int START = 1;
    private static final int STOP = 0;
    private static final int SET_RESULT = 666;
    private int currentPosition = 0;
    private final int[] fDrawableRes = {R.mipmap.shuxaing_shu, R.mipmap.shuxaing_niu,
            R.mipmap.shuxaing_hu, R.mipmap.shuxaing_tu,
            R.mipmap.shuxaing_long, R.mipmap.shuxaing_she,
            R.mipmap.shuxaing_ma, R.mipmap.shuxaing_yang,
            R.mipmap.shuxaing_hou, R.mipmap.shuxaing_ji,
            R.mipmap.shuxaing_dog, R.mipmap.shuxaing_zhu};
    private int[] drawableRes = {R.mipmap.shuxaing_shu, R.mipmap.shuxaing_niu,
            R.mipmap.shuxaing_hu, R.mipmap.shuxaing_tu,
            R.mipmap.shuxaing_long, R.mipmap.shuxaing_she,
            R.mipmap.shuxaing_ma, R.mipmap.shuxaing_yang,
            R.mipmap.shuxaing_hou, R.mipmap.shuxaing_ji,
            R.mipmap.shuxaing_dog, R.mipmap.shuxaing_zhu};
    private int mWidth;
    private int mHeight;
    private boolean isSetResult = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START:
                    if (!shouldStop) {
                        setCurrentItem(++currentPosition, true);
                        mHandler.sendEmptyMessageDelayed(START, 200);
                    } else return;
                    break;
                case STOP:

                    break;
                case SET_RESULT:
                    isSetResult = true;
                    currentPosition = currentPosition+2;
                    Log.w("xxx", "currentPosition=" + currentPosition + "   getCurrentItem=" + getCurrentItem());
                    drawableRes[currentPosition% 12] = fDrawableRes[mEndAnimal];
                    setCurrentItem(currentPosition,false);
                    break;
            }
        }
    };
    private MyAdapter adapter;


    public AnimalViewPager(Context context) {
        this(context, null);
    }

    public AnimalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取宽高
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.shuxaing_dog);
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        adapter = new MyAdapter();
        setAdapter(adapter);
        changeViewPageScroller();
        setOffscreenPageLimit(0);
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
        if (mEndAnimal >= 0) {
            mHandler.sendEmptyMessage(SET_RESULT);
        }
    }

//    @Override
//    public void onPageScrollStateChanged(int state) {
////        if (state == ViewPager.SCROLL_STATE_IDLE) {
//            if (currentPosition == getAdapter().getCount() - 1) {
//                setCurrentItem(0, false);
//            } else if (currentPosition == 0) {
//                setCurrentItem(getAdapter().getCount() - 2, false);
//            }
////        }
//    }


//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        super.onPageScrolled(position,positionOffset,positionOffsetPixels);
//    }

//    @Override
//    public void onPageSelected(int position) {
//        currentPosition = position;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }


    private class MyAdapter extends PagerAdapter {

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
            LinearLayout inflate = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_imageview, container, false);
            ImageView image = (ImageView) inflate.findViewById(R.id.imageview);
            if (isSetResult)
                Log.w("xxx", "instantiateItem position：" + position);
            image.setImageResource(drawableRes[position % drawableRes.length]);
            container.addView(inflate);
            return inflate;
        }
    }

    private void changeViewPageScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller scroller;
            scroller = new FixedSpeedScroller(getContext(), new LinearInterpolator());
            mField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class FixedSpeedScroller extends Scroller {
        private int mDuration = 200;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }

    }

    private int mEndAnimal = -1;

    public void setEndAnimal(int index) {
//        isSetResult = true;
        mEndAnimal = index;
        Log.w("xxxx", "指定的：" + index);
    }

    public void resetResult() {
        isSetResult = false;
        mEndAnimal = -1;
        drawableRes = fDrawableRes;
    }
}
