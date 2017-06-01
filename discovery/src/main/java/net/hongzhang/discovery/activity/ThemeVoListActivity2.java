package net.hongzhang.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecyclerView;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.CompilationAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.presenter.ThemeVoListContract;
import net.hongzhang.discovery.presenter.ThemeVoListPresenter;

import java.util.ArrayList;
import java.util.List;

public class ThemeVoListActivity2 extends BaseActivity implements ThemeVoListContract.View {
    /**
     * 主题名称
     */
    private String themeName;
    /**
     * 主题id
     */
    private String themeId;
    /**
     * 专辑显示列表
     */
    private RecyclerView recyclerView;
    private PullToRefreshRecyclerView pullToRefreshView;
    private TextView tv_no_more_data;
    /**
     * 数据处理
     */
    private ThemeVoListPresenter presenter;
    private int type;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 一页显示数据条数
     */
    private final static int pageSize = 10;
    private List<CompilationVo> compilationVoList;
    private CompilationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compilation_resource_list1);
        initData();
    }

    @Override
    protected void setToolBar() {
        themeName = getIntent().getStringExtra("themeName");
        setCententTitle(themeName);
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    private void initData() {
        pullToRefreshView = $(R.id.pullToRefresh);
        tv_no_more_data = $(R.id.tv_no_more_data);
        recyclerView = pullToRefreshView.getRefreshableView();
     //   pullToRefreshView.setPullLoadEnabled(true);
        pullToRefreshView.setScrollLoadEnabled(true);
        pullToRefreshView.setPullRefreshEnabled(false);
        compilationVoList = new ArrayList<>();
        Intent intent = getIntent();
        presenter = new ThemeVoListPresenter(ThemeVoListActivity2.this, this);
        type = intent.getIntExtra("type", 1);
        themeId = intent.getStringExtra("themeId");
        presenter.getThemVoList(themeId, pageSize, pageNumber);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
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
                        presenter.getThemVoList(themeId, pageSize, pageNumber);
                        refreshView.onPullUpRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }
        });
        initList();
    }

    private void initList() {
        adapter = new CompilationAdapter(this, compilationVoList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CompilationVo compilationVo = compilationVoList.get(position);
                if (type == MainRecommendPresenter.TYPE_MUISC) {
                    presenter.getSongList(UserMessage.getInstance(ThemeVoListActivity2.this).getTsId(), compilationVo.getAlbumId());
                } else {
                    presenter.starVedioActivity(compilationVo.getAlbumId(), null);
                }
            }
        });
    }


    @Override
    public void setThemeVoList(final List<CompilationVo> compilationVos) {
        compilationVoList.addAll(compilationVos);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private boolean hasData;

    @Override
    public void setThemeVoSize(int size) {
        if (size == 0 || size < pageSize) {
            hasData = false;
        } else {
            hasData = true;
        }
        tv_no_more_data.setVisibility(hasData ? View.GONE : View.VISIBLE);
        pullToRefreshView.setPullLoadEnabled(hasData);
    }
}
