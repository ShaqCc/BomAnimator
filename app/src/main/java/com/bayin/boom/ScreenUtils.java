package com.bayin.boom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class ScreenUtils {
    private static String TAG = "--ScreenUtils--";

    public static int getScreenHeight(Context context) {
        WindowManager wg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Point size = new Point();
//        wg.getDefaultDisplay().getSize(size);
//        return size.y;
        return wg.getDefaultDisplay().getHeight();
    }

    public static int getScreenWidth(Context context) {
        WindowManager wg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wg.getDefaultDisplay().getSize(size);
        return size.x;
    }

    /**
     * 获取导航栏高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getFormatWidth(int width, int screenWidth) {
        BigDecimal divide = BigDecimal.valueOf(screenWidth).divide(BigDecimal.valueOf(Constans.BaseScreenWidth), 10, RoundingMode.HALF_UP);
        double v = divide.doubleValue() * width;
        Log.w(TAG, "标准宽度:" + v);
        return (int) v;
    }

    public static int getFormatHeight(int height, int screenHeight) {
        BigDecimal divide = BigDecimal.valueOf(screenHeight).divide(BigDecimal.valueOf(Constans.BaseScreenHeight), 10, RoundingMode.HALF_UP);
        double v = divide.doubleValue() * height;
        Log.w(TAG, "标准宽度:" + v);

        return (int) v;
    }


    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {

//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = false;
        boolean hasBackKey = false;
        try {
            hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        } catch (java.lang.NoSuchMethodError e) {
        }
        try {
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        } catch (java.lang.NoSuchMethodError e) {
        }


        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int[] getScreenSize(Context context, boolean useDeviceSize) {

        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        if (!useDeviceSize) {
            size[0] = widthPixels;
            size[1] = heightPixels - getStatusBarHeight(context);

            return size;
        }

// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }
}
