package net.hongzhang.discovery.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.network.ServerConfigManager;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;

public class ConsultActivity extends BaseActivity {
    private WebView wb_consult;
    /**
     * 没网络时显示
     */
    private static String url = ServerConfigManager.WEB_IP + "/consult_detail/index.html";
    private String resourceId;
    private UserMessage userMessage;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        loadView();
    }

    private void loadView() {
        wb_consult = $(R.id.wb_consult);
        progress_bar = $(R.id.progress_bar);
        userMessage = UserMessage.getInstance(this);
        resourceId = getIntent().getStringExtra("resourceId");
        url += "?tsId=" + userMessage.getTsId() + "&accountId=" + userMessage.getAccount_id() + "resourceId=" + resourceId;
        G.log("------------------------" + url);
        setWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        wb_consult.getSettings().setJavaScriptEnabled(true);
        wb_consult.getSettings().setJavaScriptEnabled(true);
        wb_consult.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wb_consult.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress_bar.setProgress(newProgress);
                if (newProgress == 100) {
                    progress_bar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
       /* wb_consult.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
               view.loadUrl("http://www.baidu.com");
                return true;
            }
        });*/
       // wb_consult.loadUrl("http://www.baidu.com");
          wb_consult.loadUrl(url);
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("资讯详情");
    }
}
