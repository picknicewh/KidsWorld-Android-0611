package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.widget.MyViewView;
import net.hunme.login.UserChooseActivity;
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
public class UDynamicsActivity extends BaseActivity implements View.OnClickListener {
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
    /**
     * 没有网络时显示内容
     */
    private RelativeLayout rl_nonetwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamics);
        initview();
    }
    private void initview(){
        ll_loading = $(R.id.ll_loading);
        webView = $(R.id.wv_status);
        rl_nonetwork = $(R.id.rl_nonetwork);
        rl_nonetwork.setOnClickListener(this);
        rl_nonetwork.setVisibility(View.GONE);
        setWebView();
      //  setviewShow();

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
    private void  setWebView(){
        webView.addJavascriptInterface(this, "change_tob");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,this));
        webView.setWebChromeClient(new MWebChromeClient(this,ll_loading,webView));
        webView.loadUrl(url);
    }
    @JavascriptInterface
    public void noticeChange(){
        Toast.makeText(this,"重新加载动态",Toast.LENGTH_SHORT).show();
        UserChooseActivity.flag=1;
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的动态");
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_nonetwork){
            setviewShow();
        }
    }

}
