package net.hongzhang.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshListView;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.MyDynamicAdapter;
import net.hongzhang.user.mode.DynamicInfoVo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDynamicActivity extends BaseActivity implements OkHttpListener, View.OnClickListener, PullToRefreshBase.OnRefreshListener<ListView> {
    // private PullToRefreshLayout refresh_view;
    /**
     * 我的动态列表
     */
    //  private PullableListView lv_dynamic;
    private ListView lv_dynamic;
    private PullToRefreshListView pullToRefreshListView;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 列表信息
     */
    public List<DynamicInfoVo> dynamicInfoVoList;
    public MyDynamicAdapter adapter = null;
    /**
     * 页码数
     */
    private int pagesize;
    /**
     * 点击进入评论详情的位置
     */
    private int scrollPosition = 0;
    /**
     * 断网显示
     */
    private RelativeLayout rl_nonetwork;
    /**
     * 没有数据提示
     */
    private TextView tv_nodata;
    private boolean hasData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        initview();
    }

    private void initview() {
        //  lv_dynamic = $(R.id.lv_dynamic);
        dynamicInfoVoList = new ArrayList<>();
        rl_nonetwork = $(R.id.rl_nonetwork);
        tv_nodata = $(R.id.tv_nodata);
        pullToRefreshListView = $(R.id.pulllistview);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        lv_dynamic = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(this);
        rl_nonetwork.setOnClickListener(this);
        getMyDynamic(pageNumber, 10);
        adapter = new MyDynamicAdapter(this, dynamicInfoVoList, pageNumber);
        lv_dynamic.setAdapter(adapter);
        setLastUpdateTime();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的动态");
    }

    public void getMyDynamic(int pageNumber, int pagesize) {
        this.pagesize = pagesize;
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pagesize);
        Type type = new TypeToken<Result<List<DynamicInfoVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MYDYNAMICS, params, this, 2, "MYDYNAMICDATA");
        if (!G.isNetworkConnected(this)) {
            rl_nonetwork.setVisibility(View.VISIBLE);
        } else {
            rl_nonetwork.setVisibility(View.GONE);
            showLoadingDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 动态发生改变 刷新数据
        if (G.KisTyep.isUpdateComment) {
            getMyDynamic(scrollPosition + 1, 1);
            G.KisTyep.isUpdateComment = false;
        }
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    @Override
    public void onSuccess(String uri, Object date) {
        stopLoadingDialog();
        if (uri.equals(Apiurl.MYDYNAMICS)) {
            Result<String> data = (Result<String>) date;
            if (data != null) {
                List<DynamicInfoVo> dynamicInfoVos = ((Result<List<DynamicInfoVo>>) date).getData();
                if (dynamicInfoVos.size() > 0) {

                    if (pagesize == 1) {
                        dynamicInfoVoList.set(scrollPosition, dynamicInfoVos.get(0));
                    } else {
                        dynamicInfoVoList.addAll(dynamicInfoVos);
                    }
                }
                if (dynamicInfoVos.size() < 10) {
                    hasData = false;
                } else {
                    hasData = true;
                }
                setListData();
            } else {
                hasData = false;
            }
        }
    }

    @Override
    public void onError(String uri, Result error) {
        stopLoadingDialog();
        rl_nonetwork.setVisibility(View.VISIBLE);
        DetaiCodeUtil.errorDetail(error, this);
    }


    private void setListData() {
        if (dynamicInfoVoList.size() == 0) {
            tv_nodata.setVisibility(View.VISIBLE);
        } else {
            tv_nodata.setVisibility(View.GONE);
            adapter.setData(dynamicInfoVoList);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        //  refresh_view.setLv_count(dynamicInfoVoList.size());
    }

    public void updateDelete(int position) {
        //删除动态后要发送通知给
        Intent intent = new Intent(BroadcastConstant.DELETEDYNAMIC);
        intent.putExtra("dynamicId", dynamicInfoVoList.get(position).getDynamicId());
        sendBroadcast(intent);
        dynamicInfoVoList.remove(position);
        setListData();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_nonetwork) {
            dynamicInfoVoList.clear();
            getMyDynamic(pageNumber, 10);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onEvent(this, "openUserDynamic");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                dynamicInfoVoList.clear();
                pageNumber = 1;
                getMyDynamic(pageNumber, 10);
                pullToRefreshListView.onPullDownRefreshComplete();
                setLastUpdateTime();

            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                pageNumber++;
                getMyDynamic(pageNumber, 10);
                pullToRefreshListView.onPullUpRefreshComplete();
                pullToRefreshListView.setHasMoreData(hasData);
                setLastUpdateTime();

            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        pullToRefreshListView.setLastUpdatedLabel(text);
    }

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

}
