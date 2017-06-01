package net.hongzhang.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ThemeVoListActivity extends BaseActivity implements ThemeVoListContract.View, View.OnClickListener {
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
    /**
     * 加载更多
     */
    //private LinearLayout ll_load_more;
    // private TextView tv_load_more;
    // private ImageView iv_load_more;
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
    private TextView tv_no_more_data;
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private CompilationAdapter adapter;
    private List<CompilationVo> compilationVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compilation_resource_list);
        initview();
    }

    @Override
    protected void setToolBar() {
        themeName = getIntent().getStringExtra("themeName");
        setCententTitle(themeName);
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    private void initview() {
        //  ll_load_more = $(R.id.ll_load_more);
        // tv_load_more = $(R.id.tv_load_more);
        //  iv_load_more = $(R.id.iv_load_more);
        //  ll_load_more.setOnClickListener(this);
        pullToRefreshRecyclerView = $(R.id.pullToRefresh);
        tv_no_more_data = $(R.id.tv_no_more_data);
        recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        pullToRefreshRecyclerView.setPullLoadEnabled(true);
        pullToRefreshRecyclerView.setPullRefreshEnabled(false);
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {}
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
        initData();
    }
    private void initData() {
        Intent intent = getIntent();
        presenter = new ThemeVoListPresenter(ThemeVoListActivity.this, this);
        type = intent.getIntExtra("type", 1);
        themeId = intent.getStringExtra("themeId");
        presenter.getThemVoList(themeId, pageSize, pageNumber);
    }
    private void initList() {
        compilationVoList = new ArrayList<>();
        adapter = new CompilationAdapter(this, compilationVoList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CompilationVo compilationVo = compilationVoList.get(position);
                if (type == MainRecommendPresenter.TYPE_MUISC) {
                    // presenter.startMusicActivity(compilationVo.getAlbumId(), null);
                    presenter.getSongList(UserMessage.getInstance(ThemeVoListActivity.this).getTsId(), compilationVo.getAlbumId());
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
    private boolean hasMoreData;
    @Override
    public void setThemeVoSize(int size) {
        Log.i("TAG", "------------------------------" + size);
        if (size == 0 || size < pageSize) {
          //  lastPage();
            hasMoreData = false;
        }else {
            hasMoreData = true;
        }
        tv_no_more_data.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
        pullToRefreshRecyclerView.setPullLoadEnabled(hasMoreData);
    }

    @Override
    public void onClick(View view) {
       /* if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            presenter.getThemVoList(themeId, pageSize, pageNumber);
        }*/
    }

    private void lastPage() {
        //ll_load_more.setVisibility(View.VISIBLE);
        //tv_load_more.setText("没有更多数据了");
        // iv_load_more.setVisibility(View.GONE);
    }
}
