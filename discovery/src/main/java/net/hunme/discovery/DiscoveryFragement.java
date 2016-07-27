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
     * 首页
     */
    private static  final String HOME = "home";
    /**
     * 幼儿教育
     */
    private static  final  String CHILDSTORY= "childStory";
    /**
     *幼儿课堂
     */
    private static final  String CHILDCLASS = "childClass";
    /**
     *咨询教育
     */
    private static final  String CONSULT = "consult";
    /**
     * 安全教育
     */
    private static final  String SAFEED = "safeed";
    /**
     *教育咨询详情
     */
    private static final  String CONSULTDETAIL = "consultDetail";
    /**
     * 搜索
     */
    private static final String SEARCH = "search";
    /**
     * 搜索课程
     */
    private static final String SEARCH_CAUSRE = "search_cause";
    /**
     * 搜索音乐
     */
    private static final String SEARCH_MUSIC = "search_music";
    /**
     * 搜索咨询
     */
    private static final String SEARCH_CON = "search_con";
    /**
     * 播放记录
     */
    private static final String PLAY_HISTORY = "play_history";
    /**
     * 视频
     */
    private static final String VEDIO = "video";
    /**
     * 音乐播放
     */
    private static final String MEDIAPLAYING = "mediaplaying";
    /**
     * 幼儿听听
     */
    private static final String MEDIAPLAY = "mediaplay";
    /**
     * 幼儿听听首页
     */
    private static final String MEDIAPLAYDEATIL = "mediaplaydetail";
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
                    webView.goBack();   //这方法没用的哦
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
            return deletedFiles;}
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
        Log.i("TAGGG",HOME);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("TAGGG",HOME);
                switch (view){
                    case HOME:
                        Log.i("TAGGG",HOME);
                        iv_left.setImageResource(R.mipmap.ic_search);
                        tv_title.setText("园所");
                        iv_right.setImageResource(R.mipmap.ic_history);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case CHILDSTORY:
                        Log.i("TAGGG",CHILDSTORY);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿故事");
                        iv_right.setImageResource(R.mipmap.search);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case CHILDCLASS:
                        Log.i("TAGGG",CHILDCLASS);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case CONSULT:
                        Log.i("TAGGG",CONSULT);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case SAFEED:
                        Log.i("TAGGG",SAFEED);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case CONSULTDETAIL:
                        Log.i("TAGGG",CONSULTDETAIL);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case SEARCH:
                        Log.i("TAGGG",SEARCH);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        iv_right.setImageResource(R.mipmap.ic_search);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case VEDIO:
                        Log.i("TAGGG",SEARCH);
                        rl_discovery.setVisibility(View.GONE);
                        break;
                    case MEDIAPLAY: Log.i("TAGGG",MEDIAPLAY);
                        iv_left.setImageResource(R.mipmap.ic_search);
                        tv_title.setText("幼儿听听");
                        iv_right.setImageResource(R.mipmap.search);
                        rl_discovery.setVisibility(View.VISIBLE);
                        break;
                    case MEDIAPLAYDEATIL:
                        Log.i("TAGGG",MEDIAPLAYDEATIL);
                        rl_discovery.setVisibility(View.VISIBLE);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        iv_right.setVisibility(View.GONE);
                        break;
                    case MEDIAPLAYING:
                        Log.i("TAGGG",MEDIAPLAYING);
                        rl_discovery.setVisibility(View.VISIBLE);
                        iv_left.setVisibility(View.GONE);
                        tv_title.setText("音乐播放");
                        iv_right.setVisibility(View.GONE);
                        break;
                    case SEARCH_CAUSRE:
                        Log.i("TAGGG",SEARCH_CAUSRE);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                    case SEARCH_MUSIC:
                        Log.i("TAGGG",SEARCH_MUSIC);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索音乐");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                    case SEARCH_CON:
                        Log.i("TAGGG",SEARCH_CON);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                    case PLAY_HISTORY:
                        Log.i("TAGGG",PLAY_HISTORY);
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        iv_right.setVisibility(View.GONE);
                        rl_discovery.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_dleft){

        }else if (viewId==R.id.iv_dright){

        }
    }
}
