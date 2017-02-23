package net.hongzhang.baselibrary.cordova;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import java.util.concurrent.ExecutorService;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/8/2
 * 描    述：Fragment配置Cordova需要主动实现 CordovaInterface
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CordovaInterfaceImpl extends ContextWrapper implements CordovaInterface {
    CordovaInterface cordova;
    public CordovaInterfaceImpl(Context base, CordovaInterface cordova) {
        super(base);
        this.cordova=cordova;
    }

    @Override
    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        cordova.startActivityForResult(command, intent, requestCode);
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        cordova.setActivityResultCallback(plugin);
    }

    @Override
    public Activity getActivity() {
        return cordova.getActivity();
    }

    @Override
    public Object onMessage(String id, Object data) {
        return cordova.onMessage(id, data);
    }

    @Override
    public ExecutorService getThreadPool() {
        return cordova.getThreadPool();
    }
}
