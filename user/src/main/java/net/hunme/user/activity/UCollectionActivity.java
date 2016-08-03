package net.hunme.user.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.Constant;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.util.WebCommonPageFrom;
import net.hunme.baselibrary.widget.MyViewView;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户收藏
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UCollectionActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    /**
     * 加载页面动画
     */
     private LinearLayout ll_loading;
    /**
     * 加载地址
     */
    private static final String url = "http://192.168.5.136:8989/webSVN/kidsWorld/paradise/#/collect";
    /**
     * webview
     */
    private MyViewView webView;
    /**
     * 页面
     */
    private String page;
    /**
     * web接口类
     */
    private WebCommonPageFrom from;
    /**
     * 没有网络时显示内容
     */
    private RelativeLayout rl_nonetwork;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initview();
    }
    private void initview(){
        ll_loading = $(R.id.ll_loading);
        webView = $(R.id.wv_collection);
        iv_left =$(R.id.iv_left);
        iv_right = $(R.id.iv_right);
        tv_title = $(R.id.tv_title);
        from  = new WebCommonPageFrom(iv_left,tv_title,iv_right,this);
        rl_nonetwork = $(R.id.rl_nonetwork);
        rl_nonetwork.setOnClickListener(this);
        setviewShow();
    }
    @Override
    protected void setToolBar() {
         setLiftImage(R.mipmap.ic_arrow_lift);
         setCententTitle("我的收藏");
         setLiftOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (webView.getUrl().contains(Constant.COLLECT)){
                     finish();
                 }else {
                     webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                     webView.goBack();
                 }
             }
         });
    }
    private void  setWebView(){
        webView.addJavascriptInterface(from, "change_tb");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webView.loadUrl(url);
    }
    /**
     * 设置有无网络时候显示状态
     */
    private void setviewShow(){
        setWebView();
        if (!G.isNetworkConnected(this)){
            rl_nonetwork.setVisibility(View.VISIBLE);
        }else {
            rl_nonetwork.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            // 返回上一页面
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        int viewId =view.getId();
        if (viewId==R.id.iv_left){
            if (page.equals(Constant.COLLECT)){
              finish();
            }else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.goBack();
            }
        }else if (view.getId()==R.id.rl_nonetwork){
            setviewShow();
        }
    }
}
