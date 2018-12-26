package com.clubank.util;

/**
 * Created by zengyanhua on 2017/1/9.
 * 避免重复点击提交
 */

public class SingleClick {
    private static final int DEFAULT_TIME = 2000;
    private static long lastTime;
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    public static boolean isSingle(){
        boolean isSingle ;
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime <= DEFAULT_TIME){
            isSingle = true;
        }else{
            isSingle = false;
        }
        lastTime = currentTime;

        return isSingle;
    }
}
