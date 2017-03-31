package net.hongzhang.status.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.status.activity.MessageDetailActivity;
import net.hongzhang.status.mode.DynamicVo;
import net.hongzhang.status.mode.StatusVo;
import net.hongzhang.status.widget.ChooseClassPopWindow;
import net.hongzhang.status.widget.StatusPublishPopWindow;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/30
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusPresenter implements StatusContract.Presenter, OkHttpListener {

    private Activity context;
    private StatusContract.View view;
    private UserMessage um;
    private ConnectionChangeReceiver connectionChangeReceiver;
    private MyJpushReceiver myReceiver;
    private int position;
    public StatusPresenter(Activity context, StatusContract.View view, int position) {
        this.context = context;
        this.view = view;
        this.position = position;
        G.initDisplaySize(context);
        um = UserMessage.getInstance(context);
        getDynamicHead();
    }


    @Override
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(BroadcastConstant.STATUSDOS);
        filter.addAction(BroadcastConstant.STATUSDOS);
        filter.addAction(BroadcastConstant.COMMENTINFO);
        myReceiver = new MyJpushReceiver();
        context.registerReceiver(myReceiver, filter);
        IntentFilter filter2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        context.registerReceiver(connectionChangeReceiver, filter2);
    }

    @Override
    public void unregisterReceiver() {
        if (myReceiver != null) {
            context.unregisterReceiver(myReceiver);
        }
        if (connectionChangeReceiver != null) {
            context.unregisterReceiver(connectionChangeReceiver);
        }
    }

    @Override
    public String getLastUpdateTime() {
        String text;
        long time = System.currentTimeMillis();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        if (0 == time) {
            text = "";
        } else {
            text = mDateFormat.format(new Date(time));
        }
        return text;
    }

    /**
     * 获取班级列表
     */
    public void getDynamicHead() {
        if (G.isEmteny(um.getTsId())) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", um.getTsId());
        Type type = new TypeToken<Result<List<DynamicVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.DYNAMICHEAD, map, this, 2, "DYNAMIC");
    }

    /**
     * 获取动态列表
     *
     * @param groupId   群id
     * @param groupType 群类型 1=班级 2=学校
     * @param type      1.加载 2 刷新
     */
    public void getDynamicList(String groupId, String groupType, int pageSize, int pageNum, int type, String dynamicId) {
        Map<String, Object> map = new HashMap<>();
        if (G.isEmteny(um.getTsId())) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = format.format(new Date());
        map.put("tsId", um.getTsId());
        map.put("groupId", groupId);
        map.put("groupType", groupType);
        map.put("pageNumber", pageNum);
        map.put("pageSize", pageSize);
        map.put("createTime", data);
        map.put("type", type);
        if (type == 2) {
            map.put("dynamicId", dynamicId);
        }
        Type mType = new TypeToken<Result<List<StatusVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.STATUSLIST, map, this, 2, "status");
        view.showLoadingDialog();
    }

    @Override
    public void goSettingNetWork() {
        Intent intent;
        // 先判断当前系统版本
        if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);
    }

    @Override
    public void goMessageDetailActivity() {
        //获取所以没阅读的通知时间，并且点击后把所有未阅读的数据标记为已读
        Intent intent = new Intent();
        intent.setClass(context, MessageDetailActivity.class);
        context.startActivity(intent);
        view.setIsClickWindow(true);
    }

    @Override
    public void goPublishChoose(String classId, View view) {
        StatusPublishPopWindow pubishPopWindow = new StatusPublishPopWindow(context, classId);
        pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showClassChoose(final ChooseClassPopWindow popWindow, RelativeLayout rl_toolbar) {
        int xPos = G.size.W / 2 - G.dp2px(context, 75);
        if (rl_toolbar != null) {
            popWindow.showAsDropDown(rl_toolbar, xPos, -G.dp2px(context, 10));
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

    @Override
    public void sendStatusDosBroadcast(String targetId, String groupId) {
        Intent myIntent = new Intent(BroadcastConstant.MAINSTATUSDOS);
        if (!G.isEmteny(groupId)) {
            if (groupId.equals(targetId)) {
                myIntent.putExtra("count", 1);
            } else {
                myIntent.putExtra("count", 0);
            }
        }
        context.sendBroadcast(myIntent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.DYNAMICHEAD.equals(uri)) {
            G.KisTyep.isChooseId = false;
            List<DynamicVo> dynamicList = ((Result<List<DynamicVo>>) date).getData();
            if (dynamicList != null && dynamicList.size() > 0) {
                view.setStatusHead(dynamicList);
                view.setDynamicVo(dynamicList.get(position));
            }
        } else if (Apiurl.STATUSLIST.equals(uri)) {
            Result<List<StatusVo>> data = (Result<List<StatusVo>>) date;
            List<StatusVo> statusVos = data.getData();
            if (statusVos != null && statusVos.size() > 0) {
                view.setStatusList(statusVos);
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        G.KisTyep.isChooseId = false;
    }

    /**
     * 有无网络监听广播
     */
    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                view.setTvStatusbar(true);
            } else {
                view.setTvStatusbar(false);
            }
        }
    }
    /**
     * 极光推送推送
     */
    class MyJpushReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals(BroadcastConstant.STATUSDOS)) {
                String targetId = bundle.getString("targetId");
                view.setTargetId(targetId);
            } else if (action.equals(BroadcastConstant.COMMENTINFO)) {
                int count = bundle.getInt("count", 0);
                String imageUrl = bundle.getString("imageUrl");
                view.setHeadInfo(count, imageUrl);
            }
        }
    }
}
