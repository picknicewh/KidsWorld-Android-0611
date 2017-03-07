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

import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.util.AlbumSelectContract;
import net.hongzhang.discovery.util.AlbumSelectPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 */
public class SelectAlubmFramgment extends BaseFragement implements AlbumSelectContract.View, View.OnClickListener {
    /**
     * 专辑列表
     */
   private NoScrollGirdView gridView;
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
    private final  static int pageSize=6;
    /**
     * 没有数据
     */
    private TextView tv_nodata;
    /**
     * 数据列表
     */
    private List<CompilationVo> compilationVos;
    /**
     * 数据处理
     */
    private AlbumSelectPresenter presenter;
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
        View view = inflater.inflate(R.layout.fragment_select_album,null);
        initView(view);
        return view;
    }
    private void initView(View view){
        gridView = $(view,R.id.gv_album);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view,R.id.tv_load_more);
        iv_load_more = $(view,R.id.iv_load_more);
        tv_nodata = $(view, R.id.tv_nodata);
        compilationVos=  new ArrayList<>();
        ll_load_more.setOnClickListener(this);
        initData();

    }
    private void initData(){
        Bundle bundle = getArguments();
        type  =bundle.getInt("type");
        presenter = new AlbumSelectPresenter(getActivity(),this);
        presenter.getAlbumList(type,pageSize,pageNumber);
    }
    @Override
    public void setAlbum(final List<CompilationVo> compilationVos) {
      this.compilationVos  = compilationVos;
        adapter = new SelectAlbumAdapter(getActivity(),compilationVos);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CompilationVo compilationVo = compilationVos.get(position);
                presenter.startMusicActivity(String.valueOf(compilationVo.getAlbumId()),null);
            }
        });
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void setResourceSize(int size) {
        if (size == 0) {
            if (compilationVos.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
            } else {
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText("你还没有收藏哦，快去收藏吧！");
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
    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    @Override
    public void stopLoadingDialog() {
        super.stopLoadingDialog();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ll_load_more){
            pageNumber++;
            presenter.getAlbumList(type,pageSize,pageNumber);
        }
    }
}
