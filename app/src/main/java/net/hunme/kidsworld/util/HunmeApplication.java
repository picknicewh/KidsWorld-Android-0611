package net.hunme.kidsworld.util;

import android.app.Application;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.login.LoginApplication;
import net.hunme.message.MessageApplication;

import cn.jpush.android.api.JPushInterface;

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
        MessageApplication.initMessage(this);//初始化消息中的信息
        LoginApplication.initLogin(this);//初始化消息中的信息
    }
}
