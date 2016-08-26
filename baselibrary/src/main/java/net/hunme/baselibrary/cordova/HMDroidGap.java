package net.hunme.baselibrary.cordova;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.R;
import net.hunme.baselibrary.util.Constant;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MyConnectionStatusListener;
import net.hunme.baselibrary.util.WebCommonPageFrom;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import io.rong.imkit.RongIM;

public class HMDroidGap extends CordovaActivity implements View.OnClickListener{
    /**
     * 左边返回按钮
     */
    private ImageView iv_left;
    /**
     * html页面
     */
    private SystemWebView webView;
    /**
     * 标题
     */
    private TextView tv_title;
    /**
     * 右边内容
     */
    private TextView tv_subtitle;
    /**
     * 加载页面进度条
     */
    private ProgressBar pb_web;
    /**
     * web接口类
     */
    private WebCommonPageFrom from;
    /**
     * cordova插件
     */
    protected CordovaPlugin activityResultCallback = null;
    protected boolean activityResultKeepRunning;
    protected boolean keepRunning = true;
    /**
     * 标题
     */
    private LinearLayout ll_toolbar;
    public  static  int flag =0;
    /**
     * 无网络
     */
    private RelativeLayout rl_nonetwork;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_droidgap);
        BaseLibrary.addActivity(this);
        super.init();
        String uri=getIntent().getStringExtra("loadUrl");
        G.log("loaduri-----"+uri);

        launchUrl =uri;
        initView();
    }
    /**
     * 初始化
     */
    private void initView(){
        iv_left= (ImageView) findViewById(R.id.iv_left);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_subtitle= (TextView) findViewById(R.id.tv_subtitle);
        rl_nonetwork = (RelativeLayout)findViewById(R.id.rl_nonetwork);
        pb_web= (ProgressBar) findViewById(R.id.pb_web);
        ll_toolbar= (LinearLayout) findViewById(R.id.rl_toolbar);
        iv_left.setOnClickListener(this);
        setShowView();
        setTabBarText();
        // 抢登监听
        if (RongIM.getInstance()!=null){
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        }
    }
    /**
     * 设置web配置
     */
    private void setWebView(){
        from  = new WebCommonPageFrom(iv_left,tv_title,(ImageView) findViewById(R.id.iv_test),this);
        webView.addJavascriptInterface(from, "change_tb");  //设置本地调用对象及其接口
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView),pb_web,webView,this,ll_toolbar));
    }
    /**
     * 有无网络加载页面状态
     */
    private  void  setShowView(){
        setWebView();
        loadUrl(launchUrl);
        if (G.isNetworkConnected(this)){
            rl_nonetwork.setVisibility(View.GONE);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 设置标题的栏的内容
     */
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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_left){
            if (webView.canGoBack()){
                if (!webView.getUrl().contains(Constant.COLLECT)){}
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText("我的收藏");
                findViewById(R.id.iv_test).setVisibility(View.VISIBLE);
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.goBack();
            }else {
                from.sendBroadcast(true);
                finish();
            }
        }
    }
}
