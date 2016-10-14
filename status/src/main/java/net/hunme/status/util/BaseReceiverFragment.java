package net.hunme.status.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.status.R;
import net.hunme.status.mode.DynamicVo;
import net.hunme.status.mode.StatusVo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BaseReceiverFragment extends BaseFragement implements OkHttpListener,View.OnClickListener{
    public MyJpushReceiver myReceiver;
    public TextView tv_status_bar;
    public  int mPosition;
    public List<DynamicVo> dynamicList;
    public RelativeLayout rl_nonetwork;
    public String groupId;
    public String groupType;
    public int pageNum=1;
    public   static String  createTime;
    public UserMessage um;
    public     LoadingDialog dialog;
    /**
     *网络状态监听
     */
    public ConnectionChangeReceiver connectionChangeReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        um=UserMessage.getInstance(getActivity());
        registerReceiver();
    }

    /**
     * 注册监听网络广播广播
     */
    private  void registerReceiver() {
        IntentFilter filter = new IntentFilter(MyJpushReceiver.SHOWSTAUSDOL);
        myReceiver = new MyJpushReceiver();
        getActivity().registerReceiver(myReceiver, filter);
        IntentFilter filter2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        getActivity().registerReceiver(connectionChangeReceiver, filter2);
    }
    /**
     * 获取动态列表
     * @param groupId 群id
     * @param  groupType 群类型 1=班级 2=学校
     * @param  mType   1是否第一次请求数据  = 1第一次 =2其他
     *                  2 怎么形式请求数据 1=加载，2=刷新
     */
    public void getDynamicList(String groupId,String groupType,int mType){
        if (mType ==1){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String  data = format.format(new Date());
            createTime = data;
        }
        if(G.isEmteny(um.getTsId())){
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", um.getTsId());
        map.put("groupId",groupId);
        map.put("groupType", groupType);
        map.put("pageNumber", pageNum);
        map.put("pageSize", 10);
        map.put("createTime", createTime);
        map.put("type", mType);
        Type type=new TypeToken<Result<List<StatusVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.STATUSLIST,map,this,2,"status");
        showLoadingDialog();
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
                String groupId = dynamicList.get(mPosition).getGroupId();
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
    /**
     * 有无网络加载页面状态
     */
    public   void  setShowView(){
        if (G.isNetworkConnected(getActivity())){
            rl_nonetwork.setVisibility(View.GONE);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);

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
    public void showLoadingDialog() {
        if(dialog==null)
            dialog=new LoadingDialog(getActivity(), R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    public void stopLoadingDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }

    }
    public String getGroupId(){
        return this.groupId;
    }
    public String getGroupType(){
        return this.groupType;
    }
    public void setDynamicList(List<DynamicVo> dynamicList) {
        this.dynamicList = dynamicList;
    }
    public void setTv_status_bar(TextView tv_status_bar) {
        this.tv_status_bar = tv_status_bar;
    }
    public void setRl_nonetwork(RelativeLayout rl_nonetwork) {
        this.rl_nonetwork = rl_nonetwork;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
