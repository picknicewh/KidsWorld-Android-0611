package net.hunme.discovery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.discovery.util.MWebChromeClient;
import net.hunme.discovery.util.MWebViewClient;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：乐园--首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DiscoveryFragement extends BaseFragement implements View.OnClickListener{
    /**
     * 左边的显示
     */
    public TextView tv_left;
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
     * webview
     */
    private WebView webView;
    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
        init(view);
        return view;
    }
    private  void init(View v){
        tv_left = $(v,R.id.tv_left);
        tv_right = $(v,R.id.tv_right);
        tv_title = $(v,R.id.tv_dtitle);
        iv_left = $(v,R.id.iv_left);
        webView = $(v,R.id.wv_discovery);
        webView.loadUrl("file:///android_asset/discovery/paradise/index.html#/paradiseHome");
        interactive(webView);
        tv_right.setOnClickListener(this);
        tv_left.setOnClickListener(this);
      //  getActivity().onKeyDown(KeyEvent.KEYCODE_BACK,)
    //    getActivity().onKeyDown(KeyEvent.KEYCODE_BACK,new KeyDowmEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
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
        webView.setWebChromeClient(new MWebChromeClient(getActivity()));
        webSetting.setDefaultTextEncodingName("utf-8"); //设置编码
        webSetting.setJavaScriptEnabled(true); //支持js
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0)); //设置背景颜色 透明
        webView.addJavascriptInterface(this, "change_nb");  //设置本地调用对象及其接口
        webSetting.setDomStorageEnabled(true);//使用localStorage则必须打开
        webSetting.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小
        String appCacheDir = getActivity().getDir("cache", Context.MODE_PRIVATE).getPath();//缓存的地址
        webSetting.setAppCachePath(appCacheDir);//设置缓存地址
        webSetting.setAllowFileAccess(true); // 可以读取文件缓存(manifest生效)
        webSetting.setAppCacheEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);/// 默认使用缓存
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_left){
            webView.loadUrl("javascript:goChildMusic_Origin()");
        }else if (view.getId()==R.id.tv_right){}
    }
    /**
     * 设置导航栏
    */
    @JavascriptInterface
    public void  setToolbar(final String LeftTitle,final String centerTitle,final String RightTitle){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (LeftTitle==null){
                    tv_left.setVisibility(View.GONE);
                    iv_left.setVisibility(View.VISIBLE);
                }else {
                    tv_left.setVisibility(View.VISIBLE);
                    iv_left.setVisibility(View.GONE);
                    tv_left.setText(LeftTitle);
                }
                tv_title.setText(centerTitle);
                if (RightTitle==null){
                    tv_right.setVisibility(View.GONE);
                }else {
                    tv_right.setVisibility(View.VISIBLE);
                    tv_right.setText(RightTitle);
                }
            }
        });
    }
}
