package net.hongzhang.status;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.database.StatusInfoDb;
import net.hongzhang.baselibrary.database.StatusInfoDbHelper;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshListView;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.status.adapter.StatusAdapter;
import net.hongzhang.status.mode.DynamicVo;
import net.hongzhang.status.mode.StatusVo;
import net.hongzhang.status.presenter.StatusContract;
import net.hongzhang.status.presenter.StatusPresenter;
import net.hongzhang.status.widget.ChooseClassPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/30
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusFragment extends BaseFragement implements View.OnClickListener, StatusContract.View, PullToRefreshBase.OnRefreshListener<ListView> {
    /**
     * 下拉刷新数据
     */
    private static final int RUSHTYPE = 2;
    /**
     * 上拉加载或者其他发布动态等刷新数据
     */
    private static final int OTHERTYPE = 1;
    /**
     * 发布说说按钮
     */
    private ImageView iv_right;
    /**
     * 班级的名字
     */
    private TextView tv_classname;
    /**
     * 头部班级
     */
    private LinearLayout ll_classchoose;
    /**
     * 下拉刷新控件
     */
    private PullToRefreshListView pullToRefreshListView;
    private ListView lv_status;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    /**
     * 没有网络
     */
    private RelativeLayout rl_nonetwork;
    /**
     * 导航栏
     */
    private RelativeLayout rl_toolbar;
    /**
     * 没有网络时头部显示
     */
    private TextView tv_status_bar;
    /**
     * 有新的评论消息小弹窗
     */
    private View layout_head;
    /**
     * 消息小弹窗的消息数
     */
    private TextView tv_head_count;
    /**
     * 消息小弹窗的头像
     */
    private ImageView iv_head_image;
    /**
     * 动态适配器
     */
    private StatusAdapter adapter;
    /**
     * 成员id
     */
    private String groupId;
    /**
     * 数据处理
     */
    private StatusPresenter presenter;
    /**
     * 选择班级列表的数据显示弹窗
     */
    private ChooseClassPopWindow classPopWindow;
    /**
     * 当前的空间类型（校园或者班级）
     */
    private String groupType;
    /**
     * 一页多少条数,默认
     */
    private int pageSize = 10;
    /**
     * 实际加载条数
     */
    private int loadPage = pageSize;
    /**
     * 第几页
     */
    private int pageNum = 1;
    /**
     * 刷新类型
     */
    private int type;
    /**
     * 班级列表位置
     */
    private int position;
    /**
     * 动态头部的列表数据
     */
    private List<DynamicVo> dynamicVoList;
    /**
     * 动态列表，定义为静态的为了在适配器中调用当前的数据列表，从而获取当前对于进入查看动态详情的数据位置
     */
    public static List<StatusVo> statusVos;
    /**
     * 记录从某个动态进入查看动态详情的位置
     */
    private int scrollPosition = 0;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 动态通知数据
     */
    private StatusInfoDbHelper dbHelper;
    private String tsId;
    /**
     * 是否还有更多的数据
     */
    private boolean hasData;
    /**
     * 是否已经点击过动态小窗口
     */
    private boolean isClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        iv_right = $(view, R.id.iv_right);
        tv_classname = $(view, R.id.tv_classname);
        ll_classchoose = $(view, R.id.ll_classchoose);
        tv_nodata = $(view, R.id.tv_nodata);
        rl_nonetwork = $(view, R.id.rl_nonetwork);
        tv_status_bar = $(view, R.id.tv_status_bar);
        rl_toolbar = $(view, R.id.rl_toolbar);
        iv_right.setOnClickListener(this);
        ll_classchoose.setOnClickListener(this);
        tv_status_bar.setOnClickListener(this);
        pullToRefreshListView = $(view, R.id.pullToRefreshListView);
        lv_status = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.doPullRefreshing(true, 500);
        initHeadView();
        initData();
    }

    /**
     * 初始化的评论消息小弹窗控件
     */
    private void initHeadView() {
        layout_head = LayoutInflater.from(getActivity()).inflate(R.layout.lv_status_head, null);
        LinearLayout ll_message = $(layout_head, R.id.ll_message);
        iv_head_image = $(layout_head, R.id.iv_head);
        tv_head_count = $(layout_head, R.id.tv_message_text);
        ll_message.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dynamicVoList = new ArrayList<>();
        statusVos = new ArrayList<>();
        tsId = UserMessage.getInstance(getActivity()).getTsId();
        setMessageInfo();
        presenter = new StatusPresenter(getActivity(), this, position);
        pullToRefreshListView.setLastUpdatedLabel(presenter.getLastUpdateTime());
        presenter.registerReceiver();
        adapter = new StatusAdapter(this, statusVos);
        lv_status.setAdapter(adapter);
    }

    /**
     * --->适配器调用
     * 设置当前点击进入详情页面的位置
     *
     * @param scrollPosition
     */
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    /**
     * ---->删除动态对话框调用，立即删除当前的选中的动态。并且刷新适配器
     *
     * @param position 当前删除的动态的位置
     */
    public void updateDelete(int position) {
        statusVos.remove(position);
        setListData();
    }

    /**
     * 刷新数据
     */
    private void setListData() {
        tv_nodata.setVisibility(statusVos.size() == 0 ? View.VISIBLE : View.GONE);
        if (adapter != null) {
            adapter.setData(statusVos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //用户发布动态成功 重新刷新数据
        if (G.KisTyep.isReleaseSuccess) {
            G.KisTyep.isReleaseSuccess = false;
            loadPage = pageSize;
            pageNum = 1;
            //如果动态列表有数据 调用刷新方法否者调用加载进行刷新数据
            if (statusVos.size() > 0) {
                type = RUSHTYPE;
                presenter.getDynamicList(groupId, groupType, pageSize, pageNum, type, statusVos.get(0).getDynamicId());
            } else {
                type = OTHERTYPE;
                presenter.getDynamicList(groupId, groupType, pageSize, pageNum, type, null);
            }
        }
        // 动态发生改变 刷新数据
        if (G.KisTyep.isUpdateComment) {
            type = OTHERTYPE;
            //页码从1开始所以pageNumber加一
            loadPage = 1;
            presenter.getDynamicList(groupId, groupType, 1, scrollPosition + 1, type, null);
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
            ImageCache.imageLoader(dbHelper.getLatestUrl(db, tsId), iv_head_image);
            tv_head_count.setText(dbHelper.getNoReadcount(db, tsId) + "条新消息");
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_right) {
            presenter.goPublishChoose(groupId, view);
        } else if (viewId == R.id.ll_classchoose) {
            if (dynamicVoList.size() > 0 && dynamicVoList != null) {
                presenter.showClassChoose(classPopWindow, rl_toolbar);
            }

        } else if (viewId == R.id.tv_status_bar) {
            presenter.goSettingNetWork();
        } else if (viewId == R.id.ll_message) {
            presenter.goMessageDetailActivity();
        }
    }

    /**
     * ---->班级选择弹窗调用
     * 设置当前空间选择的位置
     *
     * @param position 班级列表位置
     */
    public void setPosition(int position) {
        this.position = position;
        DynamicVo dynamicVo = dynamicVoList.get(position);
        setDynamicVo(dynamicVo);
    }

    /**
     * 设置当前的动态头部信息
     *
     * @param dynamicVo 动态头部信息
     */
    @Override
    public void setDynamicVo(DynamicVo dynamicVo) {
        groupId = dynamicVo.getGroupId();
        groupType = dynamicVo.getGroupType();
        tv_classname.setText(dynamicVo.getGroupName());
        type = OTHERTYPE;
        loadPage = pageSize;
        pageNum = 1;
        presenter.getDynamicList(groupId, groupType, pageSize, pageNum, type, null);
    }

    /**
     * 是否已经点击动态小弹窗
     *
     * @param isClickWindow 是的点击
     */
    @Override
    public void setIsClickWindow(boolean isClickWindow) {
        this.isClick = isClickWindow;
    }

    /**
     * 设置当前的动态头部列表信息
     *
     * @param dynamicVoList 动态头部列表
     */
    @Override
    public void setStatusHead(List<DynamicVo> dynamicVoList) {
        this.dynamicVoList = dynamicVoList;
        if (dynamicVoList != null && dynamicVoList.size() > 0) {
            classPopWindow = new ChooseClassPopWindow(this, dynamicVoList);
            DynamicVo dynamicVo = dynamicVoList.get(position);
            setDynamicVo(dynamicVo);
        }
    }

    /**
     * 设置当前的动态列表
     *
     * @param statusList 动态列表
     */
    @Override
    public void setStatusList(List<StatusVo> statusList) {
        if (statusList != null && statusList.size() > 0) {
            switch (type) {
                case OTHERTYPE:
                    //当loadPage=1时，表示进入动态详情页修改单条记录
                    if (loadPage == 1) {
                        hasData = true;
                        statusVos.set(scrollPosition, statusList.get(0));
                    } else {//当pageNum=1时表示第一页，第一页时清除所有的数据，重新加载新的第一页数据
                        if (pageNum == 1) {
                            statusVos.clear();
                        }
                        statusVos.addAll(statusList);
                        //如果当前返回的动态列表小于当前的页面加载数，那么说明没有更多的数据
                        hasData = statusList.size() < pageSize ? false : true;
                    }
                    break;
                case RUSHTYPE://动态刷新只需要加载刚刚没有的数据，并添加在列表最前面
                    hasData = true;
                    statusVos.addAll(0, statusList);
                    break;
            }
        } else {
            hasData = false;
        }
        setListData();
    }

    /**
     * 通过网络监听，监听当前是否网络做出响应的处理
     *
     * @param isVisible 是否网络
     */
    @Override
    public void setTvStatusbar(boolean isVisible) {
        tv_status_bar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置极光推送推送过来的班级id，与当前班级id对比，如果相同，则发送红点
     *
     * @param targetId 班级id
     */
    @Override
    public void setTargetId(String targetId) {
        presenter.sendStatusDosBroadcast(targetId, groupId);
    }

    /**
     * 设置极光推送推送过来的点赞或评论条数和最后一个点赞的人头像
     *
     * @param count    点赞或评论条数
     * @param imageUrl 当前的图片id
     */
    @Override
    public void setHeadInfo(int count, String imageUrl) {
        if (count > 0) {
            if (lv_status.getHeaderViewsCount() <= 0) {
                lv_status.addHeaderView(layout_head);
            }
            ImageCache.imageLoader(imageUrl, iv_head_image);
            tv_head_count.setText(count + "条新消息");
        }
    }

    /**
     * 广播接收我的动态删除的动态。并把当前动态的一起删除。
     */
    @Override
    public void setDeleteDynamic(String dynamicId) {
        for (int i = 0; i < statusVos.size(); i++) {
            if (statusVos.get(i).getDynamicId().equals(dynamicId)) {
                updateDelete(i);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unregisterReceiver();
    }

    /**
     * 下拉刷新动态数据
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                //下拉刷新
                Intent intent = new Intent(BroadcastConstant.MAINSTATUSDOS);
                intent.putExtra("count", 0);
                getActivity().sendBroadcast(intent);
                pageNum = 1;
                statusVos.clear();
                loadPage = pageSize;
                type = OTHERTYPE;
                presenter.getDynamicList(groupId, groupType, pageSize, pageNum, type, null);
                pullToRefreshListView.onPullDownRefreshComplete();
                pullToRefreshListView.setLastUpdatedLabel(presenter.getLastUpdateTime());
            }

        }.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 上拉加载动态数据
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                //上拉加载
                pageNum++;
                type = OTHERTYPE;
                loadPage = pageSize;
                presenter.getDynamicList(groupId, groupType, pageSize, pageNum, type, null);
                pullToRefreshListView.onPullUpRefreshComplete();
                pullToRefreshListView.setHasMoreData(hasData);
            }
        }.sendEmptyMessageDelayed(0, 500);
    }
}
