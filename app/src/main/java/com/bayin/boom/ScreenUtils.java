package com.bayin.boom;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class ScreenUtils {

    public static int getScreenHeight(Context context){
        WindowManager wg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wg.getDefaultDisplay().getHeight();
    }

    public static int getScreenWidth(Context context){
        WindowManager wg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wg.getDefaultDisplay().getWidth();
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("WangJ", "状态栏-方法1:" + statusBarHeight1);
        return statusBarHeight1;
    }
}
