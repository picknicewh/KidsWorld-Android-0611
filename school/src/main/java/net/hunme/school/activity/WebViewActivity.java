package net.hunme.school.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.widget.MyViewView;
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
    private MyViewView webView;
    /**
     * url地址
     */
    private String url;
    /**
     * 左边的title
     */
    private String rightTitle;
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
      //  G.clearCacheFolder(getCacheDir(),System.currentTimeMillis());
    }

    private void  initData(){
        webView = $(R.id.wv_totle);
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
        setWebView();
        setSubTitleOnClickListener(this);
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
                if (url.equals(webView.getUrl())){
                    finish();
                }else {
                    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    webView.goBack();
                }
            }
        });
    }
    private void  setWebView(){
        webView.addJavascriptInterface(this, "change_ngb");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webView.loadUrl(url);
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
        if (view.getId() == R.id.tv_subtitle){
            if (url.contains(SchoolFragement.INFORM)){
                Intent intent = new Intent(this,PublishActivity.class);
                startActivity(intent);
            }else if (url.contains(SchoolFragement.LEAVE)){
                Intent intent = new Intent(this,LeaveAskActivity.class);
                startActivity(intent);
            }
        }
    }
}
