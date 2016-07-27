package net.hunme.login;

import android.app.ActivityManager;
import android.content.Context;

import io.rong.imkit.RongIM;

/**
 * 作者： Administrator
 * 时间： 2016/7/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LoginApplication {
    public  static  void  initLogin(Context context){
        RongIM.init(context);
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
