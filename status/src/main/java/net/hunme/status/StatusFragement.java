package net.hunme.status;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.cordova.CordovaInterfaceImpl;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.widget.listview.PullToRefreshLayout;
import net.hunme.baselibrary.widget.listview.PullableListView;
import net.hunme.status.activity.MessageDetailActivity;
import net.hunme.status.adapter.StatusAdapter;
import net.hunme.status.mode.DynamicVo;
import net.hunme.status.mode.StatusVo;
import net.hunme.status.util.BaseReceiverFragment;
import net.hunme.status.widget.ChooseClassPopWindow;
import net.hunme.status.widget.StatusPublishPopWindow;

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
public class StatusFragement extends BaseReceiverFragment implements PullToRefreshLayout.OnRefreshListener {
    /**
     * 动态列表
     */
    private PullableListView lv_status;
    private PullToRefreshLayout refresh_view;
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
     * 断网提示
     */
    private TextView tv_status_bar;
    /**
     * 班级选择
     */
    private LinearLayout ll_classchoose;

    /**
     * 班级列表对象
     */
    private List<DynamicVo> dynamicList;
    /**
     * 导航栏
     */
    private RelativeLayout rl_toolbar;
    /**
     * ListView头部
     */
    private View layout_head;

    /**
     * 班级的id号
     */
    public static String CLASSID;
    private RelativeLayout rl_nonetwork;
    /**
     * 当前选中列表位置
     */
    private int position = 0;
    /**
     * 动态列表选项
     */
    private static List<StatusVo> statusVoList;
    /**
     * 适配器
     */
    private StatusAdapter adapter;
    /**
     * 未读消息的头像
     */
    private ImageView iv_head;
    /**
     * 未读消息条数
     */
    private TextView tv_message;
    /**
     * 获取数据类型 =1时加载=2时下拉刷新
     */
    private int type=1;
    /**
     * 是否点击过未读新的消息
     */
    private boolean isClick ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater localInflater = inflater.cloneInContext(new CordovaInterfaceImpl(getActivity(), this));
        View view = localInflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_right = $(view, R.id.iv_right);
        tv_classname = $(view, R.id.tv_classname);
        ll_classchoose = $(view,R.id.ll_classchoose);
        rl_toolbar=$(view,R.id.rl_toolbar);
        tv_status_bar = $(view,R.id.tv_status_bar);
        rl_nonetwork= $(view,R.id.rl_nonetwork);
        lv_status = $(view,R.id.lv_status);
        refresh_view = $(view,R.id.refresh_view);
        iv_head = $(view,R.id.iv_head);
        tv_message = $(view,R.id.tv_message_text);
        classlist = new ArrayList<>();
        statusVoList = new ArrayList<>();
        setListViewHead();
        setmPosition(position);
        getDynamicHead();
        setTv_status_bar(tv_status_bar);
        setDynamicList(dynamicList);
        setRl_nonetwork(rl_nonetwork);
        ll_classchoose.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
        G.initDisplaySize(getActivity());
        iv_right.setOnClickListener(this);
        rl_nonetwork.setOnClickListener(this);
        tv_status_bar.setOnClickListener(this);
        adapter = new StatusAdapter(this,statusVoList);
        lv_status.setAdapter(adapter);
    }
    /**
     * 设置ListView头部的listview
     */
    private void setListViewHead(){
        layout_head = LayoutInflater.from(getActivity()).inflate(R.layout.lv_status_head, null);
        LinearLayout  ll_message_infor= (LinearLayout) layout_head.findViewById(R.id.ll_message);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = G.dp2px(getActivity(),20);
        params.setMargins(margin,margin,margin,margin);
        params.gravity = Gravity.CENTER;
        ll_message_infor.setLayoutParams(params);
        lv_status.addHeaderView(layout_head);
        ll_message_infor.setOnClickListener(this);
    }
    /**
     * popwindow调用
     * @param position  选择班级的位置
     */
    public void setPosition(int position){
        this.position = position;
        setmPosition(position);
        loadDDynamicList(position,1);
    }
    /**
     * 获取动态列表
     * @param position 班级列表位置
     * @param  mType 获取数据类型 =1时加载=2时下拉刷新
     */
    private void loadDDynamicList(int position,int mType){
        type=mType;
        groupId = dynamicList.get(position).getGroupId();
        groupType = dynamicList.get(position).getGroupType();
        if (mType==2){
          //  dynamicId = statusVoList.get(0).getDynamicId();
            if (!G.isEmteny(groupId)&&!G.isEmteny(groupType)&&!G.isEmteny(dynamicId)){
                getDynamicList(groupId,groupType,mType,dynamicId);
            }
        }else if (mType==1){
            if (!G.isEmteny(groupId)&&!G.isEmteny(groupType)){
                getDynamicList(groupId,groupType,mType,dynamicId);
            }
        }

    }
    /**
     * 设置班级的姓名
     * @param classname 班级姓名
     * 共外部popwindow使用
     */
    public void setClassname(String classname){
       tv_classname.setText(classname);
   }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.ll_classchoose) {
            int xPos = G.size.W / 2 - G.dp2px(getActivity(), 75);
            popWindow.showAsDropDown(rl_toolbar, xPos, -G.dp2px(getActivity(), 10));
            popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWindow.dismiss();
                    }
                }
            });
        }else if (viewId==R.id.tv_status_bar){
            Intent intent ;
            // 先判断当前系统版本
            if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            }else{
                intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            }
            getActivity().startActivity(intent);
        }else if (viewId==R.id.iv_right){
            final StatusPublishPopWindow pubishPopWindow = new StatusPublishPopWindow(getActivity());
            pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }else if (viewId==R.id.ll_message){
            Intent intent = new Intent();
            intent.setClass(getActivity(), MessageDetailActivity.class);
            getActivity().startActivity(intent);
            isClick = true;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //用户发布动态成功 重新刷新数据
        if(G.KisTyep.isReleaseSuccess) {
            G.KisTyep.isReleaseSuccess = false;
            loadDDynamicList(position,1);
        }
        // 动态发生改变 刷新数据
        if(G.KisTyep.isUpdateComment){
            loadDDynamicList(position,2);
            G.KisTyep.isUpdateComment=false;
        }
        if (isClick){
            lv_status.removeHeaderView(layout_head);
        }
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
        OkHttps.sendPost(type, Apiurl.DYNAMICHEAD,map,this,2,"DYNAMIC");
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if(Apiurl.DYNAMICHEAD.equals(uri)){
            classlist.clear();
            G.KisTyep.isChooseId=false;
            dynamicList = ((Result<List<DynamicVo>>) date).getData();
            for (DynamicVo d: dynamicList){
                classlist.add(d.getGroupName());
            }
             popWindow = new ChooseClassPopWindow(this, classlist);
             loadDDynamicList(position,1);
             if(dynamicList.size()>0){
                tv_classname.setText(classlist.get(0));
                CLASSID=dynamicList.get(0).getGroupId();
            }
         }else if (Apiurl.STATUSLIST.equals(uri)){
            stopLoadingDialog();
            Result<List<StatusVo>> data = (Result<List<StatusVo>>) date;
            List<StatusVo> statusVos = data.getData();
            if (statusVos!=null&& statusVos.size()>0){
                dynamicId = statusVos.get(0).getDynamicId();
                if (type==1){
                    statusVoList.addAll(statusVos);
                }else if (type==2){
                    statusVoList.addAll(0,statusVos);
                }
            }else {
              /*  if (type==2&&dynamicId==statusVoList.get(0).getDynamicId()){
                    loadDDynamicList(position,1);
                }*/
            }
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        G.KisTyep.isChooseId=false;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(myReceiver);
        getActivity().unregisterReceiver(connectionChangeReceiver);
    }
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
               // statusVoList.clear();
                pageNum=1;
                loadDDynamicList(position,1);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        }.sendEmptyMessageDelayed(0,1000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                pageNum++;
                loadDDynamicList(position,1);
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0,1000);
    }
}
