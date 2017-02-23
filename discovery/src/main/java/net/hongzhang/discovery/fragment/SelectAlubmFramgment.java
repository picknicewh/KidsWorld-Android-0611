package net.hongzhang.discovery.fragment;

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
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.SelectAlbumAdapter;
import net.hongzhang.discovery.modle.RecommendVo;
import net.hongzhang.discovery.util.AlbumContract;
import net.hongzhang.discovery.util.AlbumPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 */
public class SelectAlubmFramgment extends BaseFragement implements AlbumContract.View,View.OnClickListener{
    /**
     * 专辑列表
     */
   private NoScrollGirdView gridView;
    /**
     * 左边按钮
     */
    private ImageView iv_left;
    /**
     * 中间标题
     */
    private TextView tv_title;
    /**
     *右边按钮
     */
    private ImageView iv_right;
    /**
     * 加载更多
     */
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 一页显示数据条数
     */
    private final  static int pageSize=10;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    /**
     * 数据列表
     */
    private List<RecommendVo> recommendVos;
    /**
     * 数据处理
     */
    private AlbumPresenter presenter;
    /**
     * 类型
     */
    private int type;
    /**
     * 适配器
     */
    private SelectAlbumAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_album,null);
        initView(view);
        return view;
    }
    private void initView(View view){
        gridView = $(view,R.id.gridview);
        iv_right= $(view,R.id.iv_right);
        tv_title=$(view,R.id.tv_title);
        iv_left= $(view,R.id.iv_left);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view,R.id.tv_load_more);
        iv_load_more = $(view,R.id.iv_load_more);
        tv_nodata = $(view, R.id.tv_nodata);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        recommendVos=  new ArrayList<>();
        initData();

    }
    private void initData(){
        Bundle bundle = getArguments();
        type  =bundle.getInt("type");
        presenter = new AlbumPresenter(getActivity(),this);
        presenter.getAlbumList(type,pageSize,pageNumber);

    }
    @Override
    public void setAlbum(final List<RecommendVo> recommendVos) {
      this.recommendVos  = recommendVos;
        adapter = new SelectAlbumAdapter(getActivity(),recommendVos);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RecommendVo recommendVo = recommendVos.get(position);
                presenter.startMusicActivity(String.valueOf(recommendVo.getId()),null);
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
            if (recommendVos.size()>0){
                recommendVos.clear();
                presenter.getAlbumList(1,pageNumber*pageSize,3);
            }
            tv_nodata.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            ll_load_more.setVisibility(View.GONE);
        }else {
            if (size<10){
                ll_load_more.setVisibility(View.VISIBLE);
                tv_load_more.setText("没有更多数据了");
                iv_load_more.setVisibility(View.GONE);
                ll_load_more.setClickable(false);
            }
            tv_nodata.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
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
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_left){
            getActivity().finish();
        }else if (viewId==R.id.iv_right){

        }
    }
}
