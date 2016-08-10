package net.hunme.baselibrary.cordova;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.R;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.WebCommonPageFrom;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

public class HMDroidGap extends CordovaActivity {
    private ImageView iv_left;
    private SystemWebView webView;
    private TextView tv_title;
    private TextView tv_subtitle;
    private ProgressBar pb_web;
    /**
     * web接口类
     */
    private WebCommonPageFrom from;
    protected CordovaPlugin activityResultCallback = null;
    protected boolean activityResultKeepRunning;
    protected boolean keepRunning = true;
    private RelativeLayout rl_toolbar;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
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
        tv_subtitle= (TextView) findViewById(R.id.tv_subtitle);
        pb_web= (ProgressBar) findViewById(R.id.pb_web);
        rl_toolbar= (RelativeLayout) findViewById(R.id.rl_toolbar);
        from  = new WebCommonPageFrom(iv_left,tv_title,(ImageView) findViewById(R.id.iv_test),this);
        webView.addJavascriptInterface(from, "change_tb");  //设置本地调用对象及其接口
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView),pb_web,webView,this,rl_toolbar));
        setTabBarText();
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

    private void setTabBarText(){
        tv_title.setText(getIntent().getStringExtra("title"));
        String subTitle=getIntent().getStringExtra("subTitle");
        if(!G.isEmteny(subTitle)) {
            tv_subtitle.setText(subTitle);
            tv_subtitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected CordovaWebView makeWebView() {
        webView = (SystemWebView) findViewById(R.id.cordovaWebView);
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    @Override
    protected void createViews() {
        appView.getView().requestFocusFromTouch();
    }

    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        this.activityResultCallback = command;
        this.activityResultKeepRunning = this.keepRunning;
        // If multitasking turned on, then disable it for activities that return
        // results
        if (command != null) {
            this.keepRunning = false;
        }
        // Start activity
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        CordovaPlugin callback = this.activityResultCallback;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
