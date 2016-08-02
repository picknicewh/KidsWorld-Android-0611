package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.user.R;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

public class HMDroidGap extends CordovaActivity {
    private ImageView iv_left;
    private SystemWebView webView;
    private LinearLayout ll_loading;
    private TextView tv_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_droidgap);
        super.init();
        String uri=getIntent().getStringExtra("loadUrl");
        G.log("loaduri-----"+uri);
        launchUrl =uri;
        loadUrl(launchUrl);
        iv_left= (ImageView) findViewById(R.id.iv_left);
        tv_title= (TextView) findViewById(R.id.tv_title);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_title.setText(getIntent().getStringExtra("title"));
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()){
                    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    webView.goBack();
                }else {
                   finish();
                }
            }
        });
    }

    @Override
    protected CordovaWebView makeWebView() {
        webView = (SystemWebView) findViewById(R.id.cordovaWebView);
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView)));
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    @Override
    protected void createViews() {
        appView.getView().requestFocusFromTouch();
    }

    class MySystemWebView extends SystemWebChromeClient{

        public MySystemWebView(SystemWebViewEngine parentEngine) {
            super(parentEngine);
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
}
