package net.hunme.message;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.lzy.okhttputils.OkHttpUtils;

import io.rong.imkit.RongIM;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        OkHttpUtils.init(this);
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
}
