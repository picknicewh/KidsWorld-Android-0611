package net.hongzhang.discovery.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.discovery.LockActivity;

public class LockScreenService extends Service {
    private final static String TAG = "LockScreenService";
    private Intent lockIntent;
    private KeyguardManager keyguardManager = null;
    private KeyguardManager.KeyguardLock keyguardLock = null;
    public static boolean isUnLock = true;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lockIntent = new Intent(LockScreenService.this, LockActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        registerReceiver();
        Log.i("wwwwww", "===============onCreate=================");
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        IntentFilter mScreenFilter = new IntentFilter();
        mScreenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenFilter.addAction(Intent.ACTION_SCREEN_ON);
        LockScreenService.this.registerReceiver(mScreenOffReceiver, mScreenFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("wwwwww", "===============onDestroy=================");
        LockScreenService.this.unregisterReceiver(mScreenOffReceiver);
        //重新启动activityz
     //   startService(new Intent(LockScreenService.this, LockScreenService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 屏幕变亮的广播，这里要隐藏系统的锁屏界面
     */
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ||intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                keyguardLock = keyguardManager.newKeyguardLock("");
                keyguardLock.disableKeyguard(); //这里就是取消系统默认的锁屏
                Log.i("aaaaaa", "============取消系统默认的锁屏===============");
                //是否已经锁屏，已经锁屏了就不重新开启锁屏界面
                if (isUnLock) {
                    Log.i("aaaaaa", "============开启新的锁屏==============="+BaseLibrary.activityLockList.size());
                    BaseLibrary.removeLocFirstkActivity();
                    startActivity(lockIntent); //注意这里跳转的意图
                    isUnLock = false;
                }
            }
        }
    };

}
