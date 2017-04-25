package net.hongzhang.school.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 作者： wanghua
 * 时间： 2017/4/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class OfficeReadActivity extends Activity implements View.OnClickListener {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageView iv_left;
    private TextView tv_title;
    private String baseUrl = "http://api.idocv.com/view/url?url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_read);
        initView();
        G.setTranslucent(this);
        setWebView();

    }
    private void initView(){
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        iv_left = (ImageView)findViewById(R.id.iv_left);
        tv_title = (TextView)findViewById(R.id.tv_title);
        iv_left.setOnClickListener(this);
    }
    private void setWebView() {
        try {
            String path = URLEncoder.encode("http://api.idocv.com/data/doc/manual.docx", "UTF-8");
            webView.loadUrl(baseUrl + path);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
