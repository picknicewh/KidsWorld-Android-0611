package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 作者： wh
 * 时间： 2016/7/28
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyViewView extends WebView {
    private Context context;

    public MyViewView(Context context) {
        super(context);
        this.context =context;
        interactive();
    }

    public MyViewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        interactive();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        interactive();
    }

    /**
     * 交互配置
     * @param
     */
    public   void interactive(){
        WebSettings webSettings = getSettings();//支持插件
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8"); //设置编码
        webSettings.setJavaScriptEnabled(true); //支持js
        setBackgroundColor(Color.argb(0, 0, 0, 0)); //设置背景颜色 透明
        webSettings.setUseWideViewPort(true);
       // setWebViewClient(new MWebViewClient(this,context));
        initWebView(webSettings);
    }
    /**
     * 缓存设置
     */
    public void initWebView(WebSettings webSettings) {
        webSettings.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);  //开启 database
        String appCacheDir = context.getDir("cache", Context.MODE_PRIVATE).getPath();//缓存的地storage API 功能址
        webSettings.setDatabasePath(appCacheDir); //设置数据库缓存路径
        webSettings.setAppCachePath(appCacheDir);   //设置  Application Caches 缓存目录
        webSettings.setAppCacheEnabled(true);  //开启 Application Caches 功能*/
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
    }
}
