package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshListView;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.activity.SearchResourceActivity;
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.SearchConsultContract;
import net.hongzhang.discovery.presenter.SearchConsultPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchConsultListFragment extends BaseFragement implements SearchConsultContract.View, View.OnClickListener {
    private ListView lv_consult;
    /**
     * 数据处理
     */
    private SearchConsultPresenter presenter;
    /**
     * 适配器
     */
    private ConsultAdapter adapter;
    private int pageNumber = 1;
    private int pageSize = 10;
    /**
     * 加载更多
     */
    //  private LinearLayout ll_load_more;
    //  private TextView tv_load_more;
    // private ImageView iv_load_more;

    private UserMessage userMessage;
    private String tag;

    private List<ResourceVo> resourceVoList;
    private PullToRefreshListView pullToRefreshListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mconsult_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //  lv_consult = (ListView) view.findViewById(R.id.lv_mconsult_list);
        //  ll_load_more = $(view, R.id.ll_load_more);
        //  tv_load_more = $(view, R.id.tv_load_more);
        //   iv_load_more = $(view, R.id.iv_load_more);
        //  ll_load_more.setOnClickListener(this);
        resourceVoList = new ArrayList<>();
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
      pullToRefreshListView.setPullRefreshEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullLoadEnabled(false);
        lv_consult =  pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        pageNumber++;
                        presenter.getSearchResourceList(userMessage.getTsId(), pageNumber,
                                pageSize, userMessage.getAccount_id(), tag);
                        pullToRefreshListView.onPullUpRefreshComplete();
                        pullToRefreshListView.setHasMoreData(hasMoreData);
                    }
                }.sendEmptyMessageDelayed(0x01, 500);
            }
        });
        initData();
    }

    private void initData() {
        userMessage = UserMessage.getInstance(getActivity());
        presenter = new SearchConsultPresenter(getActivity(), this);
        pageNumber = 1;
        tag = SearchResourceActivity.tag;
        presenter.getSearchResourceList(userMessage.getTsId(), pageSize, pageNumber, userMessage.getAccount_id(), tag);
        adapter = new ConsultAdapter(getActivity(), resourceVoList);
        lv_consult.setAdapter(adapter);
        lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ResourceVo resourceVo = resourceVoList.get(i);
                if (G.isEmteny(resourceVo.getResourceId())) {
                    G.showToast(getActivity(), "该资源已经下架!");
                } else {
                    presenter.startConsultActivity(resourceVo.getResourceId());
                }
                presenter.saveSearchKey(tag, userMessage.getTsId(), resourceVo.getResourceName(), resourceVo.getResourceId());
            }
        });
    }

    @Override
    public void setConsultList(final List<ResourceVo> resourceVos) {
        resourceVoList.addAll(resourceVos);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
       /* adapter = new ConsultAdapter(getActivity(), resourceVos);
        lv_consult.setAdapter(adapter);
        lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ResourceVo resourceVo=resourceVos.get(i);
                if (G.isEmteny(resourceVo.getResourceId())){
                    G.showToast(getActivity(),"该资源已经下架!");
                }else {
                    presenter.startConsultActivity(resourceVo.getResourceId());
                }
                presenter.saveSearchKey(tag,userMessage.getTsId(),resourceVo.getResourceName(),resourceVo.getResourceId());
            }
        });*/
    }

    @Override
    public void setSearchHistoryList(List<SearchKeyVo> searchKeyVoList) {
    }

    private boolean hasMoreData;

    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
    }
    private void lastPage() {
        //ll_load_more.setVisibility(View.VISIBLE);
        // tv_load_more.setText("没有更多数据了");
        // iv_load_more.setVisibility(View.GONE);
        //  ll_load_more.setClickable(false);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {

        }
    }
}
