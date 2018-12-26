package com.clubank.device;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.clubank.domain.C;
import com.clubank.util.AlarmHelper;
import com.clubank.util.CrashHandler;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    protected List<BaseActivity> activities = new ArrayList<BaseActivity>();

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());//全局异常捕获，存至/sdcard/crash/
        //  configImageLoader();
        SDKInitializer.initialize(this);// 百度地图初始化
        // 极光
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush
        C.mAlarmHelper = new AlarmHelper(this);
    }

    public void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public void exit(boolean exitApp) {
        for (Activity activity : activities) {
            try {
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (C.lc != null && C.lc.isStarted()) {
            C.lc.stop();
        }
        if (exitApp) {
            //友盟：如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess
            // (Context context)方法，用来保存统计数据。
            MobclickAgent.onKillProcess(this);

            //这种方式退出应用，会结束本应用程序的一切活动,因为本方法会根据应用程序的包名杀死所有进程包括Activity,Service,Notifications等。
            ActivityManager am = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(getApplicationContext().getPackageName());
            //android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);//等同于Runtime.getRuntime().exit(n)
        }

    }


}
