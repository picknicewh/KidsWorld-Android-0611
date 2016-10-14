package net.hunme.user.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.listview.PullToRefreshLayout;
import net.hunme.baselibrary.widget.listview.PullableListView;
import net.hunme.user.R;
import net.hunme.user.adapter.MyDynamicAdapter;
import net.hunme.user.mode.DynamicInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDynamicActivity extends BaseActivity implements OkHttpListener,PullToRefreshLayout.OnRefreshListener {
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
    private  static List<DynamicInfoVo> dynamicInfoVoList;
    private int state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        initview();
    }
    private void initview(){
        lv_dynamic = $(R.id.lv_dynamic);
        refresh_view = $(R.id.refresh_view);
        getMyDynamic(pageNumber);
        refresh_view.setOnRefreshListener(this);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的动态");
    }
    public void  getMyDynamic(int pageNumber){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",pageNumber);
        params.put("pageSize",10);
        Type type = new TypeToken<Result<List<DynamicInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MYDYNAMICS,params,this);
        showLoadingDialog();
    }
    @Override
    public void onResume() {
        super.onResume();
        //用户发布动态成功 重新刷新数据
        if(G.KisTyep.isReleaseSuccess) {
            G.KisTyep.isReleaseSuccess = false;
            getMyDynamic(pageNumber);
        }
        // 动态发生改变 刷新数据
        if(G.KisTyep.isUpdateComment){
            getMyDynamic(pageNumber);
            G.KisTyep.isUpdateComment=false;
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.MYDYNAMICS)){
            if (date!=null){
                stopLoadingDialog();
                MyDynamicAdapter adapter=null;
                List<DynamicInfoVo> dynamicInfoVos = ((Result<List<DynamicInfoVo>> )date).getData();
                int  size = dynamicInfoVos.size();
                if (size>0&& pageNumber==1){
                    adapter = new MyDynamicAdapter(this,dynamicInfoVos,pageNumber);
                    dynamicInfoVoList = dynamicInfoVos;
                    state=1;
                }else if (size>0&& pageNumber>1){
                    adapter = new MyDynamicAdapter(this,dynamicInfoVos,pageNumber);
                    dynamicInfoVoList = dynamicInfoVos;
                    state=2;
                }else if (dynamicInfoVos.size()==0&& pageNumber>1){
                    adapter = new MyDynamicAdapter(this,dynamicInfoVoList,pageNumber);
                    state=3;
                }
                lv_dynamic.setAdapter(adapter);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
     stopLoadingDialog();
    }

    @Override
    public void onRefresh( final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (pageNumber>1){
                    pageNumber--;
                }else {
                    pageNumber=1;
                }
                getMyDynamic(pageNumber);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        }.sendEmptyMessageDelayed(0,1000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (state==1){
                    pageNumber++;
                    getMyDynamic(pageNumber);
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }else if (state==2){
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    return;
                }
            }
        }.sendEmptyMessageDelayed(0,1000);
    }
}
