package net.hongzhang.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.widget.NoScrollListView;
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
    private NoScrollListView lv_list;
    private TextView tv_nodata;
    private int pageNumber = 1;
    private List<ResourceVo> resourceManagerVoList;
    private ConsultAdapter adapter;
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;
    private int source;
    private MyResourcePresent myResourcePresent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_list = $(view, R.id.lv_consult_list);
        tv_nodata = $(view, R.id.tv_nodata);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view, R.id.tv_load_more);
        iv_load_more = $(view, R.id.iv_load_more);
        ll_load_more.setOnClickListener(this);
        initData();
    }

    public void initData() {
        Bundle bundle = getArguments();
        source = bundle.getInt("source");
        resourceManagerVoList = new ArrayList<>();
        myResourcePresent = new MyResourcePresent(getActivity(), this, source, 3);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            if (source == 0) {
                myResourcePresent.getCollectResourceList(pageNumber, pageSize, 3);
            } else if (source == 1) {
                myResourcePresent.getPlayRecordList(pageNumber, pageSize, 3);
            }
        }
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
        this.resourceManagerVoList = resourceVoList;
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
        }
    }

    @Override
    public void setResourceSize(int size) {
        if (size == 0) {
            if (source == 0) {//我的收藏
                if (resourceManagerVoList.size() > 0) {//上一页是最后一页
                    tv_nodata.setVisibility(View.GONE);
                } else {//没有数据情况
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有收藏哦，快去收藏吧！");
                }
            } else {//历史记录
                if (resourceManagerVoList.size() > 0) {//上一页是最后一页
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("你还没有足迹哦，快去留下足迹吧！");
                }
            }
            lastPage();
        } else {
            if (size < 10) {
                lastPage();
            }
            tv_nodata.setVisibility(View.GONE);
        }
    }
    private void lastPage(){
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
        ll_load_more.setClickable(false);
    }
}
