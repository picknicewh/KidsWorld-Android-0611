package net.hongzhang.baselibrary.cordova;

import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.util.UMHybrid;

import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/10/31
 * 描    述：
 * 版    本：
 * 修订历史：
 * ================================================
 */
public class CordovaWebViewClien extends SystemWebViewClient {

    public CordovaWebViewClien(SystemWebViewEngine parentEngine) {
        super(parentEngine);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (url != null) {
            MobclickAgent.onPageStart(url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String decodedURL = null;
        try {
            decodedURL = java.net.URLDecoder.decode(url, "UTF-8");
            UMHybrid.getInstance(view.getContext()).execute(decodedURL, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return true;
    }
}
