package net.hunme.baselibrary.util;

import android.app.Activity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * 作者： Administrator
 * 时间： 2016/7/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MWebChromeClient  extends WebChromeClient {

    private Activity context;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;

    private  int flag;
    public MWebChromeClient(Activity context,LinearLayout ll_loading,WebView webView) {
        super();
        this.context = context;
        this.ll_loading  = ll_loading;
        if (!G.isNetworkConnected(context)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            flag = 0;
            ll_loading.setVisibility(View.GONE);
        } else {
            flag = 1;
        }
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (flag==1){
            if (newProgress == 100 ) {
                ll_loading.setVisibility(View.GONE);

            }else {
                ll_loading.setVisibility(View.VISIBLE);

            }
        }
    }

    // 处理Alert事件
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    // onReceivedTitle()方法修改网页标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        ((Activity)context).setTitle("可以用onReceivedTitle()方法修改网页标题");
        super.onReceivedTitle(view, title);
    }

    // 处理Confirm事件
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        result.confirm();
        return super.onJsConfirm(view, url, message, result);
    }

    // 处理提示事件
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
    //扩充缓存的容量
    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(spaceNeeded * 2);
    }
}

