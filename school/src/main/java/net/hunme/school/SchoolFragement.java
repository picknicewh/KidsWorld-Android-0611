package net.hunme.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.widget.NavigationBar;
import net.hunme.school.activity.WebViewActivity;

import butterknife.ButterKnife;

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
    private RelativeLayout rl_openclass;
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
    public static final String baseurl = "http://192.168.5.57:8080/space1/#/";
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
    public  static final String FOODLIST = "recipes";
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
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }
    private  void init(View v){
        navigationBar = $(v,R.id.nb_school);
        navigationBar.setTitle("园所");
        rl_openclass = $(v,R.id.rl_openclass);
        rl_check = $(v,R.id.rl_check);
        rl_leave = $(v,R.id.rl_leave);
        rl_inform = $(v,R.id.rl_info);
        rl_food = $(v,R.id.rl_food);
        rl_arrangement = $(v,R.id.rl_arrangement);
        rl_openclass.setOnClickListener(this);
        rl_check.setOnClickListener(this);
        rl_leave.setOnClickListener(this);
        rl_inform.setOnClickListener(this);
        rl_food.setOnClickListener(this);
        rl_arrangement.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId()==R.id.rl_openclass){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url", geturl(OPENCLASS));
            intent.putExtra("title","开放课堂");
        }else if (view.getId()==R.id.rl_check){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url", geturl(CHECK));
            intent.putExtra("title","选择班级");
        }else if (view.getId()==R.id.rl_leave){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url",geturl(LEAVE));
            intent.putExtra("title","请假");
            intent.putExtra("rightTitle","我要请假");
        }else if (view.getId()==R.id.rl_info){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url",geturl(INFORM));
            intent.putExtra("title","通知");
            intent.putExtra("rightTitle","发布通知");
        }else if (view.getId()==R.id.rl_food){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url",geturl(FOODLIST));
            intent.putExtra("title","食谱");
        }else if (view.getId()==R.id.rl_arrangement){
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url",geturl(ARRANGE));
            intent.putExtra("title","课程安排");
            intent.putExtra("rightTitle","发布");
        }
        startActivity(intent);
    }
    private String  geturl(String type){
        String url = null;
     switch (type){
         case OPENCLASS:url = baseurl+ OPENCLASS ;break;
         case CHECK:url = baseurl+CHECK;break;
         case LEAVE:url = baseurl+LEAVE;break;
         case INFORM:url = baseurl+INFORM;break;
         case FOODLIST:url = baseurl+FOODLIST;break;
         case ARRANGE:url = baseurl+ARRANGE;break;
     }
        return  url;
    }
}
