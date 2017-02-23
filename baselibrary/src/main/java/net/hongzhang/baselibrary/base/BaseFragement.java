package net.hongzhang.baselibrary.base;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.cordova.CordovaInterfaceImpl;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.widget.LoadingDialog;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：所有Fragment父类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class BaseFragement extends Fragment implements CordovaInterface {

    protected CordovaPlugin activityResultCallback = null;
    protected boolean activityResultKeepRunning;
    protected boolean keepRunning = true;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private CordovaWebView webView;
    public LoadingDialog dialog;
    public <T extends View> T $(View layoutView, @IdRes int resId){
        return (T)layoutView.findViewById(resId);
    }
    @Override
    public void onResume() {
        super.onResume();
        G.setTranslucent(getActivity());
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        // 账号抢登监听
        if (RongIM.getInstance()!=null){
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(getActivity()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    public CordovaWebView getWebView(SystemWebView webView){
        CordovaWebView cwebView = new CordovaWebViewImpl(new SystemWebViewEngine(webView));
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(getActivity());
        cwebView.init(new CordovaInterfaceImpl(getActivity(), this), parser.getPluginEntries(), parser.getPreferences());
        this.webView=cwebView;
        return cwebView;
    }

    public Object onMessage(String id, Object data) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null!=webView&&webView.getPluginManager() != null) {
            webView.getPluginManager().onDestroy();
            webView.handleDestroy();
           // getActivity().unregisterReceiver();
        }
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }


    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        this.activityResultCallback = plugin;
    }

    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        this.activityResultCallback = command;
        this.activityResultKeepRunning = this.keepRunning;
        // If multitasking turned on, then disable it for activities that return
        // results
        if (command != null) {
            this.keepRunning = false;
        }
        // Start activity
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        CordovaPlugin callback = this.activityResultCallback;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, intent);
        }
    }
    public void showLoadingDialog() {
        if(dialog==null)
            dialog=new LoadingDialog(getContext(), R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    public void stopLoadingDialog() {
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
}
