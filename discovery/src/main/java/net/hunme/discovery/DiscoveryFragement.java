package net.hunme.discovery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;

import java.io.File;


/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：乐园--首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DiscoveryFragement extends BaseFragement implements View.OnClickListener{

    private WebView webView;

    /**
     * 导航栏
     */
    private RelativeLayout rl_discovery;
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
    private String page  ;

    @SuppressLint("JavascriptInterface,SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
        clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());//删除此时之前的缓存.
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
    private int clearCacheFolder(File dir, long numDays) {
           int deletedFiles = 0;
           if (dir!= null && dir.isDirectory()) {
                   try {
                          for (File child:dir.listFiles()) {
                                  if (child.isDirectory()) {
                                        deletedFiles += clearCacheFolder(child, numDays);
                                   }
                             if (child.lastModified() < numDays) {
                                 if (child.delete()) {
                                          deletedFiles++;
                                              }
                                     }
                              }
                     } catch(Exception e) {
                         e.printStackTrace();
                     }
           }
            return deletedFiles;
    }
    private  void init(View v){
        iv_left = $(v,R.id.iv_dleft);
        tv_title = $(v,R.id.tv_dtitle);
        iv_right = $(v,R.id.iv_dright);
        webView = $(v,R.id.wv_discovery);
        rl_discovery = $(v,R.id.rl_discovery);
        interactive(webView);
        final String url = "http://192.168.5.136:8989/webSVN/kidsWorld/paradise/#/paradiseHome";
        webView.loadUrl(url);
        iv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.goBack();
            }
        });
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
        webView.addJavascriptInterface(this, "change_tb");  //设置本地调用对象及其接口
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
        webSetting.setDatabaseEnabled(true); // 应用可以有数据库
    }
    /**
     * 设置导航栏
     */

    @JavascriptInterface
    public void  setToolBar(final String view){
        Log.i("TAGGG",Constant.HOME);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("TAGGG",Constant.HOME);
                switch (view){
                    case Constant.HOME:
                        Log.i("TAGGG",Constant.HOME);
                        iv_left.setImageResource(R.mipmap.search);
                        tv_title.setText("园所");
                        iv_right.setImageResource(R.mipmap.ic_history);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.HOME;
                        break;
                    case Constant.CHILDSTORY:
                        Log.i("TAGGG",Constant.CHILDSTORY);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿故事");
                        iv_right.setImageResource(R.mipmap.search);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.CHILDSTORY;
                        break;
                    case Constant.CHILDCLASS:
                        Log.i("TAGGG",Constant.CHILDCLASS);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        page = Constant.CHILDSTORY;
                        break;
                    case Constant.CONSULT:
                        Log.i("TAGGG",Constant.CONSULT);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.CONSULT;
                        break;
                    case Constant.SAFEED:
                        Log.i("TAGGG",Constant.SAFEED);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.SAFEED;
                        break;
                    case Constant.CONSULTDETAIL:
                        Log.i("TAGGG",Constant.CONSULTDETAIL);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.CONSULTDETAIL;
                        break;
                    case Constant.SEARCH:
                        Log.i("TAGGG",Constant.SEARCH);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.SEARCH;
                        break;
                    case Constant.VEDIO:
                        Log.i("TAGGG",Constant.SEARCH);
                        rl_discovery.setVisibility(View.GONE);
                        page = Constant.VEDIO;
                        break;
                    case Constant.MEDIAPLAY: Log.i("TAGGG",Constant.MEDIAPLAY);
                        iv_left.setImageResource(R.mipmap.ic_search);
                        tv_title.setText("幼儿听听");
                        iv_right.setImageResource(R.mipmap.search);
                        iv_right.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.MEDIAPLAY;
                        break;
                    case Constant.MEDIAPLAYDEATIL:
                        Log.i("TAGGG",Constant.MEDIAPLAYDEATIL);
                        rl_discovery.setVisibility(View.VISIBLE);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        iv_right.setVisibility(View.GONE);
                        page = Constant.MEDIAPLAYDEATIL;
                        tv_title.setVisibility(View.VISIBLE);

                        break;
                    case Constant.MEDIAPLAYING:
                        Log.i("TAGGG",Constant.MEDIAPLAYING);
                        rl_discovery.setVisibility(View.VISIBLE);
                        iv_left.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);
                        iv_right.setImageResource(R.mipmap.ic_arrow_lift);
                        page = Constant.MEDIAPLAYING;
                        break;
                    case Constant.SEARCH_CAUSRE:
                        Log.i("TAGGG",Constant.SEARCH_CAUSRE);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.SEARCH_CAUSRE;
                    case Constant.SEARCH_MUSIC:
                        Log.i("TAGGG",Constant.SEARCH_MUSIC);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索音乐");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        page = Constant.SEARCH_MUSIC;
                        break;
                    case Constant.SEARCH_CON:
                        Log.i("TAGGG",Constant.SEARCH_CON);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        page = Constant.SEARCH_CON;
                        break;
                    case Constant.PLAY_HISTORY:
                        Log.i("TAGGG",Constant.PLAY_HISTORY);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("播放记录");
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.VISIBLE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        page = Constant.PLAY_HISTORY;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_dleft){
             if (page.equals(Constant.HOME)){
                 webView.loadUrl("javascript:goSearch_Origin()");
            }
        }else if (viewId==R.id.iv_dright){
           switch (page){
               case Constant.CHILDCLASS:
                   webView.loadUrl("javascript:goSearchVideo_Origin()");
                   break;
               case Constant.MEDIAPLAYDEATIL:
                   webView.loadUrl("javascript:goSearchAudio_Origin()");
                   break;
               case Constant.CONSULT:
                   webView.loadUrl("javascript:goSearchInf_Origin()");
                   break;
               case  Constant.HOME:
                   webView.loadUrl("javascript:goHistory_Origin()");
                   break;
           }
        }
    }
}
