package net.hongzhang.user.activity;

import android.os.Bundle;
import android.webkit.WebView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;

public class WebViewActivity extends BaseActivity {
    private String url;
    private WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mwebview);
        initview();

    }
   private void initview(){
      webView = (WebView) findViewById(R.id.m_webview);
       url = getIntent().getStringExtra("url");
       webView.loadUrl(url);
   }
    @Override
    protected void setToolBar() {
        int source = getIntent().getIntExtra("source", 0);
        if (source == 0) {
            setCententTitle("主播认证");
        } else if (source == 1) {
            setCententTitle("版权声明");
        } else if (source == 2) {
            setCententTitle("服务条款");
        }else if (source==3){
            setCententTitle("用户协议");
        }
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }
}
