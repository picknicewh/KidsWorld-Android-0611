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
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecyclerView;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.activity.SearchResourceActivity;
import net.hongzhang.discovery.adapter.SearchResourceAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.presenter.SearchResourceContract;
import net.hongzhang.discovery.presenter.SearchResourcePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 * 精选资源列表
 * 舍弃了搜索专辑显示 --看日后产品修改 这里设计出现问题
 */
public class SearchResourceListFragment extends BaseFragement implements View.OnClickListener, SearchResourceContract.View {
    /**
     * 专辑列表
     */
    private RecyclerView rv_album;
    /**
     * 加载更多
     */
    //  private LinearLayout ll_load_more;
    //  private TextView tv_load_more;
    //  private ImageView iv_load_more;
    private int pageSize = 8;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 数据列表
     */
    private List<CompilationVo> compilationVos;
    /**
     * 数据处理
     */
    private SearchResourcePresenter presenter;
    /**
     * 类型
     */
    private int type;
    private UserMessage userMessage;
    private String tag;

    private SearchResourceAdapter adapter;
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private List<ResourceVo> resourceVoList;
    private TextView tv_no_more_data;

    // private CompilationAdapter compilationAdapter;
    // private List<CompilationVo> compilationList;
    //  private SearchResourceAdapter resourceAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_search_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        // rv_album = $(view, R.id.rv_album);
        //  ll_load_more = $(view, R.id.ll_load_more);
        //  tv_load_more = $(view, R.id.tv_load_more);
        //  iv_load_more = $(view, R.id.iv_load_more);
        //  ll_load_more.setOnClickListener(this);
        resourceVoList = new ArrayList<>();
        tv_no_more_data = $(view, R.id.tv_no_more_data);
        pullToRefreshRecyclerView = $(view, R.id.pullrecyclerView);
        rv_album = pullToRefreshRecyclerView.getRefreshableView();
        rv_album.setId((int) (Math.random()*100));
        pullToRefreshRecyclerView.setPullLoadEnabled(true);
        pullToRefreshRecyclerView.setPullRefreshEnabled(false);
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {}
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber,
                                userMessage.getAccount_id(), tag, 2);
                        pullToRefreshRecyclerView.onPullUpRefreshComplete();

                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
        initList();
        initData();
    }

    private void initList() {
        adapter = new SearchResourceAdapter(getActivity(), resourceVoList);
        rv_album.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_album.setNestedScrollingEnabled(false);
        rv_album.setAdapter(adapter);
        adapter.setOnItemClickListener(new SearchResourceAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ResourceVo vo = resourceVoList.get(position);
                if (G.isEmteny(vo.getAlbumId()) || G.isEmteny(vo.getResourceId())) {
                    G.showToast(getActivity(), "该资源已经下架！");
                } else {
                    if (type == MainRecommendPresenter.TYPE_MUISC) {
                        presenter.getSongList(userMessage.getTsId(), vo.getAlbumId(), vo.getResourceId());
                    } else {
                        presenter.startVideoActivity(vo.getAlbumId(), vo.getResourceId());
                    }

                }
                presenter.saveSearchKey(tag, 1, userMessage.getTsId(), vo.getResourceName(), vo.getResourceId());
            }
        });
    }

    private void initData() {
        userMessage = UserMessage.getInstance(getActivity());
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        tag = SearchResourceActivity.tag;
        presenter = new SearchResourcePresenter(getActivity(), this);
        presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 1);
    }

    @Override
    public void setCompilationVoList(final List<CompilationVo> musicCompilationVos) {
//        this.compilationVos = musicCompilationVos;
//        CompilationAdapter adapter = new CompilationAdapter(getActivity(), compilationVos);
//        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//        rv_album.setAdapter(adapter);
//        rv_album.setLayoutManager(manager);
//        adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
//            @Override
//            public void OnItemClick(View view, int position) {
//                CompilationVo compilationVo = compilationVos.get(position);
//                if (type == 1) {
//                    presenter.startMusicActivity(String.valueOf(compilationVo.getAlbumId()), null);
//                } else if (type == 2) {
//                    presenter.startVideoActivity(String.valueOf(compilationVo.getAlbumId()), null);
//                }
//            }
//        });
//        if (musicCompilationVos != null && musicCompilationVos.size() > 0) {
//            G.log("======CompilationVoList=====" + compilationVos.size());
//            this.compilationVos.clear();
//            this.compilationVos.addAll(musicCompilationVos);
////            rv_album.setAdapter(compilationAdapter);
//            compilationAdapter.notifyDataSetChanged();
//        }
    }


    @Override
    public void setResourceList(final List<ResourceVo> resourceList) {
        resourceVoList.addAll(resourceList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
      /*  if (resourceList != null && resourceList.size() > 0) {
            //避免重复创建新的adapter
            if (adapter != null) {
                adapter = null;
            }

        }*/
    }

    private boolean hasMoreData;

    @Override
    public void setResourceSize(int size) {
        //当返回数小于当前请求数 则显示加载完毕
        if (size == 0 || size < pageSize) {
            //  lastPage();
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        pullToRefreshRecyclerView.setPullLoadEnabled(hasMoreData);
        tv_no_more_data.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setSearchHistoryList(List<SearchKeyVo> searchKeyVoList) {
    }

    @Override
    public void setloadMoreVis(boolean isVis) {
        // ll_load_more.setVisibility(isVis ? View.VISIBLE : View.GONE);
    }

    private void lastPage() {
        // ll_load_more.setVisibility(View.VISIBLE);
        // tv_load_more.setText("没有更多数据了");
        // iv_load_more.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {

        }
    }
}
