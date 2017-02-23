package net.hongzhang.bbhow.util;

import android.app.Application;
import android.util.Log;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.util.G;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.model.Conversation;

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

        RongIM.init(this,"x18ywvqfxlloc");//初始化消息中的信息
        setExtendProvide();
        BaseLibrary.initializer(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
//        SchoolLib.initializer();
    }

    //回话扩展功能自定义
    private void setExtendProvide(){
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
    }

    @Override
    public void onTerminate() {
        Log.i("RRRRR","====================onTerminate=======================");
        G.clearWebViewCache(this);
        super.onTerminate();
    }
}
