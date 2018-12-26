package com.clubank.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clubank.dining.BuildConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by long on 17-6-9.
 */

public class MyDebugView extends TextView {


    public interface DebugLongPress{
        void debugLongPress(View v);
    }
    private DebugLongPress debugLongPress;

    public void setDebugLongPress(DebugLongPress debugLongPress) {
        this.debugLongPress = debugLongPress;
    }

    public MyDebugView(Context context) {
        super(context);
        this.setClickable(true);
    }

    public MyDebugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);

    }

    public MyDebugView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyDebugView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setClickable(true);
    }


    private long eventTime = 0;
    private long downTime = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = 0;
                eventTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_CANCEL:
                downTime = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                downTime = 0;
                break;
            case MotionEvent.ACTION_UP:
                downTime = System.currentTimeMillis();
                if (BuildConfig.DEBUG){
                    Log.d("-----", "onTouchEvent: "+(downTime-eventTime));
                }
                if ((downTime - eventTime)>20*1000){
                    if (debugLongPress != null){
                        debugLongPress.debugLongPress(this);
                        downTime = 0;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
