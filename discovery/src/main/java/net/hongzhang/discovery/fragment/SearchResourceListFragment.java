package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.activity.SearchResourceActivity;
import net.hongzhang.discovery.adapter.CompilationAdapter;
import net.hongzhang.discovery.adapter.SearchResourceAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.ResourceVo;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.presenter.SearchResourceContract;
import net.hongzhang.discovery.presenter.SearchResourcePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghua on 2016/12/19.
 * 精选资源列表
 */
public class SearchResourceListFragment extends BaseFragement implements View.OnClickListener, SearchResourceContract.View {
    /**
     * 专辑列表
     */
    private RecyclerView rv_album;
    /**
     * 加载更多
     */
    private LinearLayout ll_load_more;
    private TextView tv_load_more;
    private ImageView iv_load_more;
    private int pageSize = 6;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_search_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv_album = $(view, R.id.rv_album);
        ll_load_more = $(view, R.id.ll_load_more);
        tv_load_more = $(view, R.id.tv_load_more);
        iv_load_more = $(view, R.id.iv_load_more);
        compilationVos = new ArrayList<>();
        ll_load_more.setOnClickListener(this);
        pageNumber = 1;
        initData();
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
        this.compilationVos = musicCompilationVos;
        CompilationAdapter adapter = new CompilationAdapter(getActivity(), compilationVos);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        rv_album.setAdapter(adapter);
        rv_album.setLayoutManager(manager);
        adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CompilationVo compilationVo = compilationVos.get(position);
                if (type == 1) {
                    presenter.startMusicActivity(String.valueOf(compilationVo.getAlbumId()), null);
                } else if (type == 2) {
                    presenter.startVideoActivity(String.valueOf(compilationVo.getAlbumId()), null);
                }
            }
        });
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setResourceList(final List<ResourceVo> resourceList) {
        if (resourceList.size() > 0 && resourceList != null) {
            SearchResourceAdapter adapter = new SearchResourceAdapter(getActivity(), resourceList);
            rv_album.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            rv_album.setAdapter(adapter);
            adapter.setOnItemClickListener(new SearchResourceAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    ResourceVo vo = resourceList.get(position);
                    if (type == MainRecommendPresenter.TYPE_MUISC) {
                        presenter.startMusicActivity(vo.getAlbumId(), vo.getResourceId());
                    } else {
                        presenter.startVideoActivity(vo.getAlbumId(), vo.getResourceId());
                    }
                }
            });
        }
    }


    public void setResourceSize(int size) {
        Log.i("TAG","------------------------------"+size);
        if (size == 0 || size < pageSize) {
            lastPage();
        }
    }

    @Override
    public void setSearchHistoryList(List<SearchKeyVo> searchKeyVoList) {}
    private void lastPage() {
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber,
                    userMessage.getAccount_id(), tag, 2);
        }
    }
}
