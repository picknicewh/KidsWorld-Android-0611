package net.hunme.discovery;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.Constant;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
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
    private String page ;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
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
        rl_discovery = $(v,R.id.rl_discovery);
        ll_loading = $(v,R.id.ll_loading);
        setWebView();
        iv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }
    private void  setWebView(){
        webView.addJavascriptInterface(this, "change_tb");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,getActivity()));
        webView.setWebChromeClient(new MWebChromeClient(getActivity(),ll_loading,webView));
        webView.loadUrl(url);
    }
    /**
     * 设置导航栏标题
     */
    @JavascriptInterface
    public  void  setClassCauseTitle(final String title){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                tv_title.setText(title);
                tv_title.setVisibility(View.VISIBLE);
                iv_right.setVisibility(View.GONE);
            }
        });

    }
    /**
     * 设置导航栏
     */
    @JavascriptInterface
    public void  setToolBar(final String view){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (view){
                    case Constant.HOME:
                        iv_left.setImageResource(R.mipmap.ic_history);
                        tv_title.setText("乐园");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.HOME;
                        break;
                    case Constant.CHILDSTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿故事");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.CHILDSTORY;
                        break;
                    case Constant.CHILDCLASS:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);

                        page = Constant.CHILDCLASS;
                        break;
                    case Constant.CONSULT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);

                        page = Constant.CONSULT;
                        break;
                    case Constant.SAFEED:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);

                        page = Constant.SAFEED;
                        break;
                    case Constant.CONSULTDETAIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);

                        page = Constant.CONSULTDETAIL;
                        break;
                    case Constant.SEARCH:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);

                        page = Constant.SEARCH;
                        break;
                    case Constant.VEDIO:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);

                        page = Constant.VEDIO;
                        break;
                    case Constant.MEDIAPLAY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.MEDIAPLAY;
                        break;
                    case Constant.MEDIAPLAYDEATIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.MEDIAPLAYDEATIL;
                        break;
                    case Constant.MEDIAPLAYING:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);
                        page = Constant.MEDIAPLAYING;
                        break;
                    case Constant.SEARCH_CAUSRE:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_CAUSRE;
                        break;
                    case Constant.SEARCH_MUSIC:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索音乐");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_MUSIC;
                        break;
                    case Constant.SEARCH_CON:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_CON;
                        break;
                    case Constant.PLAY_HISTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("播放记录");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
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
                 webView.loadUrl("javascript:goHistory_Origin()");
            }else {
                 webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                 webView.goBack();
             }
        }else if (viewId==R.id.iv_dright){
            Log.i("page",page);
           switch (page){
               case Constant.CHILDCLASS:
                   webView.loadUrl("javascript:goSearchVideo_Origin()");
                   break;
               case Constant.MEDIAPLAY:
                   webView.loadUrl("javascript:goSearchAudio_Origin()");
                   break;
               case Constant.CONSULT:
                   webView.loadUrl("javascript:goSearchInf_Origin()");
                   break;
               case  Constant.HOME:
                   webView.loadUrl("javascript:goSearch_Origin()");
                   break;
           }
        }
    }

}
