package net.hunme.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.cordova.CordovaInterfaceImpl;
import net.hunme.baselibrary.cordova.HMDroidGap;
import net.hunme.baselibrary.cordova.MySystemWebView;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.network.ServerConfigManager;
import net.hunme.baselibrary.util.DateUtil;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MyConnectionStatusListener;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.status.activity.StatusDetilsActivity;
import net.hunme.status.mode.DynamicVo;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.UserActivity;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;


/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：动态首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragement extends BaseFragement implements View.OnClickListener,OkHttpListener{
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
//    private MyViewView webView;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    /**
     * 断网提示
     */
    private TextView tv_status_bar;
    /**
     * 动态uri地址  loadUrlStr = "file:///android_asset/www/sdk/view/index_android.html#/open-prepare";
                http://zhu.hunme.net:8080/KidsWorld/space/view/dynamic.html
                http://192.168.1.179:8787/web/kidsWorld/space/view/dynamic.html?
     */
    private static final String url = ServerConfigManager.WEB_IP+"/space/view/dynamic.html?";
    /**
     * 班级选择
     */
    private LinearLayout ll_classchoose;
    /**
     * 获取班级列表uri
     */
    private final String DYNAMICHEAD="/dynamics/getDynamicHead.do";
    private UserMessage um;
    /**
     * 班级列表对象
     */
    private List<DynamicVo> dynamicList;
    private RelativeLayout rl_toolbar;
    private SystemWebView webView;
    private int position = 0;
    private MyJpushReceiver myReceiver;
    private CordovaWebView cordovaWebView;
    private ProgressBar pb_web;
    public static String CLASSID;
    /**
     *网络状态监听
     */
   private ConnectionChangeReceiver connectionChangeReceiver;
    /**
     * 无网络状态
     */
    private RelativeLayout rl_nonetwork;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_status, null);
        LayoutInflater localInflater = inflater.cloneInContext(new CordovaInterfaceImpl(getActivity(), this));
        View view = localInflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_lift = $(view, R.id.iv_left);
        iv_right = $(view, R.id.iv_right);
        tv_classname = $(view, R.id.tv_classname);
        webView = $(view, R.id.cordovaWebView);
        ll_classchoose = $(view,R.id.ll_classchoose);
        rl_toolbar=$(view,R.id.rl_toolbar);
        tv_status_bar = $(view,R.id.tv_status_bar);
        rl_nonetwork= $(view,R.id.rl_nonetwork);
        rl_nonetwork.setOnClickListener(this);
        tv_status_bar.setOnClickListener(this);
        pb_web=$(view,R.id.pb_web);

        webView.addJavascriptInterface(this, "showDos");  //设置本地调用对象及其接口
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView),pb_web));
        cordovaWebView=getWebView(webView);
        um=UserMessage.getInstance(getActivity());
        classlist = new ArrayList<>();
        setViewAction();
        getDynamicHead();
        ImageCache.imageLoader(um.getHoldImgUrl(),iv_lift);
        registerReceiver();
    }
    public void setPosition(int position){
        this.position = position;
    }
    /**
     * 有无网络加载页面状态
     */
    private  void  setShowView(int position){
        setWebView(position);
        if (G.isNetworkConnected(getActivity())){
            rl_nonetwork.setVisibility(View.GONE);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);

        }
    }
    public void setWebView(int position){
        CLASSID=dynamicList.get(position).getGroupId();
//        cordovaWebView.loadUrl(url+"?tsId="+ UserMessage.getInstance(getActivity()).getTsId());
        String realUrl = null;
        try {
            realUrl = url+"groupId="+dynamicList.get(position).getGroupId()
                    +"&groupType="+dynamicList.get(position).getGroupType()
                    +"&tsId="+um.getTsId()+"&myName="+ URLEncoder.encode(um.getUserName(), "UTF-8")
                    +"&clickTime="+ URLEncoder.encode(DateUtil.formatDateTime(new Date()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cordovaWebView.loadUrl(realUrl);

        G.log("loadUrl====="+realUrl);
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
            }
        });
    }

    public void setClassname(String classname){
       tv_classname.setText(classname);
   }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.ll_classchoose){
            G.initDisplaySize(getActivity());
            int xPos = G.size.W/2-G.dp2px(getActivity(),75);
            popWindow.showAsDropDown(rl_toolbar,xPos,-G.dp2px(getActivity(),10));
            popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWindow.dismiss();
                    }
                }
            });
        }else if (viewId==R.id.rl_nonetwork){
          setShowView(position);
      }else if (viewId==R.id.tv_status_bar){
            Intent intent = null;
            // 先判断当前系统版本
            if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            }else{
                intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            }
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //用户发布动态成功 重新刷新数据
        if(G.KisTyep.isReleaseSuccess) {
            G.KisTyep.isReleaseSuccess = false;
            cordovaWebView.loadUrl("javascript:loadNew()");
        }
        if(G.KisTyep.isChooseId){
            //用户切换身份 重新刷新数据
            getDynamicHead();
            ImageCache.imageLoader(um.getHoldImgUrl(),iv_lift);
        }
        if(G.KisTyep.isUpadteHold){
            //用户切换头像 重新刷新数据
            ImageCache.imageLoader(um.getHoldImgUrl(),iv_lift);
        }
        // 动态发生改变 刷新数据
        if(G.KisTyep.isUpdateComment){
            G.KisTyep.isUpdateComment=false;
            cordovaWebView.loadUrl("javascript:pulldownRefresh()");
        }
    }
    @JavascriptInterface
    public void listenerReload(){
        RongIM.setConnectionStatusListener(new MyConnectionStatusListener(getActivity()));
    }

    @JavascriptInterface
    public void setStatus(){
        Intent myIntent = new Intent("net.hunme.kidsworld.MyStatusDosShowReceiver");
        myIntent.putExtra("count",0);
        getActivity().sendBroadcast(myIntent);
    }

    @JavascriptInterface
    public void noticeChange(){
        HMDroidGap.flag = 1;
    }

    @JavascriptInterface
    public void startStatusDetils(String dynamicId){
        if(G.isEmteny(dynamicId)){
            return;
        }
        Intent intent=new Intent(getActivity(), StatusDetilsActivity.class);
        intent.putExtra("dynamicId",dynamicId);
        startActivity(intent);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if(requestCode==101){
//            G.showToast(getActivity(),"我要刷新动态");
//            cordovaWebView.loadUrl("javascript:loadNew()");
//        }
//    }
    /**
     * 获取班级列表
     */
    private void getDynamicHead(){
        if(G.isEmteny(um.getTsId())){
            return;
        }
        Map<String,Object>map=new HashMap<>();
        map.put("tsId", um.getTsId());
        Type type=new TypeToken<Result<List<DynamicVo>>>(){}.getType();
        OkHttps.sendPost(type,DYNAMICHEAD,map,this,2,"DYNAMIC");
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(DYNAMICHEAD.equals(uri)){
            G.KisTyep.isChooseId=false;
            dynamicList = ((Result<List<DynamicVo>>) date).getData();
            classlist.clear();
            for (DynamicVo d: dynamicList){
                classlist.add(d.getGroupName());
            }
            popWindow = new ChooseClassPopWindow(this, classlist);
            ll_classchoose.setOnClickListener(this);
            if(dynamicList.size()>0){
                tv_classname.setText(classlist.get(0));
                setShowView(0);
                CLASSID=dynamicList.get(0).getGroupId();
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        G.KisTyep.isChooseId=false;
    }
    /**
    * 注册监听网络广播广播
    */
    private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(MyJpushReceiver.SHOWSTAUSDOL);
        myReceiver=new MyJpushReceiver();
        getActivity().registerReceiver(myReceiver, filter);

        IntentFilter filter2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        getActivity().registerReceiver(connectionChangeReceiver, filter2);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(myReceiver);
        getActivity().unregisterReceiver(connectionChangeReceiver);

    }
    /**
     * 有无网络监听广播
     */
     class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                tv_status_bar.setVisibility(View.VISIBLE);
            }else {
                tv_status_bar.setVisibility(View.GONE);

            }
        }
    }
    /**
     * 红点广播
     */
    class MyJpushReceiver extends BroadcastReceiver {
        public static final String SHOWSTAUSDOL = "net.hunme.status.showstatusdos";
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SHOWSTAUSDOL)) {
                Bundle bundle = intent.getExtras();
                String messagetype = bundle.getString("messagetype");
                String schoolid = bundle.getString("schoolid");
                String classid = bundle.getString("classid");
                String groupId = dynamicList.get(position).getGroupId();
                if (null!=messagetype&&messagetype.equals("1")) {
                     Intent myIntent = new Intent("net.hunme.kidsworld.MyStatusDosShowReceiver");
                    if (schoolid.equals("") && classid.equals(classid) ||classid.equals("")&&schoolid.equals(groupId)) {
                        myIntent.putExtra("count",1);
                    } else{
                        myIntent.putExtra("count",0);
                    }
                    context.sendBroadcast(myIntent);
                }
            }
        }
    }

    //拦截动态页面返回（不拦截的话Web页面会返回刷新） 不做任何操作 交给MainActiviy去处理
   @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return false;
                }
                return false;
            }
        });
    }
}
