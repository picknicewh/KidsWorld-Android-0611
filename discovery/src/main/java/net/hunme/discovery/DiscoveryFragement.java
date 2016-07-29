package net.hunme.discovery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.util.WebCommonPageFrom;
import net.hunme.baselibrary.widget.MyViewView;


/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：乐园--首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DiscoveryFragement extends BaseFragement implements View.OnClickListener{

    private MyViewView webView;
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
     * webview
     */
    private String page ;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    /**
     * web接口类
     */
    private WebCommonPageFrom from;
   private  static   final String url = "http://192.168.5.136:8989/webSVN/kidsWorld/paradise/#/paradiseHome";
    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
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
                   // webView.goBack();   //这方法没用的哦
                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

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
        webView = $(v,R.id.wv_discovery);
        ll_loading = $(v,R.id.ll_loading);
        from  = new WebCommonPageFrom(iv_left,tv_title,iv_right,getActivity());
        page = from.getPage();
        setWebView();
        iv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }
    private void  setWebView(){
        webView.addJavascriptInterface(from, "change_tb");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,getActivity()));
        webView.setWebChromeClient(new MWebChromeClient(getActivity(),ll_loading,webView));
        webView.loadUrl(url);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_dleft){
             if (webView.getUrl().contains("paradiseHome")){
                 webView.loadUrl("javascript:goHistory_Origin()");
            }else {
                 webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                 webView.goBack();
             }
        }else if (viewId==R.id.iv_dright){
            String url = webView.getUrl();
            if (url.contains("childClass")){
                webView.loadUrl("javascript:goSearchVideo_Origin()");
            }else if (url.contains("childMusic")){
                webView.loadUrl("javascript:goSearchAudio_Origin()");
            }else if (url.contains("eduInformation")){
                webView.loadUrl("javascript:goSearchInf_Origin()");
            }else if (url.contains("paradiseHome")){
                webView.loadUrl("javascript:goSearch_Origin()");
            }
        }
    }

}
