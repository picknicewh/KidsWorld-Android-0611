package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecyclerView;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MulLeaveListAdapter2;
import net.hongzhang.school.bean.VacationVo;
import net.hongzhang.school.presenter.LeaveListContract;
import net.hongzhang.school.presenter.LeaveListPresenter;

import java.util.ArrayList;
import java.util.List;

public class LeaveListActivity extends BaseActivity implements View.OnClickListener, LeaveListContract.View, PullToRefreshBase.OnRefreshListener<RecyclerView> {
    private int pageSize = 10;

    /**
     * 页码
     */
    private int count = 1;

    /**
     * 断网提示
     */
    private RelativeLayout rl_nonetwork;
    /**
     * 没有数据提示
     */
    private TextView tv_nodata;
    /**
     * 加载更多
     */
    private LeaveListPresenter presenter;
    private UserMessage userMessage;
    private RecyclerView rv_leave_after;
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private TextView tv_no_more_data;
    private MulLeaveListAdapter2 adapter2;
    private List<VacationVo> vacationVoList;
    private int beforeSize = 0;
    private int afterSize = 0;
    private List<Integer> resIdList;
    private List<String> titleList;
    private boolean hasMoreData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        initView();

    }

    private void initView() {
        rl_nonetwork = $(R.id.rl_lvnonetwork);
        tv_nodata = $(R.id.tv_nodata);
        pullToRefreshRecyclerView = $(R.id.pullToRefreshRecyclerView);
        tv_no_more_data = $(R.id.tv_no_more_data);
        rv_leave_after = pullToRefreshRecyclerView.getRefreshableView();
        pullToRefreshRecyclerView.setPullLoadEnabled(true);
        pullToRefreshRecyclerView.setPullRefreshEnabled(false);
        pullToRefreshRecyclerView.setOnRefreshListener(this);
        rl_nonetwork.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
        vacationVoList = new ArrayList<>();
        resIdList = new ArrayList<>();
        titleList = new ArrayList<>();
        adapter2 = new MulLeaveListAdapter2(this, vacationVoList, beforeSize, afterSize, titleList, resIdList);
        rv_leave_after.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_leave_after.setAdapter(adapter2);
        presenter = new LeaveListPresenter(LeaveListActivity.this, this);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("请假");
        if (UserMessage.getInstance(this).getType().equals("1")) {
            setSubTitle("我要请假");
        }
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_subtitle) {
            Intent intent = new Intent(this, LeaveAskActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.rl_lvnonetwork) {
            if (!userMessage.getType().equals("1")) {
                presenter.getTodayLeave(userMessage.getTsId());
            } else {
                presenter.getLeaveing(userMessage.getTsId(), count, pageSize);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        count = 1;
        if (!userMessage.getType().equals("1")) {
            presenter.getTodayLeave(userMessage.getTsId());
        } else {
            presenter.getLeaveing(userMessage.getTsId(), count, pageSize);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onEvent(this, "openLeave");
    }


    @Override
    public void setLeaveList(List<VacationVo> vacationVos, int beforeSize, int afterSize, List<String> titleList, List<Integer> resIdList) {
        this.vacationVoList.clear();
        this.titleList.clear();
        this.resIdList.clear();
        this.vacationVoList.addAll(vacationVos);
        this.titleList.addAll(titleList);
        this.resIdList.addAll(resIdList);
        this.beforeSize = beforeSize;
        this.afterSize = afterSize;
        tv_nodata.setVisibility(vacationVoList.size() == 0 ? View.VISIBLE : View.GONE);
        adapter2.setSize(beforeSize,afterSize);
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
        int size = vacationVoList.size();
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        pullToRefreshRecyclerView.setPullLoadEnabled(hasMoreData);
        tv_no_more_data.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                count++;
                if (!userMessage.getType().equals("1")) {
                    presenter.getTodayLeave(userMessage.getTsId());
                } else {
                    presenter.getLeaveing(userMessage.getTsId(), count, pageSize);
                }
                refreshView.onPullUpRefreshComplete();
            }
        }.sendEmptyMessageDelayed(0, 500);
    }

}
