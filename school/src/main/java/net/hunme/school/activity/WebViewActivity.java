package net.hunme.school.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.SchoolFragement;
import net.hunme.status.activity.PublishStatusActivity;
import net.hunme.status.widget.StatusPublishPopWindow;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

public class WebViewActivity extends CordovaActivity implements View.OnClickListener{
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
    public TextView tv_subtitle;
    /**
     * 网页
     */
    public static SystemWebView webView;
    private ProgressBar pb_web;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_droidgap);
        super.init();
        launchUrl=getIntent().getStringExtra("loadUrl");
       // Log.i("TAGG",launchUrl);
        loadUrl(launchUrl);
        initData();
    }
    /**
     * 初始化数据
     */
    private void  initData(){
        iv_left= (ImageView) findViewById(R.id.iv_left);
        tv_title= (TextView) findViewById(R.id.tv_title);
        pb_web = (ProgressBar) findViewById(R.id.pb_web);
        tv_subtitle= (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setOnClickListener(this);
       // rl_nonetwork=(RelativeLayout)findViewById(R.id.rl_nonetwork);
       // rl_nonetwork.setOnClickListener(this);
        setToolBar();
    }
    /**
     * 是在导航栏
     */
    private void setToolBar() {
        tv_title.setText(getIntent().getStringExtra("title"));
        String subTitle=getIntent().getStringExtra("subTitle");
        if(!G.isEmteny(subTitle)) {
            tv_subtitle.setText(subTitle);
            tv_subtitle.setVisibility(View.VISIBLE);
        }
        if (launchUrl.contains(SchoolFragement.ARRANGE)){
            if (UserMessage.getInstance(this).getType().equals("2")){
                tv_subtitle.setVisibility(View.VISIBLE);
            }else {
                tv_subtitle.setVisibility(View.GONE);
            }
        }
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (launchUrl.equals(webView.getUrl())){
                    finish();
                }else {
                    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    webView.goBack();
                }
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
       /*  int index =  webView.getUrl().lastIndexOf("/");
         String url =  webView.getUrl().substring(0,index)+ webView.getUrl().substring(index+1);
        //   String url =  webView.getUrl();
        Log.i("TAGFGG",url);
        if (webView.getUrl().contains(SchoolFragement.LEAVE)){
            webView.loadUrl(url);
        }else if (webView.getUrl().contains(SchoolFragement.ARRANGE)){
            webView.loadUrl(url);
        }*/
    }

    @Override
    protected CordovaWebView makeWebView() {
        webView = (SystemWebView) findViewById(R.id.cordovaWebView);
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView)));
        webView.addJavascriptInterface(this, "change_ngb");  //设置本地调用对象及其接口
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    @Override
    protected void createViews() {
        appView.getView().requestFocusFromTouch();
    }

    class MySystemWebView extends SystemWebChromeClient {

        public MySystemWebView(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 100 )
                pb_web.setVisibility(View.GONE);
            else{
                pb_web.setVisibility(View.VISIBLE);
                pb_web.setProgress(newProgress);
            }

        }
    }

    /**
     * 设置导航栏
     * @param  ContentTitle 中间标题
     * @param  RightTitle 右边标题
     */
    @JavascriptInterface
    public void  setNavigationbar(final String ContentTitle,final String RightTitle){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_title.setText(ContentTitle);
                if (RightTitle==null){
                    tv_subtitle.setVisibility(View.GONE);
                }else {
                    tv_subtitle.setVisibility(View.VISIBLE);
                    tv_subtitle.setText(RightTitle);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_subtitle){
            if (launchUrl.contains(SchoolFragement.INFORM)){
                Intent intent = new Intent(this,PublishActivity.class);
                startActivity(intent);
            }else if (launchUrl.contains(SchoolFragement.LEAVE)){
                Intent intent = new Intent(this,LeaveAskActivity.class);
                startActivity(intent);
            }else if (launchUrl.contains(SchoolFragement.ARRANGE)){
                Intent intent = new Intent(this,PublishStatusActivity.class);
                intent.putExtra("type", StatusPublishPopWindow.PICTURE);
                intent.putExtra("from","school");
                startActivity(intent);
            }
        }
    }
}
