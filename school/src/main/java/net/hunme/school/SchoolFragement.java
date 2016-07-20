package net.hunme.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.widget.NavigationBar;

import net.hunme.school.activity.ArrangmentActivity;
import net.hunme.school.activity.CheckActivity;
import net.hunme.school.activity.FoodListActivity;
import net.hunme.school.activity.InformActivity;
import net.hunme.school.activity.LeaveActivity;
import net.hunme.school.activity.OpenClassActivity;
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
    private LinearLayout ll_openclass;
    /**
     *考勤
     */
    private LinearLayout ll_check;
    /**
     * 请假
     */
    private LinearLayout ll_leave;
    /**
     * 通知
     */
    private LinearLayout ll_inform;
    /**
     * 食谱
     */
    private LinearLayout ll_food;
    /**
     * 课程安排
     */
    private LinearLayout ll_arrangement;
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
        navigationBar.setTitle("学校");
        ll_openclass = $(v,R.id.ll_openclass);
        ll_check = $(v,R.id.ll_check);
        ll_leave = $(v,R.id.ll_leave);
        ll_inform = $(v,R.id.ll_info);
        ll_food = $(v,R.id.ll_food);
        ll_arrangement = $(v,R.id.ll_arrangement);
        ll_openclass.setOnClickListener(this);
        ll_check.setOnClickListener(this);
        ll_leave.setOnClickListener(this);
        ll_inform.setOnClickListener(this);
        ll_food.setOnClickListener(this);
        ll_arrangement.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId()==R.id.ll_openclass){
            intent.setClass(getActivity(), OpenClassActivity.class);
        }else if (view.getId()==R.id.ll_check){
            intent.setClass(getActivity(), CheckActivity.class);
        }else if (view.getId()==R.id.ll_leave){
            intent.setClass(getActivity(), LeaveActivity.class);
        }else if (view.getId()==R.id.ll_info){
            intent.setClass(getActivity(), InformActivity.class);
        }else if (view.getId()==R.id.ll_food){
            intent.setClass(getActivity(), FoodListActivity.class);
        }else if (view.getId()==R.id.ll_arrangement){
            intent.setClass(getActivity(), ArrangmentActivity.class);
        }
        startActivity(intent);
    }
}
