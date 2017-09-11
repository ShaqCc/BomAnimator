package com.bayin.boom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class CenterAnimalView extends View {

    private int[] drawableRes = {R.mipmap.shuxaing_shu, R.mipmap.shuxaing_niu,
            R.mipmap.shuxaing_hu, R.mipmap.shuxaing_tu,
            R.mipmap.shuxaing_long, R.mipmap.shuxaing_she,
            R.mipmap.shuxaing_ma, R.mipmap.shuxaing_yang,
            R.mipmap.shuxaing_hou, R.mipmap.shuxaing_ji,
            R.mipmap.shuxaing_dog, R.mipmap.shuxaing_zhu};

    private List<Bitmap> bitmapList;
    private int animalWidth;
    private int animalHeight;
    private int screenWidth;
    private int screenHeight;
    private int left;
    private int top;
    private Paint paint;

    public CenterAnimalView(Context context) {
        this(context, null);
    }

    public CenterAnimalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterAnimalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wn = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wn.getDefaultDisplay().getWidth();
        screenHeight = wn.getDefaultDisplay().getHeight();
        paint = new Paint();
        initBitmap();
        left = (screenWidth - animalWidth) / 2;
        top = (screenHeight - animalHeight) / 2;
    }

    private void initBitmap() {
        if (bitmapList == null || bitmapList.size() == 0) {
            bitmapList = new ArrayList<>();
            for (int i = 0; i < drawableRes.length; i++) {
                bitmapList.add(getBitmap(drawableRes[i]));
            }
            animalWidth = bitmapList.get(0).getWidth();
            animalHeight = bitmapList.get(0).getHeight();
        }
    }

    private Bitmap getBitmap(int res) {
        return BitmapFactory.decodeResource(getResources(), res);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapList.get(0), left, top, paint);
    }
}
