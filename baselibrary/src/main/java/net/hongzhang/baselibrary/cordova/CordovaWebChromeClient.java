package net.hongzhang.baselibrary.cordova;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/8/2
 * 描    述：监听WebView的加载进度
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CordovaWebChromeClient extends SystemWebChromeClient {
    private ProgressBar pb_web;
    private View myView = null;
    private CustomViewCallback myCallback = null;
    private SystemWebView mWebView;
    private Activity activity;
    private LinearLayout ll_toolbar;
    public CordovaWebChromeClient(SystemWebViewEngine parentEngine, ProgressBar pb_web) {
        super(parentEngine);
        this.pb_web=pb_web;
    }

    public CordovaWebChromeClient(SystemWebViewEngine parentEngine, ProgressBar pb_web, SystemWebView mWebView, Activity activity, LinearLayout ll_toolbar) {
        super(parentEngine);
        this.pb_web=pb_web;
        this.mWebView=mWebView;
        this.activity=activity;
        this.ll_toolbar = ll_toolbar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100){
            pb_web.setVisibility(View.GONE);
        } else{
            pb_web.setProgress(newProgress);
            pb_web.setVisibility(View.VISIBLE);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (myCallback != null) {
            myCallback.onCustomViewHidden();
            myCallback = null ;
            return;
        }
        ViewGroup parent = (ViewGroup) mWebView.getParent();
        parent.removeView( mWebView);
        parent.addView(view);
        ll_toolbar.setVisibility(View.GONE);
        myView = view;
        myCallback = callback;
        //设置横屏
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        //设置全屏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    @Override
    public void onHideCustomView() {
        if (myView != null) {
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null ;
            }

            ViewGroup parent = (ViewGroup) myView.getParent();
            parent.removeView( myView);
            parent.addView( mWebView);
            myView = null;
        }

        ll_toolbar.setVisibility(View.VISIBLE);
        // 设置竖屏
       activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 取消全屏
        final WindowManager.LayoutParams attrs =activity.getWindow()
                .getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
