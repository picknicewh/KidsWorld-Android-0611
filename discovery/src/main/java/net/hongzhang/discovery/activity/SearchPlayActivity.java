package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshRecyclerView;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.SearchHistoryAdapter;
import net.hongzhang.discovery.adapter.SearchResourceAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.presenter.SearchResourceContract;
import net.hongzhang.discovery.presenter.SearchResourcePresenter;
import net.hongzhang.discovery.util.TextUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2017/3/5
 * 名称：搜索音乐或者视频
 * 版本说明：
 * 附加注释：
 * 主要接口：获取搜索音乐或者视频列表
 */
public class SearchPlayActivity extends BaseActivity implements View.OnClickListener, SearchResourceContract.View {
    /**
     * 搜索框
     */
    private EditText et_search_key;
    /**
     * 搜索
     */
    private TextView tv_search;
    /**
     * 清除
     */
    private TextView tv_clean;
    /**
     * 加载更多
     */
    //private LinearLayout ll_load_more;
    /// private TextView tv_load_more;
    //private ImageView iv_load_more;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 一页显示数据条数
     */
    private final static int pageSize = 8;
    /**
     * 页面
     */
    private RecyclerView rv_search_play;
    /**
     * 类型
     */
    private int type;
    /**
     * 数据处理
     */
    private SearchResourcePresenter presenter;
    private UserMessage userMessage;
    private String tag;
    private ListView lv_search_play_history;
    //  private LinearLayout ll_search_content;
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private TextView tv_no_more_data;
    private SearchResourceAdapter searchResourceAdapter;
    private LinearLayout ll_search_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_play);
        initView();
    }

    private void initView() {
        // rv_search_play = $(R.id.rv_search_play);
        //ll_load_more = $(R.id.ll_load_more);
        // tv_load_more = $(R.id.tv_load_more);
        // iv_load_more = $(R.id.iv_load_more);
        // ll_load_more.setOnClickListener(this);
        // ll_search_content = $(R.id.ll_search_content);
        ll_search_play = $(R.id.ll_search_play);
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        tv_clean = $(R.id.tv_clean);
        tv_no_more_data = $(R.id.tv_no_more_data);
        lv_search_play_history = $(R.id.lv_search_play_history);
        pullToRefreshRecyclerView = $(R.id.pull_recycle_view);
        setPullToRefreshLayoutParam();
        pullToRefreshRecyclerView.setPullRefreshEnabled(false);
        pullToRefreshRecyclerView.setPullLoadEnabled(true);
        rv_search_play = pullToRefreshRecyclerView.getRefreshableView();
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 2);
                        pullToRefreshRecyclerView.onPullUpRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0x01, 500);
            }
        });
        initData();
    }

    private void initData() {
        initList();
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        et_search_key.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
        presenter = new SearchResourcePresenter(SearchPlayActivity.this, this);
        type = getIntent().getIntExtra("type", MainRecommendPresenter.TYPE_MUISC);
        presenter.getSearchHistoryList(type);
    }

    /**
     * 设置recycleView高度
     */
    private void setPullToRefreshLayoutParam() {
        G.initDisplaySize(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_search, null);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        tv_no_more_data.measure(w, h);
        ll_search_play.measure(w, h);
        int width = G.size.W;
        int height = G.size.H - tv_no_more_data.getMeasuredHeight() - view.getMeasuredHeight() - G.dp2px(this, 74);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        pullToRefreshRecyclerView.setLayoutParams(layoutParams);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        type = getIntent().getIntExtra("type", MainRecommendPresenter.TYPE_MUISC);
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            setCententTitle("搜索听听");
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            setCententTitle("搜索课堂");
        }
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            resourceVoList.clear();
            pageNumber = 1;
            lv_search_play_history.setVisibility(View.GONE);
            tag = et_search_key.getText().toString().trim();
            if (!TextUtil.isAllSpace(tag)) {
                presenter.insertKey(type, tag);
                presenter.getSearchHistoryList(type);
                presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 1);
            } else {
                et_search_key.setText("");
                G.showToast(this, "搜索关键词不能为空");
            }
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        } else if (view.getId() == R.id.ll_load_more) {

        } else if (viewId == R.id.et_search_key) {
            lv_search_play_history.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSearchHistoryList(final List<SearchKeyVo> searchKeyVoList) {
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, searchKeyVoList);
        lv_search_play_history.setAdapter(adapter);
        lv_search_play_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                resourceVoList.clear();
                pageNumber = 1;
                lv_search_play_history.setVisibility(View.GONE);
                tag = searchKeyVoList.get(position).getKey();
                et_search_key.setText(tag);
                presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 1);
            }
        });
    }

    @Override
    public void setloadMoreVis(boolean isVis) {
        //      ll_load_more.setVisibility(isVis ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCompilationVoList(final List<CompilationVo> musicCompilationVos) {
    /*    lv_search_play_history.setVisibility(View.GONE);
        if (musicCompilationVos.size() > 0 && musicCompilationVos != null) {
            if (adapter!=null){
                adapter = null;
            }
            adapter =  new CompilationAdapter(this, musicCompilationVos);
            rv_search_play.setLayoutManager(new GridLayoutManager(this, 2));
            rv_search_play.setAdapter(adapter);
            rv_search_play.setNestedScrollingEnabled(false);
            adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    CompilationVo vo = musicCompilationVos.get(position);
                    presenter.saveSearchKey(tag,2,userMessage.getTsId(),vo.getAlbumName(),vo.getAlbumId());
                    if (type == MainRecommendPresenter.TYPE_MUISC) {
                        presenter.getSongList(userMessage.getTsId(),vo.getAlbumId(),null);
                       // presenter.startMusicActivity(vo.getAlbumId(), null);
                    } else {
                        presenter.startVideoActivity(vo.getAlbumId(), null);
                    }
                }
            });
        }*/
    }

    private List<ResourceVo> resourceVoList;

    private void initList() {
        resourceVoList = new ArrayList<>();
        searchResourceAdapter = new SearchResourceAdapter(this, resourceVoList);
        rv_search_play.setLayoutManager(new GridLayoutManager(this, 2));
        rv_search_play.setNestedScrollingEnabled(false);
        rv_search_play.setAdapter(searchResourceAdapter);
        searchResourceAdapter.setOnItemClickListener(new SearchResourceAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ResourceVo vo = resourceVoList.get(position);
                if (G.isEmteny(vo.getAlbumId()) || G.isEmteny(vo.getResourceId())) {
                    G.showToast(SearchPlayActivity.this, "该资源已经下架!");
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


    @Override
    public void setResourceList(final List<ResourceVo> resourceList) {
        lv_search_play_history.setVisibility(View.GONE);
        resourceVoList.addAll(resourceList);
        if (searchResourceAdapter != null) {
            searchResourceAdapter.notifyDataSetChanged();
        }
        /*if (resourceList.size() > 0 && resourceList != null) {
            if (searchResourceAdapter != null) {
                searchResourceAdapter = null;
            }
            searchResourceAdapter = new SearchResourceAdapter(this, resourceList);
            rv_search_play.setLayoutManager(new GridLayoutManager(this, 2));
            rv_search_play.setAdapter(searchResourceAdapter);
            searchResourceAdapter.setOnItemClickListener(new SearchResourceAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    ResourceVo vo = resourceList.get(position);
                    if (G.isEmteny(vo.getAlbumId()) || G.isEmteny(vo.getResourceId())) {
                        G.showToast(SearchPlayActivity.this, "该资源已经下架!");
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
        }*/
    }

    private boolean hasMoreData = true;

    @Override
    public void setResourceSize(int size) {
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        G.log("--------------------" + hasMoreData);
        tv_no_more_data.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
        pullToRefreshRecyclerView.setPullLoadEnabled(hasMoreData);
    }


    private void lastPage() {
        // ll_load_more.setVisibility(View.VISIBLE);
        //tv_load_more.setText("没有更多数据了");
        //iv_load_more.setVisibility(View.GONE);
        // ll_load_more.setClickable(false);
    }
}
