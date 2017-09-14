package com.bayin.boom;

import android.util.Log;

import java.util.Random;

/**
 * Created by Administrator on 2017/9/11.
 */

public class RandomUtils {
    public static int getRandom(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public static int[] getRandomBall(int length, int max) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = getRandom(max);
            Log.i("RandomUtils", "getRandomBall--" + i + "   " + arr[i]);
        }

        return arr;
    }
}
