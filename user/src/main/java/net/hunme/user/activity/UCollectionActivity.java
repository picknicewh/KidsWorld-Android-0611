package net.hunme.user.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.Constant;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
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
    /**
     * 加载页面动画
     */
     private LinearLayout ll_loading;
    /**
     * 加载地址
     */
    private static final  String url = "http://192.168.5.136:8989/webSVN/kidsWorld/paradise/#/collect";
    /**
     * webview
     */
    private MyViewView webView;
    /**
     * 页面
     */
    private String page;
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
        setWebView();
    }
    @Override
    protected void setToolBar() {
         setLiftImage(R.mipmap.ic_arrow_lift);
         setCententTitle("我的收藏");
         setLiftOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (page.equals(Constant.COLLECT)){
                     Log.i("TAGGG","cccccccccccc");
                     finish();
                 }else {
                     Log.i("TAGGG","BBBBBBBBBBBBBB");
                     webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                     webView.goBack();
                 }
             }
         });
    }
    private void  setWebView(){
        webView.addJavascriptInterface(this, "change_tob");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webView.loadUrl(url);
    }
    /**
     * 设置导航栏
     */
    /**
     * 设置导航栏
     */
    @JavascriptInterface
    public void  setMyToolbar(final String view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (view){
                    case Constant.CHILDSTORY:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("幼儿故事");
                        page = Constant.CHILDSTORY;
                        break;
                    case Constant.CHILDCLASS:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("幼儿课堂");
                        page = Constant.CHILDCLASS;
                        break;
                    case Constant.CONSULT:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("教育资讯");
                        page = Constant.CONSULT;
                        break;
                    case Constant.SAFEED:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("安全教育");
                        page = Constant.SAFEED;
                        break;
                    case Constant.CONSULTDETAIL:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("教育资讯");
                        page = Constant.CONSULTDETAIL;
                        break;
                    case Constant.VEDIO:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        page = Constant.VEDIO;
                        break;
                    case Constant.MEDIAPLAY:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("幼儿听听");
                        page = Constant.MEDIAPLAY;
                        break;
                    case Constant.MEDIAPLAYDEATIL:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("幼儿听听");
                         page = Constant.MEDIAPLAYDEATIL;
                    case Constant.COLLECT:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        setCententTitle("收藏");
                        page = Constant.COLLECT;
                        break;
                    case Constant.MEDIAPLAYING:
                        setLiftImage(R.mipmap.ic_arrow_lift);
                        page = Constant.MEDIAPLAYING;
                        break;
                }
                Log.i("TAGGG",page);
            }

        });
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
                Log.i("TAGGG","cccccccccccc");
              finish();
            }else {
                Log.i("TAGGG","BBBBBBBBBBBBBB");
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.goBack();
            }
        }
    }
}
