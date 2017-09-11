package com.bayin.boom;

import java.util.Random;

/**
 * Created by Administrator on 2017/9/11.
 */

public class RandomUtils {
    public static int getRandom(int max){
        Random random = new Random();
        return random.nextInt(max);
    }
}
