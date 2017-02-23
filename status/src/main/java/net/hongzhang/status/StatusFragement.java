package net.hongzhang.status;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import net.hongzhang.baselibrary.cordova.CordovaInterfaceImpl;
import net.hongzhang.baselibrary.database.StatusInfoDb;
import net.hongzhang.baselibrary.database.StatusInfoDbHelper;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.listview.PullToRefreshLayout;
import net.hongzhang.baselibrary.widget.listview.PullableListView;
import net.hongzhang.status.activity.MessageDetailActivity;
import net.hongzhang.status.adapter.StatusAdapter;
import net.hongzhang.status.mode.DynamicVo;
import net.hongzhang.status.mode.StatusVo;
import net.hongzhang.status.util.BaseReceiverFragment;
import net.hongzhang.status.widget.ChooseClassPopWindow;
import net.hongzhang.status.widget.StatusPublishPopWindow;

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
    private static final int PAGESIZE = 20;
    /**
     * 动态列表
     */
    public PullableListView lv_status;
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
    private List<String> classlist;
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
    public List<StatusVo> statusVoList;
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
     * 是否点击过未读新的消息
     */
    private boolean isClick;
    /**
     * 点击评论进入详情页面的位置
     */
    private int scrollPosition = 0;
    /**
     * 加载的页码数
     */
    private int loadpage;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 动态通知数据
     */
    private StatusInfoDbHelper dbHelper;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    /**
     * type = 1加载 type=2 刷新
     */
    private int type;
    private String tsId;
    private String groupId;

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
        ll_classchoose = $(view, R.id.ll_classchoose);
        rl_toolbar = $(view, R.id.rl_toolbar);
        tv_status_bar = $(view, R.id.tv_status_bar);
        rl_nonetwork = $(view, R.id.rl_nonetwork);
        lv_status = $(view, R.id.lv_status);
        refresh_view = $(view, R.id.refresh_view);
        tv_nodata = $(view, R.id.tv_nodata);
        ll_classchoose.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
        iv_right.setOnClickListener(this);
        rl_nonetwork.setOnClickListener(this);
        tv_status_bar.setOnClickListener(this);
        classlist = new ArrayList<>();
        statusVoList = new ArrayList<>();
        tsId = UserMessage.getInstance(getActivity()).getTsId();
        G.initDisplaySize(getActivity());
        setListViewHead();
        getDynamicHead();
        setLayout_head(layout_head);
        setLv_status(lv_status);
        setIv_head(iv_head);
        setTv_message(tv_message);
        setTv_status_bar(tv_status_bar);
        setDynamicList(dynamicList);
        setRl_nonetwork(rl_nonetwork);
        setMessageInfo();
        adapter = new StatusAdapter(this, statusVoList);
        lv_status.setAdapter(adapter);
    }

    /**
     * 设置ListView头部的listview
     */
    private void setListViewHead() {
        layout_head = LayoutInflater.from(getActivity()).inflate(R.layout.lv_status_head, null);
        LinearLayout ll_message_infor = (LinearLayout) layout_head.findViewById(R.id.ll_message);
        iv_head = $(ll_message_infor, R.id.iv_head);
        tv_message = $(ll_message_infor, R.id.tv_message_text);
        ll_message_infor.setOnClickListener(this);
    }

    /**
     * popwindow调用
     *
     * @param position 选择班级的位置
     */
    public void setPosition(int position) {
        this.position = position;
        groupId = dynamicList.get(position).getGroupId();
        statusVoList.clear();
        loadDDynamicList(position, PAGESIZE, 1, 1, null);
    }

    /**
     * 获取动态列表
     *
     * @param position   班级列表位置
     * @param pageSize   一页加载多少条数据
     * @param pageNumber 加载第几页第几页
     */
    private void loadDDynamicList(int position, int pageSize, int pageNumber, int type, String dynamicId) {
        this.type = type;
        loadpage = pageSize;
        if (dynamicList != null && dynamicList.size() > 0) {
            groupId = dynamicList.get(position).getGroupId();
            groupType = dynamicList.get(position).getGroupType();
            if (!G.isEmteny(groupId) && !G.isEmteny(groupType)) {
                getDynamicList(groupId, groupType, pageSize, pageNumber, type, dynamicId);
            }
        }
    }

    /**
     * 设置班级的姓名
     *
     * @param classname 班级姓名
     *                  共外部popwindow使用
     */
    public void setClassname(String classname) {
        tv_classname.setText(classname);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_classchoose) {
            int xPos = G.size.W / 2 - G.dp2px(getActivity(), 75);
            if (rl_toolbar != null) {
                popWindow.showAsDropDown(rl_toolbar, xPos, -G.dp2px(getActivity(), 10));
                popWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            popWindow.dismiss();
                        }
                    }
                });
            }
        } else if (viewId == R.id.tv_status_bar) {
            Intent intent;
            // 先判断当前系统版本
            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            }
            getActivity().startActivity(intent);
        } else if (viewId == R.id.iv_right) {
            final StatusPublishPopWindow pubishPopWindow = new StatusPublishPopWindow(getActivity(), groupId);
            pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else if (viewId == R.id.ll_message) {
            //获取所以没阅读的通知时间，并且点击后把所有未阅读的数据标记为已读
            Intent intent = new Intent();
            intent.setClass(getActivity(), MessageDetailActivity.class);
            getActivity().startActivity(intent);
            isClick = true;
        }
    }

    /**
     * 点击前获取数据
     */
    public void setMessageInfo() {
        //设置初始状态
        db = new StatusInfoDb(getActivity()).getWritableDatabase();
        dbHelper = StatusInfoDbHelper.getInstance();
        if (dbHelper.getNoReadcount(db, tsId) > 0) {
            if (lv_status.getHeaderViewsCount() == 0) {
                lv_status.addHeaderView(layout_head);
            }
            ImageCache.imageLoader(dbHelper.getLatestUrl(db, tsId), iv_head);
            tv_message.setText(dbHelper.getNoReadcount(db, tsId) + "条新消息");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //用户发布动态成功 重新刷新数据
        if (G.KisTyep.isReleaseSuccess) {
            G.KisTyep.isReleaseSuccess = false;
            //如果动态列表有数据 调用刷新方法否者调用加载进行刷新数据
            if (statusVoList.size() > 0) {
                loadDDynamicList(position, PAGESIZE, 1, 2, statusVoList.get(0).getDynamicId());
            }else {
                loadDDynamicList(position, PAGESIZE, pageNum, 1, "");
            }
        }
        // 动态发生改变 刷新数据
        if (G.KisTyep.isUpdateComment) {
            //页码从1开始所以pageNumber加一
            loadDDynamicList(position, 1, scrollPosition + 1, 1, null);
            G.KisTyep.isUpdateComment = false;
        }
        if (isClick) {
            lv_status.removeHeaderView(layout_head);
        }
        //设置消息通知状态，评论完了要重新获取通知栏的消息
        if (dbHelper.getNoReadcount(db, tsId) > 0) {
            lv_status.removeHeaderView(layout_head);
            setMessageInfo();
        }

    }

    /**
     * 获取班级列表
     */
    private void getDynamicHead() {
        if (G.isEmteny(um.getTsId())) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", um.getTsId());
        Type type = new TypeToken<Result<List<DynamicVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.DYNAMICHEAD, map, this, 2, "DYNAMIC");
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (Apiurl.DYNAMICHEAD.equals(uri)) {
            classlist.clear();
            G.KisTyep.isChooseId = false;
            dynamicList = ((Result<List<DynamicVo>>) date).getData();
            if (dynamicList != null && dynamicList.size() > 0) {
                for (DynamicVo d : dynamicList) {
                    classlist.add(d.getGroupName());
                }
                popWindow = new ChooseClassPopWindow(this, classlist);
                loadDDynamicList(position, PAGESIZE, pageNum, 1, null);
                tv_classname.setText(classlist.get(0));
                CLASSID = dynamicList.get(0).getGroupId();
            }
        } else if (Apiurl.STATUSLIST.equals(uri)) {
            stopLoadingDialog();
            Result<List<StatusVo>> data = (Result<List<StatusVo>>) date;
            List<StatusVo> statusVos = data.getData();
            if (statusVos != null && statusVos.size() > 0) {
                if (type == 1) {//上拉加载或者下拉刷新或者改变数据
                    //在详情页评论或点赞了动态情况
                    if (loadpage == 1) {
                        if (statusVoList.size() > 0) {
                            statusVoList.set(scrollPosition, statusVos.get(0));
                        }
                    } else {
                        if (pageNum==1){//上拉加载
                            statusVoList.clear();
                            statusVoList.addAll(statusVos);
                        }else {//下拉刷新
                            statusVoList.addAll(statusVos);
                        }
                    }
                } else if (type == 2) {//下拉刷新动态
                    statusVoList.addAll(0, statusVos);
                }
            } else {
                if (pageNum == 1 && type == 1) {
                    statusVoList.clear(); //上拉刷新 数据清空
                }else {
                    pageNum--;
                    if (pageNum <= 1) pageNum = 1;
                }
            }
            setListData();
        }
    }

    private void setListData() {
        if (statusVoList.size() == 0) {
            tv_nodata.setVisibility(View.VISIBLE);
        } else {
            tv_nodata.setVisibility(View.GONE);
        }
        if (adapter != null) {
            adapter.setData(statusVoList);
            adapter.notifyDataSetChanged();
        }
        refresh_view.setLv_count(statusVoList.size());
    }

    public void updateDelete(int position) {
        statusVoList.remove(position);
        setListData();
    }

    /**
     * 设置当前点击进入详情页面的位置
     *
     * @param scrollPosition
     */
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        G.KisTyep.isChooseId = false;

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
                //statusVoList.clear();
                //下拉刷新
                Intent intent = new Intent(BroadcastConstant.MAINSTATUSDOS);
                intent.putExtra("count", 0);
                getActivity().sendBroadcast(intent);
                //如果没有动态数据 下拉刷新直接调用上拉加载方法进行刷新数据
                pageNum = 1;
              /*  if (statusVoList != null && statusVoList.size() > 0) {
                    loadDDynamicList(position, PAGESIZE, pageNum, 2, statusVoList.get(0).getDynamicId());
                }*/
//                if (statusVoList != null && statusVoList.size() > 0) {
//
//                } else {
                   loadDDynamicList(position, PAGESIZE, pageNum, 1, "");
//                }
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                //上拉加载
                pageNum++;
                loadDDynamicList(position, PAGESIZE, pageNum, 1, null);
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 500);
    }
}
