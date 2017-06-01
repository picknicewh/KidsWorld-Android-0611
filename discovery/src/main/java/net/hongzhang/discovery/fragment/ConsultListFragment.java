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
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.presenter.ConsultFragmentContract;
import net.hongzhang.discovery.presenter.ConsultFragmentPresenter;

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


public class ConsultListFragment extends BaseFragement implements ConsultFragmentContract.View {
    private ListView lv_consult;
    /**
     * 数据处理
     */
    private ConsultFragmentPresenter presenter;
    /**
     * 适配器
     */
    private ConsultAdapter adapter;
    /**
     * 主题id
     */
    private String themeId;
    private int pageNumber = 1;
    private int pageSize = 10;
    /**
     * 加载更多
     */
    //   private LinearLayout ll_load_more;
    //   private TextView tv_load_more;
    //   private ImageView iv_load_more;
    private PullToRefreshListView pullToRefreshListView;
    private List<ResourceVo> resourceVoList;
    private boolean hasMoreData;
    private String accountId;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mconsult_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        accountId = UserMessage.getInstance(getActivity()).getAccount_id();
        resourceVoList = new ArrayList<>();
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setPullRefreshEnabled(false);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        lv_consult = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        presenter.getConsultList(pageNumber, pageSize, themeId, accountId);
                        pullToRefreshListView.onPullUpRefreshComplete();
                        pullToRefreshListView.setHasMoreData(hasMoreData);
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
        //   lv_consult = (ListView) view.findViewById(R.id.lv_mconsult_list);
        //   ll_load_more = $(view, R.id.ll_load_more);
        //   tv_load_more = $(view, R.id.tv_load_more);
        //   iv_load_more = $(view, R.id.iv_load_more);
        //   ll_load_more.setOnClickListener(this);
        initList();
    }

    private void initList() {
        adapter = new ConsultAdapter(getActivity(), resourceVoList);
        lv_consult.setAdapter(adapter);
        lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.starConsultActivity(resourceVoList.get(i).getResourceId());
            }
        });
        initData();
    }

    private void initData() {
        presenter = new ConsultFragmentPresenter(getActivity(), this);
        themeId = getArguments().getString("themeId");
        presenter.getConsultList(pageNumber, pageSize, themeId, accountId);
    }

    @Override
    public void setConsultList(final List<ResourceVo> resourceVos) {
        resourceVoList.addAll(resourceVos);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0 || size < pageSize) {
            // lastPage();
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
    }

    private void lastPage() {
        // ll_load_more.setVisibility(View.VISIBLE);
        // tv_load_more.setText("没有更多数据了");
        //  iv_load_more.setVisibility(View.GONE);
        //  ll_load_more.setClickable(false);
    }

}
