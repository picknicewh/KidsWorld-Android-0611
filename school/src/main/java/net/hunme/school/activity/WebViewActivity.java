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
        webView.setWebViewClient(new MWebViewClient(webView));
        webView.setWebChromeClient(new MWebChromeClient(this));
        webSetting.setDefaultTextEncodingName("utf-8"); //设置编码
        webSetting.setJavaScriptEnabled(true); //支持js
        webView.addJavascriptInterface(this, "change_ngb");  //设置本地调用对象及其接口
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0)); //设置背景颜色 透明
        webSetting.setDomStorageEnabled(true);//使用localStorage则必须打开
        webSetting.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小
        String appCacheDir =getDir("cache", Context.MODE_PRIVATE).getPath();//缓存的地址
        webSetting.setAppCachePath(appCacheDir);//设置缓存地址
        webSetting.setAllowFileAccess(true); // 可以读取文件缓存(manifest生效)
        webSetting.setAppCacheEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);/// 默认使用缓存

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
