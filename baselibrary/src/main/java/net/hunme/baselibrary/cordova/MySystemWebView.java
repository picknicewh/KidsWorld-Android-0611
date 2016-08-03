package net.hunme.baselibrary.cordova;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

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
    public MySystemWebView(SystemWebViewEngine parentEngine, LinearLayout ll_loading) {
        super(parentEngine);
        this.ll_loading=ll_loading;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100 )
            ll_loading.setVisibility(View.GONE);
        else
            ll_loading.setVisibility(View.VISIBLE);
    }
}
