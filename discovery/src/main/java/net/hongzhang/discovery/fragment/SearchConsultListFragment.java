package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.ConsultListAdapter;
import net.hongzhang.discovery.modle.ConsultInfoVo;
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
    private ConsultListAdapter adapter;
    private int pageNumber = 1;
    private int pageSize = 10;
    /**
     * 加载更多
     */
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    /**
     * 数据列表
     */
    private List<ConsultInfoVo> consultInfoVoList;
    private UserMessage userMessage;
    private String tag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mconsult_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_consult = (ListView) view.findViewById(R.id.lv_mconsult_list);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view, R.id.tv_load_more);
        iv_load_more = $(view, R.id.iv_load_more);
        tv_nodata = $(view, R.id.tv_nodata);
        initData();
    }

    private void initData() {
        consultInfoVoList = new ArrayList<>();
        userMessage = UserMessage.getInstance(getActivity());
        Bundle bundle = getArguments();
        presenter = new SearchConsultPresenter(getActivity(), this);
        tag = bundle.getString("tag");
        presenter.getSearchResourceList(userMessage.getTsId(), 3, pageSize, pageNumber,userMessage.getAccount_id(), tag);
    }

    @Override
    public void setConsultList(final List<ConsultInfoVo> consultList) {
        consultInfoVoList = consultList;
        adapter = new ConsultListAdapter(getActivity(), consultList);
        lv_consult.setAdapter(adapter);
        lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.startConsultActivity(consultList.get(i).getResourceId());
            }
        });
    }

    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0) {
            if (consultInfoVoList.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
            } else {
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText("没有资讯哦！");
            }
            lastPage();
        } else {
            if (size < pageSize) {
                lastPage();
            }
            tv_nodata.setVisibility(View.GONE);
        }
    }

    private void lastPage() {
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
        ll_load_more.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            presenter.getSearchResourceList(userMessage.getTsId(), 3, pageNumber,
                    pageSize, userMessage.getAccount_id(), tag);
        }
    }
}