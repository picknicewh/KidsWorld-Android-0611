package net.hongzhang.user.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.MyDynamicAdapter;
import net.hongzhang.user.mode.DynamicInfoVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDynamicActivity extends BaseActivity implements OkHttpListener,PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    private PullToRefreshLayout refresh_view;
    /**
     * 我的动态列表
     */
    private PullableListView lv_dynamic;
    /**
     * 页码数
     */
    private int pageNumber=1;
    /**
     * 列表信息
     */
    public    List<DynamicInfoVo> dynamicInfoVoList;
    public    MyDynamicAdapter adapter=null;
    /**
     * 页码数
     */
    private int pagesize;
    /**
     *点击进入评论详情的位置
     */
    private int scrollPosition=0;
    /**
     * 断网显示
     */
    private RelativeLayout rl_nonetwork;
    /**
     * 没有数据提示
     */
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        initview();
    }
    private void initview(){
        lv_dynamic = $(R.id.lv_dynamic);
        refresh_view = $(R.id.refresview);
        rl_nonetwork = $(R.id.rl_nonetwork);
        tv_nodata = $(R.id.tv_nodata);
        dynamicInfoVoList = new ArrayList<>();
        rl_nonetwork.setOnClickListener(this);
        getMyDynamic(pageNumber,10);
        adapter = new MyDynamicAdapter(this,dynamicInfoVoList,pageNumber);
        lv_dynamic.setAdapter(adapter);
        refresh_view.setOnRefreshListener(this);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的动态");
    }
    public void  getMyDynamic(int pageNumber,int pagesize){
        this.pagesize = pagesize;
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",pageNumber);
        params.put("pageSize",pagesize);
        Type type = new TypeToken<Result<List<DynamicInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MYDYNAMICS,params,this,2,"MYDYNAMICDATA");
        if (!G.isNetworkConnected(this)){
            rl_nonetwork.setVisibility(View.VISIBLE);
        }else {
            rl_nonetwork.setVisibility(View.GONE);
            showLoadingDialog();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // 动态发生改变 刷新数据
        if(G.KisTyep.isUpdateComment){
            getMyDynamic(scrollPosition+1,1);
            G.KisTyep.isUpdateComment=false;
        }
    }
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }
    @Override
    public void onSuccess(String uri, Object date) {
         stopLoadingDialog();
        if (uri.equals(Apiurl.MYDYNAMICS)){
            Result<String> data = (Result<String>) date;
            if (data==null){
            //    stopLoadingDialog();
            }else {
               // stopLoadingDialog();
                List<DynamicInfoVo> dynamicInfoVos = ((Result<List<DynamicInfoVo>> )date).getData();
                if (dynamicInfoVos.size()>0){
                    if (pagesize==1){
                        dynamicInfoVoList.set(scrollPosition,dynamicInfoVos.get(0));
                    }else {
                        dynamicInfoVoList.addAll(dynamicInfoVos);
                    }
                }
                setListData();
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        rl_nonetwork.setVisibility(View.VISIBLE);
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    private void setListData(){
        if (dynamicInfoVoList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
        }else {
            tv_nodata.setVisibility(View.GONE);
            adapter.setData(dynamicInfoVoList);
        }
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh_view.setLv_count(dynamicInfoVoList.size());
    }
    public void updateDelete(int position){
        dynamicInfoVoList.remove(position);
        setListData();

    }
    @Override
    public void onRefresh( final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                dynamicInfoVoList.clear();
                pageNumber=1;
                getMyDynamic(pageNumber,10);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        }.sendEmptyMessageDelayed(0,500);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                pageNumber++;
                getMyDynamic(pageNumber,10);
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0,500);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_nonetwork){
            dynamicInfoVoList.clear();
            getMyDynamic(pageNumber,10);
        }
    }
}
