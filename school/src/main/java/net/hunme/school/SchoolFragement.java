package net.hunme.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.network.ServerConfigManager;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.NavigationBar;
import net.hunme.school.activity.PublishActivity;
import net.hunme.school.activity.WebViewActivity;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：学校首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SchoolFragement extends BaseFragement implements View.OnClickListener{
    /**
     * 导航栏
     */
    private NavigationBar navigationBar;
    /**
     * 开放课堂
     */
 //   private RelativeLayout rl_openclass;
    /**
     *考勤
     */
    private RelativeLayout rl_check;
    /**
     * 请假
     */
    private RelativeLayout rl_leave;
    /**
     * 通知
     */
    private RelativeLayout rl_inform;
    /**
     * 食谱
     */
    private RelativeLayout rl_food;
    /**
     * 课程安排
     */
    private RelativeLayout rl_arrangement;
    /**
     * 基本url
     */
    public static final String baseurl = ServerConfigManager.WEB_IP+"/school/index.html?";
    /**
     * 考勤
     */
    public  static final String CHECK = "checkOnclass";
    /**
     * 课程安排
     */
    public  static final String ARRANGE = "courseArr";
    /**
     * 食谱
     */
    public  static final String FOODLIST = "foodList";
    /**
     * 通知
     */
    public  static final String INFORM = "noticeRead";
    /**
     * 请假
     */
    public  static final String LEAVE = "askForlv";
    /**
     * 开放课堂
     */
    public  static final String OPENCLASS = "openClass";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, null);
        init(view);
        return view;
    }
    private void init(View v){
        navigationBar = $(v,R.id.nb_school);
        navigationBar.setTitle("园所");
      //  rl_openclass = $(v,R.id.rl_openclass);
        rl_check = $(v,R.id.rl_check);
        rl_leave = $(v,R.id.rl_leave);
        rl_inform = $(v,R.id.rl_info);
        rl_food = $(v,R.id.rl_food);
        rl_arrangement = $(v,R.id.rl_arrangement);
     //   rl_openclass.setOnClickListener(this);
        rl_check.setOnClickListener(this);
        rl_leave.setOnClickListener(this);
        rl_inform.setOnClickListener(this);
        rl_food.setOnClickListener(this);
        rl_arrangement.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
         /*
        if (view.getId()==R.id.rl_openclass){
        *//*    intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url", geturl(OPENCLASS));
            intent.putExtra("title","开放课堂");*//*
            Toast.makeText(getActivity(),"暂未开通此功能！",Toast.LENGTH_SHORT).show();
        }else*/ if (view.getId()==R.id.rl_check){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("loadUrl", geturl(CHECK));
            intent.putExtra("title","选择班级");
        }else if (view.getId()==R.id.rl_leave){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("loadUrl",geturl(LEAVE));
            intent.putExtra("title","请假");
            intent.putExtra("subTitle","我要请假");
        }else if (view.getId()==R.id.rl_info){
            intent.setClass(getActivity(), PublishActivity.class);
        }else if (view.getId()==R.id.rl_food){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("loadUrl",geturl(FOODLIST));
            intent.putExtra("title","食谱");
        }else if (view.getId()==R.id.rl_arrangement){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("loadUrl",geturl(ARRANGE));
            intent.putExtra("title","课程安排");
            intent.putExtra("subTitle","发布");
        }
        startActivity(intent);

    }
    private String  geturl(String type){

        String url = baseurl+"TsId="+ UserMessage.getInstance(getActivity()).getTsId()+"#/";
     switch (type){
         case OPENCLASS:url =url+OPENCLASS ;break;
         case CHECK:url = url+CHECK;break;
         case LEAVE:url = url+LEAVE;break;
         case INFORM:url = url+INFORM;break;
         case FOODLIST:url = url+FOODLIST;break;
         case ARRANGE:url = url+ARRANGE;break;
     }
        return  url;
    }
}
