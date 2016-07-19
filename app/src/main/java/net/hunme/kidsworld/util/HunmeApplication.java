package net.hunme.kidsworld.util;

import android.app.Application;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.message.MessageApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by DELL on 2016/1/19.
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
        MessageApplication.initMessage(this);//初始化消息中的信息
    }
}
