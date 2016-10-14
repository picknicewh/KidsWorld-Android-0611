package net.hunme.school.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
import net.hunme.school.adapter.CourseListAdapter;
import net.hunme.school.bean.SyllabusVo;
import net.hunme.status.activity.PublishStatusActivity;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.util.PublishPhotoUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseArrangeActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    /**
     * 是否被删除
     */
    public  static String  COURSEDELETE = "net.hunme.school.activity.delete";
    /**
     * 确认删除广播
     */
    private DeleteBroadcast deleteBroadcast;
    private PullToRefreshLayout refresh_view;
    private PullableListView lv_course;
    /**
     * 数据列表
     */
    private static List<SyllabusVo> syllabusVoList;
    private int count = 1;
    private int state = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_arrange);
        initView();
    }
   public void initView(){
       refresh_view = $(R.id.refresh_view);
       lv_course = $(R.id.lv_course);
       refresh_view.setOnRefreshListener(new MyListener());
       registerReceiver();
       syllabusVoList = new ArrayList<>();
       showLoadingDialog();
   }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("课程安排");
        if (UserMessage.getInstance(this).getType().equals("2")){
            setSubTitle("发布");
            setSubTitleOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getArrange(count);
    }
    /**
     * 注册监听网络广播广播
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(COURSEDELETE);
        deleteBroadcast = new DeleteBroadcast();
        this.registerReceiver(deleteBroadcast, filter);
    }

    /**
     *获取课程表
     */
    private void getArrange(int count){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",count);
        params.put("pageSize",2);
        Type type = new TypeToken<Result<List<SyllabusVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETSYLLABUSLISTS,params,this);

    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_subtitle){
            Intent intent = new Intent(this,PublishStatusActivity.class);
            intent.putExtra("type", StatusPublishPopWindow.COURSE);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deleteBroadcast);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<SyllabusVo>> data = (Result<List<SyllabusVo>>) date;
        if (data!=null){
            stopLoadingDialog();
            CourseListAdapter adapter = null;
            final List<SyllabusVo> syllabusVos =data.getData();
            if (count==1&&syllabusVos.size()>0){
                 state=0;
                 syllabusVoList= syllabusVos;
              //  adapter = new CourseListAdapter(this,syllabusVos);
            }else if (count>1 && syllabusVos.size()<2){
                if (syllabusVoList.size()==0){
                    count--;
                    getArrange(count);
                   // adapter = new CourseListAdapter(this,syllabusVoList);
                }else {
                    syllabusVoList= syllabusVos;
                    state=1;
                }
            }
            adapter = new CourseListAdapter(this,syllabusVos);
            lv_course.setAdapter(adapter);
            lv_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PublishPhotoUtil.imageBrowernet(i, (ArrayList<String>) syllabusVos.get(i).getImgs(),CourseArrangeActivity.this);
                }
            });
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    class MyListener implements PullToRefreshLayout.OnRefreshListener {
         @Override
         public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
             new Handler(){
                 @Override
                 public void handleMessage(Message msg) {
                     super.handleMessage(msg);
                     syllabusVoList.clear();
                     count=1;
                     getArrange(count);
                     pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                 }
             }.sendEmptyMessageDelayed(0,2000);
         }

         @Override
         public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
             new Handler(){
                 @Override
                 public void handleMessage(Message msg) {
                     super.handleMessage(msg);
                     syllabusVoList.clear();
                     if (state==1){
                         pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NoMORE);
                     }else if (state==0){
                         count++;
                     }
                     getArrange(count);
                     pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                 }
             }.sendEmptyMessageDelayed(0,2000);

         }
     }
    private class  DeleteBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(COURSEDELETE)){
                getArrange(count);
            }
        }
    }
}
