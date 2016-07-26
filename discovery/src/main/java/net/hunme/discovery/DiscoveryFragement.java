package net.hunme.discovery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;


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
    private WebView webView;
    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,null);
      //  clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());//删除此时之前的缓存.
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

  /*  private int clearCacheFolder(File dir, long numDays) {
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
*/
    private  void init(View v){
        iv_left = $(v,R.id.iv_dleft);
        tv_title = $(v,R.id.tv_dtitle);
        iv_right = $(v,R.id.iv_dright);
        webView = $(v,R.id.wv_discovery);
        interactive(webView);
        webView.loadUrl("http://192.168.5.136:8989/webSVN/kidsWorld/paradise/#/paradiseHome");
        iv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
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
        webView.addJavascriptInterface(this, "change_nb");  //设置本地调用对象及其接口
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
        String dbPath = this.getActivity().getDir("database", Context.MODE_PRIVATE).getPath();
        webSetting.setDatabasePath(dbPath);

    }
    /**
     * 设置导航栏
     */
    @JavascriptInterface
    public void  setToolbar(final String left,final String ContentTitle,final String right){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (left!=null){

                }
                tv_title.setText(ContentTitle);
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
