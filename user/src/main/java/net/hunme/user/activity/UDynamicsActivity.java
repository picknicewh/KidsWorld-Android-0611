package net.hunme.user.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.widget.MyViewView;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户动态
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UDynamicsActivity extends BaseActivity {
    /**
     * 加载页面动画
     */
    private LinearLayout ll_loading;
    /**
     * 加载地址
     */
    private static final  String url = "";
    /**
     * webview
     */
    private MyViewView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamics);
        initview();
    }
    private void initview(){
        ll_loading = $(R.id.ll_loading);
        webView = $(R.id.wv_status);
        setWebView();
    }
    private void  setWebView(){
       // webView.addJavascriptInterface(this, "change_tob");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webView.loadUrl(url);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的动态");
    }
}
