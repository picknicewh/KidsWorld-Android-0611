package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshBase;
import net.hongzhang.baselibrary.pullrefresh.PullToRefreshListView;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.adapter.SearchHistoryAdapter;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.SearchConsultContract;
import net.hongzhang.discovery.presenter.SearchConsultPresenter;
import net.hongzhang.discovery.util.TextUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2017/3/5
 * 名称：搜索的资讯列表
 * 版本说明：
 * 附加注释：
 * 主要接口：获取搜索资讯列表
 */
public class SearchConsultActivity extends BaseActivity implements View.OnClickListener, SearchConsultContract.View {
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
    //  private LinearLayout ll_load_more;
    // private TextView tv_load_more;
    //  private ImageView iv_load_more;
    /**
     * 页码数
     */
    private int pageNumber = 1;
    /**
     * 一页显示数据条数
     */
    private final static int pageSize = 10;
    /**
     * 页面
     */
    private ListView lv_search_consult;

    /**
     * 数据处理
     */
    private SearchConsultPresenter presenter;
    private UserMessage userMessage;
    /**
     * 关键字
     */
    private String tag;
    /**
     * 资讯历史记录
     */
    private ListView lv_search_counslt_history;
 //   private LinearLayout ll_search_content;
    private PullToRefreshListView pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_consult);
        initView();
    }

    private void initView() {
        //     ll_load_more = $(R.id.ll_load_more);
        // tv_load_more = $(R.id.tv_load_more);
        // iv_load_more = $(R.id.iv_load_more);
        // ll_load_more.setOnClickListener(this);

        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        tv_clean = $(R.id.tv_clean);
       // lv_search_consult = $(R.id.lv_search_consult);
        lv_search_counslt_history = $(R.id.lv_search_counslt_history);
     //   ll_search_content = $(R.id.ll_search_content);
        pullToRefreshListView = $(R.id.pullToRefreshListView);
        lv_search_consult = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(false);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        pageNumber++;
                        presenter.getSearchResourceList(userMessage.getTsId(), pageSize, pageNumber, userMessage.getAccount_id(), tag);
                        refreshView.onPullUpRefreshComplete();
                        pullToRefreshListView.setHasMoreData(hasMoreData);

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
        presenter = new SearchConsultPresenter(SearchConsultActivity.this, this);
    }

    private List<ResourceVo> resourceVoList;
    private ConsultAdapter adapter;

    private void initList() {
        resourceVoList = new ArrayList<>();
        adapter = new ConsultAdapter(this, resourceVoList);
        lv_search_consult.setAdapter(adapter);
        lv_search_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ResourceVo vo = resourceVoList.get(position);
                if (G.isEmteny(vo.getResourceId())) {
                    G.showToast(SearchConsultActivity.this, "该资源已经下架！");
                } else {
                    presenter.startConsultActivity(vo.getResourceId());
                }
                presenter.saveSearchKey(tag, userMessage.getTsId(), vo.getResourceName(), vo.getResourceId());
            }
        });
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("搜索资讯");
        setRightImage(R.mipmap.ic_search);
        setLiftOnClickClose();
    }
    @Override
    public void setConsultList(final List<ResourceVo> resourceList) {
        lv_search_counslt_history.setVisibility(View.GONE);
        resourceVoList.addAll(resourceList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void setSearchHistoryList(final List<SearchKeyVo> searchKeyVoList) {
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, searchKeyVoList);
        lv_search_counslt_history.setAdapter(adapter);
        lv_search_counslt_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                pageNumber = 1;
               // ll_search_content.setVisibility(View.VISIBLE);
                lv_search_counslt_history.setVisibility(View.GONE);
                tag = searchKeyVoList.get(position).getKey();
                et_search_key.setText(tag);
                presenter.getSearchResourceList(userMessage.getTsId(), pageSize, pageNumber, userMessage.getAccount_id(), tag);
            }
        });
    }

    private boolean hasMoreData;

    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0 || size < pageSize) {
            hasMoreData = false;
            // lastPage();
        } else {
            hasMoreData = true;
        }

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            pageNumber = 1;
            lv_search_counslt_history.setVisibility(View.GONE);
         //   ll_search_content.setVisibility(View.VISIBLE);
            tag = et_search_key.getText().toString().trim();
            if (!TextUtil.isAllSpace(tag)) {
                presenter.insertKey(tag);
                presenter.getSearchHistoryList();
                presenter.getSearchResourceList(userMessage.getTsId(), pageSize, pageNumber, userMessage.getAccount_id(), tag);
            } else {
                G.showToast(this, "搜索关键词不能为空");
                et_search_key.setText("");
            }
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        } else if (view.getId() == R.id.ll_load_more) {

        } else if (viewId == R.id.et_search_key) {
            lv_search_counslt_history.setVisibility(View.VISIBLE);
        }
    }

    private void lastPage() {
        // ll_load_more.setVisibility(View.VISIBLE);
        //  tv_load_more.setText("没有更多数据了");
        //  iv_load_more.setVisibility(View.GONE);
        //  ll_load_more.setClickable(false);
    }
}
