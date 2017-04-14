package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecycleView;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.CompilationPlayCountAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.presenter.AlbumSelectContract;
import net.hongzhang.discovery.presenter.AlbumSelectPresenter;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 * 精选资源列表
 */
public class SelectAlubmFragment2 extends BaseFragement implements AlbumSelectContract.View, View.OnClickListener {
    /**
     * 专辑列表
     */
    private RecyclerView recyclerView;
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
    private final static int pageSize = 8;
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
    private CompilationPlayCountAdapter adapter;
   private PullToRefreshRecycleView pullToRefreshRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_album, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
      //  pullToRefreshRecycleView = $(view, R.id.prcv_select_album);
        pullToRefreshRecycleView.setPullRefreshEnabled(false);
        pullToRefreshRecycleView.setPullLoadEnabled(true);
        pullToRefreshRecycleView.setScrollLoadEnabled(true);
        recyclerView = pullToRefreshRecycleView.getRefreshableView();
       // recyclerView = $(view, R.id.rv_album);
        pullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNumber=1;
                pullToRefreshRecycleView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNumber++;
                presenter.getAlbumList(type, pageSize, pageNumber);
                pullToRefreshRecycleView.onPullUpRefreshComplete();

            }
        });
      //  ll_load_more = $(view, R.id.ll_load_more);
      //  tv_load_more = $(view, R.id.tv_load_more);
      //  iv_load_more = $(view, R.id.iv_load_more);
        tv_nodata = $(view, R.id.tv_nodata);
        compilationVos = new ArrayList<>();
      //  ll_load_more.setOnClickListener(this);
        initData();

    }
    private void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        presenter = new AlbumSelectPresenter(getActivity(), this);
        presenter.getAlbumList(type, pageSize, pageNumber);
        adapter = new CompilationPlayCountAdapter(getActivity(), compilationVos);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CompilationPlayCountAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CompilationVo compilationVo = compilationVos.get(position);
                if (type == MainRecommendPresenter.TYPE_MUISC) {
                    presenter.getSongList(UserMessage.getInstance(getActivity()).getTsId(),compilationVo.getAlbumId());
                    //   presenter.startMusicActivity(String.valueOf(compilationVo.getAlbumId()), null);
                } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
                    presenter.starVedioActivity(String.valueOf(compilationVo.getAlbumId()));
                }
            }
        });
    }
    @Override
    public void setAlbum(final List<CompilationVo> compilationVos) {
        this.compilationVos.addAll(compilationVos);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setResourceSize(int size) {
        if (size==0){
          if (compilationVos.size()>0){
              if (compilationVos.size() > 0) {
                  tv_nodata.setVisibility(View.GONE);
              } else {
                  tv_nodata.setVisibility(View.VISIBLE);
                  tv_nodata.setText("你还没有数据哦，快去找点新的吧！");
              }
              pullToRefreshRecycleView.setHasMoreData(true);
          }
        }else {
            if (size < pageSize) {
                pullToRefreshRecycleView.setHasMoreData(false);
            }
        }
       /* if (size == 0) {
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
        }*/
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

        }
    }
}
