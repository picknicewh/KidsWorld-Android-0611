package net.hunme.discovery;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView tv_record;
    /**
     * 中间的标题
     */
    private TextView tv_title;
    /**
     * 右边显示
     */
    private ImageView iv_search;
    /**
     * webview
     */
    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
        init(view);
        return view;
    }
    private  void init(View v){
        tv_record = $(v,R.id.tv_drecord);
        iv_search = $(v,R.id.iv_dsearch);
        tv_title = $(v,R.id.tv_dtitle);
        webView = $(v,R.id.wv_discovery);
        webView.loadUrl("file:///android_asset/discovery/paradise/index.html#/paradiseHome");
        interactive(webView);
        tv_record.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }
     /**
     * 交互配置
     * @param  webView
     */
    private  void interactive(WebView webView){
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new MWebViewClient(webView));
        webView.setWebChromeClient(new MWebChromeClient(getActivity()));
        webView.getSettings().setDefaultTextEncodingName("utf-8"); //设置编码
        webView.getSettings().setJavaScriptEnabled(true); //支持js
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0)); //设置背景颜色 透明
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //javascript调用本地，新建一个类。调用类中的方法给js调用,其中myObj为调用的名字
        //webView.addJavascriptInterface(null, "myObj");  //设置本地调用对象及其接口
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_drecord){
            webView.loadUrl("javascript:goChildClass()");
        }else if (view.getId()==R.id.iv_dsearch){
        }
    }
}
