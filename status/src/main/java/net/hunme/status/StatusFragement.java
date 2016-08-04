package net.hunme.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.cordova.CordovaInterfaceImpl;
import net.hunme.baselibrary.cordova.MySystemWebView;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.status.activity.PublishStatusActivity;
import net.hunme.status.mode.DynamicVo;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.UserActivity;

import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 动态uri地址
     */
    private static final String url = "http://192.168.1.179:8787/web/kidsWorld/space/view/dynamic.html?class=2";//&
    /**
     * 班级选择
     */
    private LinearLayout ll_classchoose;
    /**
     * 获取班级列表uri
     */
    private final String DYNAMICHEAD="/dynamic/getDynamicHead.do";
    private UserMessage um;
    /**
     * 班级列表对象
     */
    private List<DynamicVo> dynamicList;
    private RelativeLayout rl_toolbar;
    private SystemWebView webView;
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
        ll_loading = $(view, R.id.ll_loading);
        ll_classchoose = $(view,R.id.ll_classchoose);
        rl_toolbar=$(view,R.id.rl_toolbar);
        um=UserMessage.getInstance(getActivity());
        classlist = new ArrayList<>();
        setViewAction();
        getDynamicHead();
    }

    public void setWebView(int position){
        webView.addJavascriptInterface(this, "change");  //设置本地调用对象及其接口
        webView.setWebChromeClient(new MySystemWebView(new SystemWebViewEngine(webView),ll_loading));
        getWebView(webView).loadUrl(url+"&groupId="+dynamicList.get(position).getGroupId()
                +"&groupName="+dynamicList.get(position).getGroupName()
                +"&groupType="+dynamicList.get(position).getGroupType());
        G.log("loadUrl====="+url+"&groupId="+dynamicList.get(position).getGroupId()
                +"&groupName="+dynamicList.get(position).getGroupName()
                +"&groupType="+dynamicList.get(position).getGroupType());
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
                pubishPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            pubishPopWindow.dismiss();
                        }
                    }
                });
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
            G.log(G.size.W/2+"----------------"+popWindow.getContentView().getWidth()/2);
            popWindow.showAsDropDown(rl_toolbar,xPos,-G.dp2px(getActivity(),0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PublishStatusActivity.isReleaseSuccess) {
            PublishStatusActivity.isReleaseSuccess = false;
            webView.loadUrl("javascript:pulldownRefresh()");
        }
        ImageCache.imageLoader(um.getHoldImgUrl(),iv_lift);
    }

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
        dynamicList = ((Result<List<DynamicVo>>) date).getData();
        for (DynamicVo d: dynamicList){
            classlist.add(d.getGroupName());
        }
        popWindow = new ChooseClassPopWindow(this, classlist);
        popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    popWindow.dismiss();
                }
            }
        });
        ll_classchoose.setOnClickListener(this);
        if(dynamicList.size()>0){
            setWebView(0);
        }
    }
    @Override
    public void onError(String uri, String error) {
//        G.showToast(getActivity(),error);
    }

}
