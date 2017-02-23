package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.listview.PullToRefreshLayout;
import net.hongzhang.baselibrary.widget.listview.PullableListView;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.LeaveListAdapter;
import net.hongzhang.school.bean.LeaveVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveListActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {

    /**
     * 适配器
     */
    public LeaveListAdapter adapter;
    /**
     * 下来刷新view
     */
    private PullableListView lv_leaves;
    /**
     * 页码
     */
    private int count=1;
    private PullToRefreshLayout refresh_view;
    /**
     * 数据列表
     */
    public  List<LeaveVo> leaveVos;
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
    private LinearLayout ll_loadmore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
         initView();

    }
     private void  initView(){
         lv_leaves  = $(R.id.lv_leaves);
         refresh_view = $(R.id.refresh_view);
         rl_nonetwork = $(R.id.rl_lvnonetwork);
         tv_nodata = $(R.id.tv_nodata);
         ll_loadmore = $(R.id.load_more);
         refresh_view.setOnRefreshListener(new MyListener());
         rl_nonetwork.setOnClickListener(this);
         leaveVos = new ArrayList<>();
         getLeave();
         adapter = new LeaveListAdapter(this,leaveVos);
         lv_leaves.setAdapter(adapter);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("请假");
        if (UserMessage.getInstance(this).getType().equals("1")){
            setSubTitle("我要请假");
        }
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_subtitle){
            Intent intent = new Intent(this,LeaveAskActivity.class);
            startActivity(intent);
        }else if (view.getId()==R.id.rl_lvnonetwork){
            leaveVos.clear();
            getLeave();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        leaveVos.clear();
        getLeave();
        Log.i("sssss","====================onRestart=======================");
    }

    /**
     * 获取请假列表
     */
    private void getLeave(){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",count);
        params.put("pageSize",4);
        Type type = new TypeToken<Result<List<LeaveVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETLEAVES,params,this);
        if (G.isNetworkConnected(this)){
            showLoadingDialog();
            rl_nonetwork.setVisibility(View.GONE);
            dispalynoList(false);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
            dispalynoList(true);
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (Apiurl.SCHOOL_GETLEAVES.equals(uri)){
            Result<List<LeaveVo>> data = (Result<List<LeaveVo>>) date;
            if (data!=null){
                stopLoadingDialog();
                List<LeaveVo> leaveVoList = data.getData();
                if (leaveVoList.size()>0){
                    leaveVos.addAll(leaveVoList);
                }
                if (leaveVos.size()==0){
                    tv_nodata.setVisibility(View.VISIBLE);
                    dispalynoList(true);
                }else {
                    dispalynoList(false);
                    adapter.notifyDataSetChanged();
                    tv_nodata.setVisibility(View.GONE);
                }
                refresh_view.setLv_count(leaveVos.size());
            }
        }
    }
    /**
     * 删除课程，更新界面
     */
    public void deleteUpdate(int position){
        leaveVos.remove(position);
        if (leaveVos.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
            dispalynoList(true);
        }else {
            dispalynoList(false);
            tv_nodata.setVisibility(View.GONE);
        }
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh_view.setLv_count(leaveVos.size());
    }

    private void dispalynoList(boolean isvisible){
        if (isvisible){
            lv_leaves.setVisibility(View.GONE);
            ll_loadmore.setVisibility(View.GONE);
        }else {
            lv_leaves.setVisibility(View.VISIBLE);
            ll_loadmore.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onError(String uri, String error) {
        rl_nonetwork.setVisibility(View.VISIBLE);
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    class MyListener implements PullToRefreshLayout.OnRefreshListener {
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    leaveVos.clear();
                    count=1;
                    getLeave();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 500);
        }

        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    count++;
                    getLeave();
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 500);
        }
    }
}
