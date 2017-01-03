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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import net.hunme.school.R;
import net.hunme.school.adapter.CourseListAdapter;
import net.hunme.school.bean.SyllabusVo;
import net.hunme.status.activity.PublishStatusActivity;
import net.hunme.status.widget.StatusPublishPopWindow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.picturesee.util.ImagePagerActivity;

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
    public   List<SyllabusVo> syllabusVoList;
    private int count = 1;
    public    CourseListAdapter adapter;
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
        setContentView(R.layout.activity_course_arrange);
        initView();
    }
   public void initView(){
       refresh_view = $(R.id.refresh_view);
       lv_course = $(R.id.lv_course);
       rl_nonetwork= $(R.id.rl_nonetwork);
       ll_loadmore =$(R.id.load_more);
       tv_nodata = $(R.id.tv_nodata);
       refresh_view.setOnRefreshListener(new MyListener());
       registerReceiver();
       syllabusVoList = new ArrayList<>();
       showLoadingDialog();
       getArrange(count);
       setlist();

   }
    private void setlist(){
        adapter = new CourseListAdapter(this,syllabusVoList);
        lv_course.setAdapter(adapter);
        lv_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageBrowernet(i,(ArrayList<String>) syllabusVoList.get(i).getImgs(),CourseArrangeActivity.this);
            }
        });
    }

    /**
     *浏览图片
     * @param position
     *@param urls 图片列表
     * @param context 文本
     */
    public static void imageBrowernet(int position, ArrayList<String> urls,Context context) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("source","net");
        context.startActivity(intent);
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
        if (syllabusVoList.size()>0){
            syllabusVoList.clear();
            getArrange(count);
        }
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
     * @param  count
     */
    private void getArrange(int count){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",count);
        params.put("pageSize",10);
        Type type = new TypeToken<Result<List<SyllabusVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETSYLLABUSLISTS,params,this);
      if (G.isNetworkConnected(this)){
          showLoadingDialog();
          rl_nonetwork.setVisibility(View.GONE);
          dispalynonet(false);
      }else {
          dispalynonet(true);
          rl_nonetwork.setVisibility(View.VISIBLE);
      }
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
             List<SyllabusVo> syllabusVos =data.getData();
            if (syllabusVos.size()>0){
               syllabusVoList.addAll(syllabusVos);
            }
            if (syllabusVoList.size()==0){
                tv_nodata.setVisibility(View.VISIBLE);
                dispalynonet(true);
            }else {
                tv_nodata.setVisibility(View.GONE);
                dispalynonet(false);
                adapter.notifyDataSetChanged();
            }
            refresh_view.setLv_count(syllabusVoList.size());
        }
    }
    public void updateDelete(int position){
        syllabusVoList.remove(position);
        if (syllabusVoList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
            dispalynonet(true);
        }else {
            tv_nodata.setVisibility(View.GONE);
            dispalynonet(false);
        }
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh_view.setLv_count(syllabusVoList.size());
    }
    /**
     * 隐藏列表
     * @param isvisible 是否隐藏
     */
    private void dispalynonet(boolean isvisible){
        if (isvisible){
            lv_course.setVisibility(View.GONE);
            ll_loadmore.setVisibility(View.GONE);
        }else {
            lv_course.setVisibility(View.VISIBLE);
            ll_loadmore.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        rl_nonetwork.setVisibility(View.VISIBLE);
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
             }.sendEmptyMessageDelayed(0,500);
         }

         @Override
         public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
             new Handler(){
                 @Override
                 public void handleMessage(Message msg) {
                     super.handleMessage(msg);
                     count++;
                     getArrange(count);
                     pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                 }
             }.sendEmptyMessageDelayed(0,500);

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
