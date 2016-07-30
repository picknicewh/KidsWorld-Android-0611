package net.hunme.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MWebChromeClient;
import net.hunme.baselibrary.util.MWebViewClient;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.baselibrary.widget.MyViewView;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.UserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：动态首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragement extends BaseFragement implements View.OnClickListener{
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
    private MyViewView webView;
    /**
     * 加载动画
     */
    private LinearLayout ll_loading;
    private static  final  String url = "http://192.168.1.179:8787/web/kidsWorld/space/view/dynamic.html";
    /**
     * 班级选择
     */
    private LinearLayout ll_classchoose;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, null);
        initView(view);
        return view;
    }
    private void initView(View view) {
        iv_lift = $(view, R.id.iv_left);
        iv_right = $(view, R.id.iv_right);
        tv_classname = $(view, R.id.tv_classname);
        webView = $(view, R.id.wv_status);
        ll_loading = $(view, R.id.ll_loading);
        ll_classchoose = $(view,R.id.ll_classchoose);
        setWebView();
        setViewAction();
        classlist = new ArrayList<>();
        classlist.add("一(1)班");
        classlist.add("一(2)班");
        classlist.add("一(3)班");
        popWindow = new ChooseClassPopWindow(this, classlist);
   //    tv_classname.setOnClickListener(this);
        ll_classchoose.setOnClickListener(this);
    }

    private void  setWebView(){
        webView.addJavascriptInterface(this, "choose_class");  //设置本地调用对象及其接口
        webView.setWebViewClient(new MWebViewClient(webView,getActivity()));
        webView.setWebChromeClient(new MWebChromeClient(getActivity(),ll_loading,webView));
        webView.loadUrl(url);
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
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            G.initDisplaySize(getActivity());
            int width = (location[0]-popWindow.getWidth())/2;
            int height = G.dp2px(getActivity(),75)+popWindow.getHeight();
            popWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, -G.dp2px(getActivity(),180));
            popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWindow.dismiss();
                    }
                }
            });
        }
    }
}
