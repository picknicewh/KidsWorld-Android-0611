package net.hunme.discovery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.cordova.CordovaInterfaceImpl;
import net.hunme.baselibrary.cordova.MySystemWebView;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.network.ServerConfigManager;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.util.WebCommonPageFrom;
import net.hunme.user.activity.UserActivity;

import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;


/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：乐园--首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DiscoveryFragement extends BaseFragement implements View.OnClickListener{

    private SystemWebView webView;
    /**
     * 左边图片
     */
    private ImageView iv_left;
    /**
     * 中间文字
     */
    private TextView tv_title;
    /**
     * 右边图片
     */
    private ImageView iv_right;
    /**
     * 首页搜索
     */
    private Button bt_search;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    /**
     * web接口类
     */
    private WebCommonPageFrom from;
    private LinearLayout ll_discovery;
    /**
     * 没网络时显示
     */
    private static final String url = ServerConfigManager.WEB_IP+"/paradise/index.html";
    private ProgressBar pb_web;
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater localInflater = inflater.cloneInContext(new CordovaInterfaceImpl(getActivity(), this));
        View view = localInflater.inflate(R.layout.fragment_discovery, null);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                  if(webView.canGoBack()){

                      webView.goBack();
                  }else{
                      getActivity().finish();
                  }
                   return true;
                }
                return false;
            }
        });
    }
    private  void init(View v){
        iv_left = $(v,R.id.iv_dleft);
        tv_title = $(v,R.id.tv_dtitle);
        iv_right = $(v,R.id.iv_dright);
        bt_search = $(v,R.id.et_search);
        webView = $(v,R.id.cordovaWebView);
        pb_web=$(v,R.id.pb_web);
        ll_discovery=$(v,R.id.ll_cdiscovery);
        ImageCache.imageLoader(UserMessage.getInstance(getActivity()).getHoldImgUrl(),iv_left);
//        ll_loading = $(v,R.id.ll_loading);
        //rl_nonetwork= $(v,R.id.rl_nonetwork);
       // rl_nonetwork.setOnClickListener(this);
        from  = new WebCommonPageFrom(iv_left,tv_title,iv_right,bt_search,getActivity());
        iv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
       // et_search.setOnClickListener(this);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:goSearch_Origin()");
            }
        });
     /*   bt_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                webView.loadUrl("javascript:goSearch_Origin()");

            }
        });*/
       // setShowView();
       setWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
       if (G.KisTyep.isUpadteHold){
           ImageCache.imageLoader(UserMessage.getInstance(getActivity()).getHoldImgUrl(),iv_left);
       }

    }

    private  void  setShowView(){
         setWebView();
      /*   if (G.isNetworkConnected(getActivity())){
             rl_nonetwork.setVisibility(View.GONE);
         }else {
             rl_nonetwork.setVisibility(View.VISIBLE);
         }*/
     }
    private void  setWebView(){
        webView.addJavascriptInterface(from, "change_tb");  //设置本地调用对象及其接口
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView),pb_web,webView,getActivity(),ll_discovery));
        getWebView(webView).loadUrl(url+"?tsId="+ UserMessage.getInstance(getActivity()).getTsId());
//        if (!webView.getUrl().contains("paradiseHome")){
//            ll_loading.setVisibility(View.GONE);
//        }else {
//            ll_loading.setVisibility(View.VISIBLE);
//        }

    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
       if (viewId==R.id.iv_dleft){
           if (webView.getUrl().contains("paradiseHome")){
               Intent intent = new Intent(getActivity(), UserActivity.class);
               getActivity().startActivity(intent);
           }else {
               webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
               webView.goBack();

               //Log.i("TAGG",webView.getUrl());
           }
         } else if (viewId==R.id.iv_dright){
            String url = webView.getUrl();
            if (url.contains("childClass")){
                webView.loadUrl("javascript:goSearchVideo_Origin()");
            }else if (url.contains("childMusic")){
                webView.loadUrl("javascript:goSearchAudio_Origin()");
            }else if (url.contains("eduInformation")){
                webView.loadUrl("javascript:goSearchInf_Origin()");
            }else if (webView.getUrl().contains("paradiseHome")){
                webView.loadUrl("javascript:goHistory_Origin()");
            }
        }/*else if (viewId==R.id.et_search){
           webView.loadUrl("javascript:goSearch_Origin()");
        }*/
    }

}
