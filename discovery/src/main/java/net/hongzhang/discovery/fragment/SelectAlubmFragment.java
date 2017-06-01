package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecyclerView;
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
public class SelectAlubmFragment extends BaseFragement implements AlbumSelectContract.View, View.OnClickListener {
    /**
     * 专辑列表
     */
    private RecyclerView recyclerView;
    /**
     * 加载更多
     */
  /* private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;*/
    private TextView tv_has_more_data;
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
    private List<CompilationVo> compilationVoList;
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
    private PullToRefreshRecyclerView prcv_album;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_album, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        compilationVoList = new ArrayList<>();
        prcv_album = $(view, R.id.prcv_album);
        tv_has_more_data = $(view, R.id.tv_no_more_data);
        tv_nodata = $(view, R.id.tv_nodata);
        recyclerView = prcv_album.getRefreshableView();
        prcv_album.setPullLoadEnabled(true);
        prcv_album.setPullRefreshEnabled(false);
        prcv_album.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        presenter.getAlbumList(type, pageSize, pageNumber);
                        refreshView.onPullUpRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
        //  recyclerView = $(view, R.id.rv_album);
        //   ll_load_more = $(view, R.id.ll_load_more);
        //  tv_load_more = $(view, R.id.tv_load_more);
        //    iv_load_more = $(view, R.id.iv_load_more);

        //   ll_load_more.setOnClickListener(this);
        adapter = new CompilationPlayCountAdapter(getActivity(), compilationVoList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CompilationPlayCountAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CompilationVo compilationVo = compilationVoList.get(position);
                if (type == MainRecommendPresenter.TYPE_MUISC) {
                    presenter.getSongList(UserMessage.getInstance(getActivity()).getTsId(), compilationVo.getAlbumId());
                } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
                    presenter.starVedioActivity(String.valueOf(compilationVo.getAlbumId()));
                }
            }
        });
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        presenter = new AlbumSelectPresenter(getActivity(), this);
        presenter.getAlbumList(type, pageSize, pageNumber);
    }

    @Override
    public void setAlbum(final List<CompilationVo> compilationVos) {
        compilationVoList.addAll(compilationVos);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private boolean hasMoreData;

    @Override
    public void setResourceSize(int size) {
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        prcv_album.setPullLoadEnabled(hasMoreData);
        tv_has_more_data.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
        if (size == 0) {
            if (compilationVoList.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
            } else {
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText("你还没有数据哦！");
            }
        } else {
            tv_nodata.setVisibility(View.GONE);
        }
    }

    private void lastPage() {
        //    ll_load_more.setVisibility(View.VISIBLE);
        //  tv_load_more.setText("没有更多数据了");
        //  iv_load_more.setVisibility(View.GONE);
        //   ll_load_more.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        //  if (view.getId() == R.id.ll_load_more) {
        //  pageNumber++;
        //presenter.getAlbumList(type, pageSize, pageNumber);
        // }
    }
}
