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
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.modle.ResourceVo;
import net.hongzhang.discovery.presenter.ConsultFragmentContract;
import net.hongzhang.discovery.presenter.ConsultFragmentPresenter;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public class ConsultListFragment extends BaseFragement implements ConsultFragmentContract.View, View.OnClickListener {
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
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;


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
        initData();
    }

    private void initData() {

        presenter = new ConsultFragmentPresenter(getActivity(), this);
        themeId = getArguments().getString("themeId");
        presenter.getConsultList(pageNumber, pageSize, themeId, UserMessage.getInstance(getActivity()).getAccount_id());
    }

    @Override
    public void setConsultList(final List<ResourceVo> resourceVos) {

        adapter = new ConsultAdapter(getActivity(), resourceVos);
        lv_consult.setAdapter(adapter);
        lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.starConsultActivity(resourceVos.get(i).getResourceId());
            }
        });
    }

    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0 || size<pageSize) {
            lastPage();
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
            presenter.getConsultList(pageNumber, pageSize, themeId, UserMessage.getInstance(getActivity()).getAccount_id());
        }
    }
}
