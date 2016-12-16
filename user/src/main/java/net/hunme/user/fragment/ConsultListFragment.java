package net.hunme.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.baselibrary.widget.NoScrollListView;
import net.hunme.user.R;
import net.hunme.user.adapter.ConsultAdapter;
import net.hunme.user.mode.CompilationsVo;
import net.hunme.user.util.MyResourcePresent;
import net.hunme.user.util.ResourceContract;

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
    private final  static int pageSize=10;
    private NoScrollListView lv_list;
    private TextView tv_nodata;
    private int pageNumber = 1;
    private  List<ResourceVo> resourceManagerVoList;
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
    public  void initData(){
        Bundle bundle = getArguments();
        source = bundle.getInt("source");
        resourceManagerVoList = new ArrayList<>();
        myResourcePresent = new MyResourcePresent(getActivity(),this,source,3);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            if (source == 0) {
                myResourcePresent.getCollectResourceList(pageNumber,pageSize,3);
            } else if (source==1){
               myResourcePresent.getPlayRecordList(pageNumber,pageSize,3);
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
                myResourcePresent.starConsultActivity(Integer.parseInt(compilationVo.getResourceId()));
            }
        });
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void setResourceSize(int size) {
        if (size==0){
            pageNumber--;
            if (source==0&&resourceManagerVoList.size()>0){
                resourceManagerVoList.clear();
                myResourcePresent.getCollectResourceList(1,pageNumber*pageSize,3);
            }else if (source==1&&resourceManagerVoList.size()>0){
                resourceManagerVoList.clear();
                myResourcePresent.getPlayRecordList(1,pageNumber*pageSize,3);
            }
            tv_nodata.setVisibility(View.VISIBLE);
            lv_list.setVisibility(View.GONE);
            ll_load_more.setVisibility(View.GONE);
        }else {
            if (size<10){
                ll_load_more.setVisibility(View.VISIBLE);
                tv_load_more.setText("没有更多数据了");
                iv_load_more.setVisibility(View.GONE);
                ll_load_more.setClickable(false);
            }
            tv_nodata.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);
        }
    }
}
