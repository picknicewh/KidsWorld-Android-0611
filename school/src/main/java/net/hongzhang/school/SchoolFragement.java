package net.hongzhang.school;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.network.ServerConfigManager;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.activity.ClassListActivity;
import net.hongzhang.school.activity.CourseArrangeActivity;
import net.hongzhang.school.activity.FoodListActivity;
import net.hongzhang.school.activity.LeaveListActivity;
import net.hongzhang.school.activity.MedicineListSActivity;
import net.hongzhang.school.activity.MedicineListTActivity;
import net.hongzhang.school.activity.PublishActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：学校首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SchoolFragement extends BaseFragement implements View.OnClickListener{

    /**
     *考勤
     */
    private RelativeLayout rl_check;
    /**
     * 请假
     */
    private RelativeLayout rl_leave;
    private TextView tv_dos_leaveadk;
    /**
     * 通知
     */
    private RelativeLayout rl_inform;
    private TextView  tv_dos_info;
    /**
     * 食谱
     */
    private RelativeLayout rl_food;
    /**
     * 课程安排
     */
    private RelativeLayout rl_arrangement;
    /**
     * 开放课堂
     */
    private RelativeLayout rl_openClass;
    /**
     * 喂药
     */
    private RelativeLayout rl_medicine;
    private TextView  tv_dos_medicine;

    private ShowDosRecivier recivier;
    /**
     * 基本url
     */
    public static final String baseurl = ServerConfigManager.WEB_IP+"/medicine/index.html?";
    public static final String MEDCINERS = "medicineListT";
    public static final String MEDCINERP = "medicineListP";
    private   static  boolean isVisible = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, null);
        init(view);
        return view;
    }
    private void init(View v){

        rl_check = $(v,R.id.rl_check);
        rl_openClass=$(v,R.id.rl_openClass);
        rl_leave = $(v,R.id.rl_leave);
        rl_inform = $(v,R.id.rl_info);
        rl_food = $(v,R.id.rl_food);
        rl_arrangement = $(v,R.id.rl_arrangement);
        rl_medicine = $(v,R.id.rl_medicine);
        tv_dos_info = $(v,R.id.tv_dos_info);
        tv_dos_leaveadk=$(v,R.id.tv_dos_leaveadk);
        tv_dos_medicine = $(v,R.id.tv_dos_medicine);
        rl_check.setOnClickListener(this);
        rl_leave.setOnClickListener(this);
        rl_inform.setOnClickListener(this);
        rl_food.setOnClickListener(this);
        rl_arrangement.setOnClickListener(this);
        rl_openClass.setOnClickListener(this);
        rl_medicine.setOnClickListener(this);
        registerReceiver();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId()==R.id.rl_check){
            intent.setClass(getActivity(), TestActivity.class);
        }else if (view.getId()==R.id.rl_leave){
            intent.setClass(getActivity(), LeaveListActivity.class);
            schoolDosDisappear(BroadcastConstant.LEAVEASEKDOS);

        }else if (view.getId()==R.id.rl_info){
            intent.setClass(getActivity(), PublishActivity.class);
            schoolDosDisappear(BroadcastConstant.SCHOOLINFODOS);
        }else if (view.getId()==R.id.rl_food){
            intent.setClass(getActivity(), FoodListActivity.class);
        }else if (view.getId()==R.id.rl_arrangement){
            intent.setClass(getActivity(), CourseArrangeActivity.class);
        }else if(view.getId()==R.id.rl_openClass){
            intent.setClass(getActivity(), ClassListActivity.class);
        }else if (view.getId()==R.id.rl_medicine){
           //  intent.setClass(getActivity(), net.hunme.school.activity.TestActivity.class);
       /*     intent.setClass(getActivity(), HMDroidGap.class);
            intent.putExtra("loadUrl",geturl());
            schoolDosDisappear(BroadcastConstant.MEDICINEDOS);
           */
            if (UserMessage.getInstance(getActivity()).getType().equals("1")){
                intent.setClass(getActivity(), MedicineListSActivity.class);
            }else {
                intent.setClass(getActivity(), MedicineListTActivity.class);
                //"http://192.168.1.171:8787/KidsWorld-Web
                schoolDosDisappear(BroadcastConstant.MEDICINEDOS);
            }
        }
        startActivity(intent);
    }
    /**
     *学校红点消失
     * @param  action
     */
    private void schoolDosDisappear(String action){
        Intent myintent = new Intent(action);
        myintent.putExtra("isVisible",false);
        myintent.putExtra("tsId",UserMessage.getInstance(getActivity()).getTsId());
        getActivity().sendBroadcast(myintent);
        showSchoolDos(0);
    }
    private void registerReceiver(){
        IntentFilter leave = new IntentFilter(BroadcastConstant.LEAVEASEKDOS);
        IntentFilter medicine = new IntentFilter(BroadcastConstant.MEDICINEDOS);
        IntentFilter info = new IntentFilter(BroadcastConstant.SCHOOLINFODOS);
        recivier = new ShowDosRecivier();
        getActivity().registerReceiver(recivier,leave);
        getActivity().registerReceiver(recivier,medicine);
        getActivity().registerReceiver(recivier,info);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recivier!=null){
            getActivity().unregisterReceiver(recivier);
        }
    }
    private List<String> tsids = new ArrayList<>();
    private class ShowDosRecivier extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            String tsid   =bundle.getString("tsId");
             isVisible = bundle.getBoolean("isVisible",false);

            if (action.equals(BroadcastConstant.LEAVEASEKDOS)){//请假
                ShowDos(isVisible,tsid,tv_dos_leaveadk);
            }else if (action.equals(BroadcastConstant.MEDICINEDOS)){//喂药
                ShowDos(isVisible,tsid,tv_dos_medicine);
            }else if (action.equals(BroadcastConstant.SCHOOLINFODOS)){//   通知
                ShowDos(isVisible,tsid,tv_dos_info);
            }

        }
    }
    private void ShowDos( boolean isVisible, String tsid,TextView textView){
        tsids.add(tsid);
      //  Log.i("ssssss",tsid+"============="+UserMessage.getInstance(getActivity()).getTsId());
        for (int i = 0 ;i<tsids.size();i++){
            if (tsids.get(i).equals(UserMessage.getInstance(getActivity()).getTsId())&& isVisible){
                textView.setVisibility(View.VISIBLE);
                showSchoolDos(1);
                return;
            }else {
                textView.setVisibility(View.GONE);
            }
        }
        tsids.clear();
    }
    /**
     * 只要接收到任意一个红点通知发送学校底部的通知红点点广播
     * @param count
     */
    private void showSchoolDos(int count){
        Intent myIntent = new Intent(BroadcastConstant.MAINSCHOOLDOS);
        myIntent.putExtra("count",count);
        getActivity().sendBroadcast(myIntent);
    }
    private String  geturl(){
        String url =baseurl+"tsId="+ UserMessage.getInstance(getActivity()).getTsId()+"#/";
        if (UserMessage.getInstance(getActivity()).getType().equals("1")){
          url = url+MEDCINERP;
        }else {
            url = url+MEDCINERS;
        }
        return  url;
    }
}
