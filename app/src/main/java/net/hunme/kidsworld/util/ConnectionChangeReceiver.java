package net.hunme.kidsworld.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import net.hunme.baselibrary.util.G;
import net.hunme.kidsworld.MainActivity;

/**
 * 作者： Administrator
 * 时间： 2016/7/29
 * 名称：断网监听事件
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            MainActivity.isconnect = false;
            //断网
            MainActivity.count++;
        }else {
            //连上网络
            MainActivity.isconnect = true;
            //改变背景或者 处理网络的全局变量
        }
        Log.i("TAG",+MainActivity.count+"===============");
    }

}