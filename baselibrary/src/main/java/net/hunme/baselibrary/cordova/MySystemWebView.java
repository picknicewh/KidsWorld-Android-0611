package net.hunme.baselibrary.cordova;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.apache.cordova.engine.SystemWebChromeClient;
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
public class MySystemWebView extends SystemWebChromeClient {
    private LinearLayout ll_loading;
    private ProgressBar pb_web;
    public MySystemWebView(SystemWebViewEngine parentEngine, LinearLayout ll_loading) {
        super(parentEngine);
        this.ll_loading=ll_loading;
    }

    public MySystemWebView(SystemWebViewEngine parentEngine, ProgressBar pb_web) {
        super(parentEngine);
        this.pb_web=pb_web;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100){
            if(null!=pb_web){
                pb_web.setVisibility(View.GONE);
            }
            if(null!=ll_loading){
                ll_loading.setVisibility(View.GONE);
            }

        } else{
            if(null!=ll_loading){
                ll_loading.setVisibility(View.VISIBLE);
            }
            if(null!=pb_web){
                pb_web.setProgress(newProgress);
                pb_web.setVisibility(View.VISIBLE);
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
