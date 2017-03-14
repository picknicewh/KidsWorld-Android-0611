package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollListView;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.adapter.SearchHistoryAdapter;
import net.hongzhang.discovery.modle.ResourceVo;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.presenter.SearchConsultContract;
import net.hongzhang.discovery.presenter.SearchConsultPresenter;

import java.util.List;

import static android.R.attr.type;

/**
 * 搜索音乐或者视频
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
    private final static int pageSize = 10;
    /**
     * 页面
     */
    private NoScrollListView lv_search_consult;

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
    private LinearLayout ll_search_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_consult);
        initView();
    }

    private void initView() {
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        tv_clean = $(R.id.tv_clean);
        lv_search_consult = $(R.id.lv_search_consult);
        lv_search_counslt_history = $(R.id.lv_search_counslt_history);
        ll_search_content = $(R.id.ll_search_content);
        ll_load_more = $(R.id.ll_load_more);
        tv_load_more = $(R.id.tv_load_more);
        iv_load_more = $(R.id.iv_load_more);
        ll_load_more.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        et_search_key.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
        presenter = new SearchConsultPresenter(SearchConsultActivity.this, this);
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
        if (resourceList.size() > 0 && resourceList != null) {
            ConsultAdapter adapter = new ConsultAdapter(this, resourceList);
            lv_search_consult.setAdapter(adapter);
            lv_search_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ResourceVo vo = resourceList.get(position);
                    presenter.startConsultActivity(vo.getResourceId());
                }
            });
        }
    }

    @Override
    public void setSearchHistoryList(final List<SearchKeyVo> searchKeyVoList) {
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, searchKeyVoList);
        lv_search_counslt_history.setAdapter(adapter);
        lv_search_counslt_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                pageNumber=1;
                ll_search_content.setVisibility(View.VISIBLE);
                lv_search_counslt_history.setVisibility(View.GONE);
                tag = searchKeyVoList.get(position).getKey();
                et_search_key.setText(tag);
                presenter.getSearchResourceList(userMessage.getTsId(), type, pageSize, pageNumber, userMessage.getAccount_id(), tag);
            }
        });
    }
    @Override
    public void setConsultInfoSize(int size) {
        if (size == 0 || size < pageSize) {
            lastPage();
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            pageNumber=1;
            lv_search_counslt_history.setVisibility(View.GONE);
            ll_search_content.setVisibility(View.VISIBLE);
            tag = et_search_key.getText().toString();
            presenter.insertKey(tag);
            presenter.getSearchHistoryList();
            presenter.getSearchResourceList(userMessage.getTsId(), 3, pageSize, pageNumber, userMessage.getAccount_id(), tag);
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        } else if (view.getId() == R.id.ll_load_more) {
            pageNumber++;
            presenter.getSearchResourceList(userMessage.getTsId(), 3, pageSize, pageNumber, userMessage.getAccount_id(), tag);
        }else if (viewId==R.id.et_search_key){
            lv_search_counslt_history.setVisibility(View.VISIBLE);
        }
    }

    private void lastPage() {
        ll_load_more.setVisibility(View.VISIBLE);
        tv_load_more.setText("没有更多数据了");
        iv_load_more.setVisibility(View.GONE);
        ll_load_more.setClickable(false);
    }
}
