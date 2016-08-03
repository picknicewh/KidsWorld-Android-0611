package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.SchoolFragement;

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
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    /**
     * 没网络时显示
     */
    private RelativeLayout rl_nonetwork;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_droidgap);
        super.init();
        launchUrl=getIntent().getStringExtra("loadUrl");
        G.log("loaduri-----"+launchUrl);
        loadUrl(launchUrl+"?TsId="+ UserMessage.getInstance(this).getTsId());
        //Log.i("TAFF",UserMessage.getInstance(this).getTsId()+"==============getTsId");
        initData();
      //  G.clearCacheFolder(getCacheDir(),System.currentTimeMillis());
    }

    private void  initData(){
        iv_left= (ImageView) findViewById(R.id.iv_left);
        tv_title= (TextView) findViewById(R.id.tv_title);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_subtitle= (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setOnClickListener(this);
      /*  rl_nonetwork=(RelativeLayout)findViewById(R.id.rl_nonetwork);
        rl_nonetwork.setOnClickListener(this);*/
        setToolBar();
    }

    private void setToolBar() {
        tv_title.setText(getIntent().getStringExtra("title"));
        String subTitle=getIntent().getStringExtra("subTitle");
        if(!G.isEmteny(subTitle)) {
            tv_subtitle.setText(subTitle);
            tv_subtitle.setVisibility(View.VISIBLE);
        }

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()){
                    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    webView.goBack();
                }else {
                    finish();
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (webView.getUrl().contains(SchoolFragement.INFORM))
            webView.loadUrl("javascript:getData()");
    }
    @Override
    protected CordovaWebView makeWebView() {
        webView = (SystemWebView) findViewById(R.id.cordovaWebView);
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView)));
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
                ll_loading.setVisibility(View.GONE);
            else
                ll_loading.setVisibility(View.VISIBLE);
        }
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
            }
        }
    }
}
