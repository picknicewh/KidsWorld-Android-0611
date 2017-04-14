package net.hongzhang.school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import net.hongzhang.school.adapter.CourseListAdapter;
import net.hongzhang.school.bean.SyllabusVo;
import net.hongzhang.status.activity.PublishStatusActivity;
import net.hongzhang.status.widget.StatusPublishPopWindow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.picturesee.util.ImagePagerActivity;

public class CourseArrangeListActivity extends BaseActivity implements View.OnClickListener, OkHttpListener, PullToRefreshBase.OnRefreshListener<ListView> {
    /**
     * 是否被删除
     */
    public  static String  COURSEDELETE = "net.hongzhang.school.activity.delete";
  //  private PullToRefreshLayout refresh_view;
    private ListView lv_course;
    /**
     * 数据列表
     */
    public   List<SyllabusVo> syllabusVoList;
    public    CourseListAdapter adapter;
    private RelativeLayout rl_nonetwork;
    /**
     * 没有数据提示
     */
    private TextView tv_nodata;
    /**
     * 加载更多
     */
  //  private LinearLayout ll_loadmore;
    private int count = 1;
    private PullToRefreshListView plv_arrange_list;
    private int pageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_arrange);
        initView();
    }
   public void initView(){
      // refresh_view = $(refresh_view);
       plv_arrange_list = $(R.id.plv_arrange_list);
       plv_arrange_list.setPullLoadEnabled(false);
       plv_arrange_list.setScrollLoadEnabled(true);
       plv_arrange_list.setOnRefreshListener(this);
       lv_course  = plv_arrange_list.getRefreshableView();
       //lv_course = $(R.id.lv_course);
       rl_nonetwork= $(R.id.rl_nonetwork);
     //  ll_loadmore =$(R.id.load_more);
       tv_nodata = $(R.id.tv_nodata);
  //     refresh_view.setOnRefreshListener(new MyListener());
       syllabusVoList = new ArrayList<>();
       getArrange(count);
       setlist();
       plv_arrange_list.doPullRefreshing(true,500);

       plv_arrange_list.setLastUpdatedLabel(DateUtil.getLastUpdateTime());
   }
    private void setlist(){
        adapter = new CourseListAdapter(this,syllabusVoList);
        lv_course.setAdapter(adapter);
        lv_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageBrowernet(i,(ArrayList<String>) syllabusVoList.get(i).getImgs(),CourseArrangeListActivity.this);
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
    protected void onRestart() {
        super.onRestart();
        if (syllabusVoList!=null){
            if (syllabusVoList.size()>0){
                syllabusVoList.clear();
            }
            getArrange(count);
        }
        Log.i("ssssss","==============onRestart=================");
    }

    /**
     *获取课程表
     * @param  count
     */
    private void getArrange(int count){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("pageNumber",count);
        params.put("pageSize",pageSize);
        Type type = new TypeToken<Result<List<SyllabusVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETSYLLABUSLISTS,params,this);
       if (G.isNetworkConnected(this)){
          showLoadingDialog();
          rl_nonetwork.setVisibility(View.GONE);
       }else {
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
        MobclickAgent.onEvent(this, "openCurriculum");
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
            tv_nodata.setVisibility(syllabusVoList.size()==0?View.VISIBLE:View.GONE);
            hasMoreData = syllabusVoList.size()==0 || syllabusVoList.size()<pageSize ? false:true;
            plv_arrange_list.setHasMoreData(hasMoreData);
            if (syllabusVoList.size()==0){
                plv_arrange_list.getFooterLoadingLayout().setVisibility(View.GONE);
            }

          /*  if (syllabusVoList.size()==0){
                tv_nodata.setVisibility(View.VISIBLE);
             //   dispalynonet(true);
            }else {
                tv_nodata.setVisibility(View.GONE);
              //  dispalynonet(false);
                adapter.notifyDataSetChanged();
            }*/
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }
         //   refresh_view.setLv_count(syllabusVoList.size());
        }
    }
    /**
     * 删除
     * @param  position 当前位置
     */
    public void updateDelete(int position){
        syllabusVoList.remove(position);
        tv_nodata.setVisibility(syllabusVoList.size()==0?View.VISIBLE:View.GONE);
        if (syllabusVoList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
        //    dispalynonet(true);
        }else {
            tv_nodata.setVisibility(View.GONE);
          //  dispalynonet(false);
        }
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
        //refresh_view.setLv_count(syllabusVoList.size());
    }
    /**
   /*  * 隐藏列表
     *
    private void dispalynonet(boolean isvisible){
        if (isvisible){
            lv_course.setVisibility(View.GONE);
            ll_loadmore.setVisibility(View.GONE);
        }else {
            lv_course.setVisibility(View.VISIBLE);
            ll_loadmore.setVisibility(View.VISIBLE);
        }
    }*/
    @Override
    public void onError(String uri, Result error) {
        stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error,this);
    }
    private boolean hasMoreData;
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                syllabusVoList.clear();
                count=1;
                getArrange(count);
                plv_arrange_list.onPullDownRefreshComplete();
                plv_arrange_list.setLastUpdatedLabel(DateUtil.getLastUpdateTime());

            }
        }.sendEmptyMessageDelayed(0,500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                count++;
                getArrange(count);
                plv_arrange_list.onPullUpRefreshComplete();
                plv_arrange_list.setHasMoreData(hasMoreData);
            }
        }.sendEmptyMessageDelayed(0,500);

    }

  /*  class MyListener implements PullToRefreshLayout.OnRefreshListener {
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
     }*/
}
