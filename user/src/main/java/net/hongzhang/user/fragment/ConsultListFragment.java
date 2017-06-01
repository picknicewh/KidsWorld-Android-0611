package net.hongzhang.user.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshListView;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.ConsultAdapter;
import net.hongzhang.user.mode.CompilationsVo;
import net.hongzhang.user.util.MyResourcePresent;
import net.hongzhang.user.util.ResourceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/25
 * 名称：我的收藏和园所记录的教育资讯页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConsultListFragment extends BaseFragement implements ResourceContract.View, View.OnClickListener {
    private final static int pageSize = 10;
    private ListView lv_list;
    private TextView tv_nodata;
    private int pageNumber = 1;
    private ConsultAdapter adapter;
    // private List<ResourceVo> resourceManagerVoList;
    //  private LinearLayout ll_load_more;
    //   private TextView tv_load_more;
    //   private ImageView iv_load_more;
    private int source;
    private MyResourcePresent myResourcePresent;
    private PullToRefreshListView pullToRefreshListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_nodata = $(view, R.id.tv_nodata);
        pullToRefreshListView = $(view, R.id.pullToRefreshListView);
        lv_list = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setPullRefreshEnabled(false);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {}
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        if (source == 0) {
                            myResourcePresent.getCollectResourceList(pageNumber, pageSize, 3);
                        } else if (source == 1) {
                            myResourcePresent.getPlayRecordList(pageNumber, pageSize, 3);
                        }
                        pullToRefreshListView.onPullUpRefreshComplete();
                        pullToRefreshListView.setHasMoreData(hasMoreData);
                    }
                }.sendEmptyMessageDelayed(0x01, 500);
            }
        });
        //  lv_list = $(view, R.id.lv_consult_list);
        //  ll_load_more = $(view, R.id.ll_load_more);
        //   tv_load_more = $(view, R.id.tv_load_more);
        //   iv_load_more = $(view, R.id.iv_load_more);
        //   ll_load_more.setOnClickListener(this);
        initData();
    }

    public void initData() {
        initList();
        Bundle bundle = getArguments();
        source = bundle.getInt("source");
        myResourcePresent = new MyResourcePresent(getActivity(), this, source, 3);
    }
    private List<ResourceVo> resourceVoList;
    private void initList(){
        resourceVoList = new ArrayList<>();
        adapter = new ConsultAdapter(getActivity(), resourceVoList);
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ResourceVo compilationVo = resourceVoList.get(i);
                myResourcePresent.starConsultActivity(compilationVo.getResourceId());
            }
        });

    }
    @Override
    public void onClick(View view) {
      /*  if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            if (source == 0) {
                myResourcePresent.getCollectResourceList(pageNumber, pageSize, 3);
            } else if (source == 1) {
                myResourcePresent.getPlayRecordList(pageNumber, pageSize, 3);
            }
        }*/
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    @Override
    public void stopLoadingDialog() {
        super.stopLoadingDialog();
    }

    @Override
    public void setResourceVoList(final List<ResourceVo> resourceVoList, List<CompilationsVo> compilationsVos) {
        this.resourceVoList.addAll(resourceVoList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
      /*  this.resourceManagerVoList = resourceVoList;
        adapter = new ConsultAdapter(getActivity(), resourceVoList);
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ResourceVo compilationVo = resourceVoList.get(i);
                myResourcePresent.starConsultActivity(compilationVo.getResourceId());
            }
        });
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }*/
    }

    private boolean hasMoreData;

    @Override
    public void setResourceSize(int size) {
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        if (size == 0) {
            if (source == 0) {//我的收藏
                if (resourceVoList.size() > 0) {//上一页是最后一页
                    tv_nodata.setVisibility(View.GONE);
                } else {//没有数据情况
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有收藏哦，快去收藏吧！");
                }
            } else {//历史记录
                if (resourceVoList.size() > 0) {//上一页是最后一页
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有足迹哦，快去留下足迹吧！");
                }
            }
            //lastPage();
        } else {
            if (size < 10) {
                //    lastPage();
            }
            tv_nodata.setVisibility(View.GONE);
        }
    }

    private void lastPage() {
        // ll_load_more.setVisibility(View.VISIBLE);
        //  tv_load_more.setText("没有更多数据了");
        // iv_load_more.setVisibility(View.GONE);
        //   ll_load_more.setClickable(false);
    }
}
