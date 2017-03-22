package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.CompilationAdapter;
import net.hongzhang.discovery.adapter.SearchHistoryAdapter;
import net.hongzhang.discovery.adapter.SearchResourceAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.ResourceVo;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.presenter.SearchResourceContract;
import net.hongzhang.discovery.presenter.SearchResourcePresenter;

import java.util.List;

/**
 * 搜索音乐或者视频
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
    private final static int pageSize = 6;
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
    private LinearLayout ll_search_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_play);
        initView();
    }

    private void initView() {
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        tv_clean = $(R.id.tv_clean);
        rv_search_play = $(R.id.rv_search_play);
        lv_search_play_history = $(R.id.lv_search_play_history);
        ll_search_content = $(R.id.ll_search_content);
        ll_load_more = $(R.id.ll_load_more);
        tv_load_more = $(R.id.tv_load_more);
        iv_load_more = $(R.id.iv_load_more);
        ll_load_more.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        et_search_key.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
        presenter = new SearchResourcePresenter(SearchPlayActivity.this, this);
        type = getIntent().getIntExtra("type", MainRecommendPresenter.TYPE_MUISC);
        presenter.getSearchHistoryList(type);
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
            pageNumber = 1;
            lv_search_play_history.setVisibility(View.GONE);
            ll_search_content.setVisibility(View.VISIBLE);
            tag = et_search_key.getText().toString();
            presenter.insertKey(type, tag);
            presenter.getSearchHistoryList(type);
            presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 1);
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        } else if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 2);
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
                pageNumber = 1;
                ll_search_content.setVisibility(View.VISIBLE);
                lv_search_play_history.setVisibility(View.GONE);
                tag = searchKeyVoList.get(position).getKey();
                presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag, 1);
            }
        });
    }

    @Override
    public void setloadMoreVis(boolean isVis) {
        ll_load_more.setVisibility(isVis ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCompilationVoList(final List<CompilationVo> musicCompilationVos) {
        lv_search_play_history.setVisibility(View.GONE);
        if (musicCompilationVos.size() > 0 && musicCompilationVos != null) {
            CompilationAdapter adapter = new CompilationAdapter(this, musicCompilationVos);
            rv_search_play.setLayoutManager(new GridLayoutManager(this, 2));
            rv_search_play.setAdapter(adapter);
            rv_search_play.setNestedScrollingEnabled(false);
            adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    CompilationVo vo = musicCompilationVos.get(position);
                    if (type == MainRecommendPresenter.TYPE_MUISC) {
                        presenter.startMusicActivity(vo.getAlbumId(), null);
                    } else {
                        presenter.startVideoActivity(vo.getAlbumId(), null);
                    }
                }
            });
        }
    }

    @Override
    public void setResourceList(final List<ResourceVo> resourceList) {
        lv_search_play_history.setVisibility(View.GONE);
        if (resourceList.size() > 0 && resourceList != null) {
            SearchResourceAdapter adapter = new SearchResourceAdapter(this, resourceList);
            rv_search_play.setLayoutManager(new GridLayoutManager(this, 2));
            rv_search_play.setAdapter(adapter);
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

    @Override
    public void setResourceSize(int size) {
        Log.i("TAG", "------------------------------" + size);
        if (size == 0 || size < pageSize) {
            lastPage();
        }
    }


    private void lastPage() {
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
        ll_load_more.setClickable(false);
    }
}
