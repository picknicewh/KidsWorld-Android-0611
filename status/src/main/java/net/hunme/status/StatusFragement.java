package net.hunme.status;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.UserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：动态首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragement extends BaseFragement implements View.OnClickListener{
    /**
     * 头像
     */
    private CircleImageView iv_lift;
    /**
     * 发布说说按钮
     */
    private ImageView iv_right;
    /**
     * 班级的名字
     */
    private TextView tv_classname;
    /**
     * 班级列表
     */
    private List<String> classlist ;
    /**
     * 选择班级弹窗
     */
    private ChooseClassPopWindow popWindow;
    /**
     * 显示html5
     */
    private WebView webView;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    private static  final  String url = "http://192.168.1.179:8787/web/kidsWorld/space/view/dynamic.html";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        iv_lift=$(view,R.id.iv_left);
        iv_right=$(view,R.id.iv_right);
        tv_classname=$(view,R.id.tv_classname);
        webView = $(view,R.id.wv_status);
        ll_loading = $(view,R.id.ll_loading);
        interactive(webView);
        webView.loadUrl(url);
        setViewAction();
        classlist = new ArrayList<>();
        classlist.add("一(1)班");
        classlist.add("一(2)班");
        classlist.add("一(3)班");
        popWindow = new ChooseClassPopWindow(this,classlist);
        tv_classname.setOnClickListener(this);
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
        webView.setWebViewClient(new MWebViewClient(webView,getActivity()));
        webView.setWebChromeClient(new MWebChromeClient(getActivity(),ll_loading,webView));
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
        String appCacheDir =getActivity().getDir("cache", Context.MODE_PRIVATE).getPath();//缓存的地址
        webSettings.setDatabasePath(appCacheDir); //设置数据库缓存路径
        webSettings.setAppCachePath(appCacheDir);   //设置  Application Caches 缓存目录
        webSettings.setAppCacheEnabled(true);  //开启 Application Caches 功能
    }
    /**
     * 设置选择弹窗
     */
    private void setViewAction(){
        iv_lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserActivity.class));
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StatusPublishPopWindow pubishPopWindow = new StatusPublishPopWindow(getActivity());
                pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                pubishPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            pubishPopWindow.dismiss();
                        }
                    }
                });
            }
        });
    }

   public void setClassname(String classname){
       tv_classname.setText(classname);
   }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.tv_classname){
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            G.initDisplaySize(getActivity());
            int width = (location[0]-popWindow.getWidth())/2;
            int height = G.dp2px(getActivity(),75)+popWindow.getHeight();
            popWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, -G.dp2px(getActivity(),180));
            popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWindow.dismiss();
                    }
                }
            });
        }
    }
}
