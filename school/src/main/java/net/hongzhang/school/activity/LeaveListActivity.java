package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.LeaveListAdapter;
import net.hongzhang.school.bean.LeaveVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveListActivity extends BaseActivity implements View.OnClickListener, OkHttpListener, PullToRefreshBase.OnRefreshListener<ListView> {
    private int pageSize=10;
    /**
     * 适配器
     */
    public LeaveListAdapter adapter;
    /**
     * 下来刷新view
     */
    private ListView lv_leaves;
    /**
     * 页码
     */
    private int count=1;
 //   private PullToRefreshLayout refresh_view;
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
  //  private LinearLayout ll_loadmore;
    private PullToRefreshListView plv_leave_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
         initView();

    }
     private void  initView(){
         plv_leave_list = $(R.id.plv_leave_list);
   //      lv_leaves  = $(R.id.lv_leaves);
    //     refresh_view = $(R.id.refresh_view);;
         lv_leaves = plv_leave_list.getRefreshableView();
         plv_leave_list.setPullLoadEnabled(false);
         plv_leave_list.setScrollLoadEnabled(true);
         plv_leave_list.setOnRefreshListener(this);
     //    plv_leave_list.doPullRefreshing(true, 500);
         rl_nonetwork = $(R.id.rl_lvnonetwork);
         tv_nodata = $(R.id.tv_nodata);
      //   ll_loadmore = $(R.id.load_more);
      //   refresh_view.setOnRefreshListener(new MyListener());
         rl_nonetwork.setOnClickListener(this);
         leaveVos = new ArrayList<>();
         getLeave();
         adapter = new LeaveListAdapter(this,leaveVos);
         lv_leaves.setAdapter(adapter);
         plv_leave_list.setLastUpdatedLabel(DateUtil.getLastUpdateTime());
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
        count=1;
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
        params.put("pageSize",pageSize);
        Type type = new TypeToken<Result<List<LeaveVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETLEAVES,params,this);
        if (G.isNetworkConnected(this)){
            showLoadingDialog();
            rl_nonetwork.setVisibility(View.GONE);
          //  lv_leaves.setVisibility(View.VISIBLE);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
           // lv_leaves.setVisibility(View.GONE);

        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
       stopLoadingDialog();
        if (Apiurl.SCHOOL_GETLEAVES.equals(uri)){
            Result<List<LeaveVo>> data = (Result<List<LeaveVo>>) date;
            ///编码检测判断

            if (data!=null){
                List<LeaveVo> leaveVoList = data.getData();
                if (leaveVoList.size()>0){
                    leaveVos.addAll(leaveVoList);
                }
                hasMoreData = leaveVoList.size()==0 || leaveVoList.size()<pageSize ? false:true;
                plv_leave_list.setHasMoreData(hasMoreData);
                tv_nodata.setVisibility(leaveVos.size()==0?View.VISIBLE:View.GONE);
                plv_leave_list.getFooterLoadingLayout().setVisibility(leaveVos.size()==0?View.GONE:View.VISIBLE);
              /*  if (leaveVos.size()==0){
                    tv_nodata.setVisibility(View.VISIBLE);
                    dispalynoList(true);
                }else {
                    dispalynoList(false);
                    adapter.notifyDataSetChanged();
                    tv_nodata.setVisibility(View.GONE);
                }*/
             //   refresh_view.setLv_count(leaveVos.size());
                if (adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }

        }
    }
    /**
     * 删除课程，更新界面
     */
    public void deleteUpdate(int position){
        leaveVos.remove(position);
        tv_nodata.setVisibility(leaveVos.size()==0?View.VISIBLE:View.GONE);
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
       // refresh_view.setLv_count(leaveVos.size());
    }

   /* private void dispalynoList(boolean isvisible){
        if (isvisible){
            lv_leaves.setVisibility(View.GONE);
           // ll_loadmore.setVisibility(View.GONE);
        }else {
            lv_leaves.setVisibility(View.VISIBLE);
          //  ll_loadmore.setVisibility(View.VISIBLE);
        }
    }*/
   private int flag=1;
    @Override
    public void onError(String uri, Result error) {
        stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error,this);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        // 下拉刷新操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                leaveVos.clear();
                count=1;
                getLeave();
                plv_leave_list.onPullDownRefreshComplete();
                plv_leave_list.setLastUpdatedLabel(DateUtil.getLastUpdateTime());

            }
        }.sendEmptyMessageDelayed(0, 500);
    }
    private boolean hasMoreData;
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        // 加载操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                count++;
                getLeave();
                plv_leave_list.onPullUpRefreshComplete();
                plv_leave_list.setHasMoreData(hasMoreData);

            }
        }.sendEmptyMessageDelayed(0, 500);
    }

  /*  class MyListener implements PullToRefreshLayout.OnRefreshListener {
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
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onEvent(this, "openLeave");
    }
}
