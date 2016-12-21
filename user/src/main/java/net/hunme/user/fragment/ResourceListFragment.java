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
import net.hunme.baselibrary.widget.NoScrollGirdView;
import net.hunme.user.R;
import net.hunme.user.adapter.ResourceAdapter;
import net.hunme.user.mode.CompilationsVo;
import net.hunme.user.util.MyResourcePresent;
import net.hunme.user.util.ResourceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/25
 * 名称：我的收藏和园所记录的我的听听和幼儿课堂页面
 * 版本说明：
 * 附加注释：通过传递不同的source值来判断是哪个位置，再根据传递的不同type值来决定是幼儿课堂还是幼儿听听
 * 主要接口：
 */
public class ResourceListFragment extends BaseFragement implements ResourceContract.View, View.OnClickListener {
    private NoScrollGirdView gv_list;
    private TextView tv_nodata;
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;
    private List<ResourceVo> resourceManagerVoList;
    private List<CompilationsVo> compilationsVoList;
    private ResourceAdapter adapter;
    private int pageNumber = 1;
    /**
     * source=0收藏 source = 1记录
     */
    private int source;
    private MyResourcePresent myResourcePresent;
    private int pageSize = 10;
    private  int type ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gv_list = $(view, R.id.gv_resource_list);
        tv_nodata = $(view, R.id.tv_nodata);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view, R.id.tv_load_more);
        iv_load_more = $(view, R.id.iv_load_more);
        ll_load_more.setOnClickListener(this);
        initData();
    }
    private void initData(){
        Bundle bundle = getArguments();
        source = bundle.getInt("source");
        type  = bundle.getInt("type");
        myResourcePresent = new MyResourcePresent(getContext(),this,source,type);
        if (source == 0) {
            compilationsVoList = new ArrayList<>();
        } else if (source==1){
            resourceManagerVoList = new ArrayList<>();
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            if (source==0){
                myResourcePresent.getCollectResourceList(pageNumber,pageSize,type);
            }else if (source==1){
                myResourcePresent.getPlayRecordList(pageNumber,pageSize,type);
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
    public void setResourceVoList(final List<ResourceVo> resourceVoList, final List<CompilationsVo> compilationsVos) {
        this.resourceManagerVoList = resourceVoList;
        this.compilationsVoList = compilationsVos;
        if (source == 0) {
            adapter = new ResourceAdapter(getActivity(), compilationsVos, resourceVoList);
        } else if (source==1){
            adapter = new ResourceAdapter(getActivity(), compilationsVos, resourceVoList);
        }
        gv_list.setAdapter(adapter);
        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (source == 0) {
                        CompilationsVo compilationsVo = compilationsVos.get(i);
                        String themeId = compilationsVo.getAlbumId();
                        if (type==1){
                            myResourcePresent.starVedioActivity(themeId);
                        }else {

                            myResourcePresent.startMusicActivity(themeId,null);
                        }
                    } else if (source==1){
                        ResourceVo resourceVo = resourceVoList.get(i);
                        if (type==1){
                            myResourcePresent.starVedioActivity(resourceVo.getAlbumId());
                        }else {
                            myResourcePresent.startMusicActivity(resourceVo.getAlbumId(),resourceVo.getResourceId());
                        }

                    }
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
            if (source==0 && compilationsVoList.size()>0){
                compilationsVoList.clear();
                myResourcePresent.getCollectResourceList(1,pageNumber*pageSize,type);
            }else if (source==1&& resourceManagerVoList.size()>0) {
                resourceManagerVoList.clear();
                myResourcePresent.getPlayRecordList(1, pageNumber * pageSize, type);
            }
            if (source==0){
                tv_nodata.setText("你还没有收藏哦，快去收藏吧！");
            }else {
                tv_nodata.setText("你还没有足迹哦，快去留下足迹吧！");
            }
            tv_nodata.setVisibility(View.VISIBLE);
            gv_list.setVisibility(View.GONE);
            ll_load_more.setVisibility(View.GONE);
        }else {
            if (size<10){
                ll_load_more.setVisibility(View.VISIBLE);
                tv_load_more.setText("没有更多数据了");
                iv_load_more.setVisibility(View.GONE);
                ll_load_more.setClickable(false);
            }
            tv_nodata.setVisibility(View.GONE);
            gv_list.setVisibility(View.VISIBLE);
        }
    }
}
