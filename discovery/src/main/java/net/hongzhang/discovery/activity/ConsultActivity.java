package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    private static String url = ServerConfigManager.WEB_IP + "/paradise/index.html";
    private String resourceId;
    private UserMessage userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        loadView();
    }

    private void loadView() {
        wb_consult = $(R.id.wb_consult);
        userMessage = UserMessage.getInstance(this);
        resourceId = getIntent().getStringExtra("resourceId");
        //"?tsId=" + userMessage.getTsId() + "&accountId=" + userMessage.getAccount_id() +
        url += "#/eduInformation_Detail?resourceid=" + resourceId;
        G.log("------------------------" + url);
        setWebView();
    }

    private void setWebView() {

        wb_consult.getSettings().setJavaScriptEnabled(true);
        wb_consult.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wb_consult.loadUrl(url);
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("资讯详情");
    }
}
