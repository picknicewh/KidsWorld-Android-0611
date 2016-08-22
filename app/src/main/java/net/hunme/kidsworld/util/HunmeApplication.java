package net.hunme.kidsworld.util;

import android.app.Activity;
import android.app.Application;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.util.G;

import java.util.List;

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
        RongIM.init(this,"x4vkb1qpvvggk");//初始化消息中的信息
        BaseLibrary.initializer(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
        ConnectionListener();

    }

   public void ConnectionListener(){
      List<Activity> activities =  BaseLibrary.getActivities();
       if (RongIM.getInstance()!=null){
           for (int i = 0;i<activities.size();i++){
               RongIM.setConnectionStatusListener(new MyConnectionStatusListener(activities.get(i)));
           }
       }

   }
    @Override
    public void onTerminate() {
        G.clearWebViewCache(this);
        super.onTerminate();
    }
}
