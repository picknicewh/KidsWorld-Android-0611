package net.hunme.kidsworld.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.util.G;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：整个app初始化配置信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HunmeApplication extends Application {
    private static HunmeApplication instance;
    public static HunmeApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseLibrary.initializer(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        RongIM.init(this);//初始化消息中的信息
    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        G.clearWebViewCache(this);
        super.onTerminate();
    }
}
