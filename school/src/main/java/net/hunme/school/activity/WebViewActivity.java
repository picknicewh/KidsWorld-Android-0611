package net.hunme.school.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.school.R;
import net.hunme.school.SchoolFragement;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 左边的图片
     */
    public ImageView iv_left;
    /**
     * 中间的标题
     */
    public TextView tv_title;
    /**
     * 右边显示
     */
    public TextView tv_right;
    /**
     * 网页
     */
    private WebView wv_totle;
    /**
     * url地址
     */
    private String url;
    /**
     * 左边的title
     */
    private String rightTitle;
    /**
     * flag
     */
    private int flag=0;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initData();
        G.clearCacheFolder(getCacheDir(),System.currentTimeMillis());
    }

    private void  initData(){
        wv_totle = $(R.id.wv_totle);
        iv_left = $(R.id.iv_left);
        tv_title = $(R.id.tv_title);
        tv_right = $(R.id.tv_subtitle);
        ll_loading = $(R.id.ll_loading);
        Intent intent = getIntent();
        rightTitle  =intent.getStringExtra("rightTitle");
        if (rightTitle!=null){
            Log.i("TRF",rightTitle);
            tv_right.setText(rightTitle);
        }
        url = intent.getStringExtra("url");
        interactive(wv_totle);
        Log.i("FFF",getCacheDir().lastModified()+"");

        wv_totle.loadUrl(url);

    }
    @Override
    protected void setToolBar() {
        setCententTitle(getIntent().getStringExtra("title"));
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitle(getIntent().getStringExtra("rightTitle"));
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url.equals(wv_totle.getUrl())){
                    finish();
                }else {
                    wv_totle.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    wv_totle.goBack();
                }
            }
        });
    }

    /**
     * 交互配置
     * @param  webView
     */
    private  void interactive(WebView webView){
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        WebSettings webSetting = webView.getSettings();
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webSetting.setDefaultTextEncodingName("utf-8"); //设置编码
        webSetting.setJavaScriptEnabled(true); //支持js
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0)); //设置背景颜色 透明
        webView.addJavascriptInterface(this, "change_tb");  //设置本地调用对象及其接口
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setUseWideViewPort(true);//支持插件
        initWebView(webView);
    }
    /**
     * 缓存设置
     * @param  mWebView
     */
    private void initWebView(WebView mWebView) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);  //开启 database storage API 功能
        String appCacheDir = getDir("cache", Context.MODE_PRIVATE).getPath();//缓存的地址
        webSettings.setDatabasePath(appCacheDir); //设置数据库缓存路径
        webSettings.setAppCachePath(appCacheDir);   //设置  Application Caches 缓存目录
        webSettings.setAppCacheEnabled(true);  //开启 Application Caches 功能
    }
    /**
     * 设置导航栏
     */
    @JavascriptInterface
    public void  setNavigationbar(final String ContentTitle,final String RightTitle){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setCententTitle(ContentTitle);
                if (RightTitle==null){
                    tv_right.setVisibility(View.GONE);
                }else {
                    tv_right.setVisibility(View.VISIBLE);
                    tv_right.setText(RightTitle);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_totle.canGoBack()) {
            // 返回上一页面
                wv_totle.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                wv_totle.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_subtitle){
            if (url.contains(SchoolFragement.INFORM)){
                Intent intent = new Intent(this,PublishInformActivity.class);
                startActivity(intent);
            }
        }
    }
}
