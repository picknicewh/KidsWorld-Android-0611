package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.listview.PullToRefreshLayout;
import net.hunme.baselibrary.widget.listview.PullableListView;
import net.hunme.school.R;
import net.hunme.school.adapter.LeaveListAdapter;
import net.hunme.school.bean.LeaveVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveListActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
  //  private ListView lv_leaves;
    private LeaveListAdapter adapter;
    private PullableListView lv_leaves;
    private int count=1;
    private PullToRefreshLayout refresh_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        lv_leaves  = $(R.id.lv_leaves);
        refresh_view = $(R.id.refresh_view);
        refresh_view.setOnRefreshListener(new MyListener());
        getLeave();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setTitle("请假");
        setSubTitle("我要请假");
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_subtitle){
            Intent intent = new Intent(this,LeaveAskActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
         getLeave();
    }
    /**
     * 获取请假列表
     */
    private void getLeave(){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",count);
        params.put("pageSize",3);
        Type type = new TypeToken<Result<List<LeaveVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETLEAVES,params,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<LeaveVo>> data = (Result<List<LeaveVo>>) date;
        if (data!=null){
            List<LeaveVo> leaveVoList = data.getData();
            adapter = new LeaveListAdapter(this,leaveVoList);
            lv_leaves.setAdapter(adapter);
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    class MyListener implements PullToRefreshLayout.OnRefreshListener {

        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    count=1;
                    getLeave();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 2000);
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
            }.sendEmptyMessageDelayed(0, 2000);
        }
    }
}
