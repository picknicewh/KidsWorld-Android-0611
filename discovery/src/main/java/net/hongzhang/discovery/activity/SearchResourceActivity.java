package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.SearchHistoryAdapter;
import net.hongzhang.discovery.fragment.SearchConsultListFragment;
import net.hongzhang.discovery.fragment.SearchResourceListFragment;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.util.SearchHistoryDb;
import net.hongzhang.discovery.util.SearchHistoryDbHelper;
import net.hongzhang.discovery.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2017/3/5
 * 名称： 搜索音乐或者视频，资讯
 * 版本说明：
 * 附加注释：SearchHistoryDb 添加本地已经的搜索的关键字数据库
 * 主要接口：
 */
public class SearchResourceActivity extends BaseActivity implements View.OnClickListener {
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
     * tab选项
     */
    private ViewPagerHead viewPagerHead;
    /**
     * 页面
     */
    private ViewPager viewPager;
    /**
     * tab标题
     */
    private List<String> titles;
    private LinearLayout ll_search_content;
    private ListView lv_search_history_list;
    private List<SearchKeyVo> searchKeyVos;
    /**
     * 搜索历史数据库
     */
    private SearchHistoryDb db;
    private SearchHistoryDbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resource);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("搜索");
        setLiftOnClickClose();
    }

    private void initView() {
        ll_search_content = $(R.id.ll_search_content);
        lv_search_history_list = $(R.id.lv_search_history_list);
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        viewPager = $(R.id.vp_search);
        viewPagerHead = $(R.id.vph_search);
        tv_clean = $(R.id.tv_clean);
        tv_clean.setOnClickListener(this);
        et_search_key.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        et_search_key.setOnClickListener(this);
        titles = new ArrayList<>();
        titles.add("幼儿听听");
        titles.add("幼儿课堂");
        titles.add("教育资讯");
        setSearchHistoryList();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            SearchResourceListFragment fragment = new SearchResourceListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", 3 - i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        fragments.add(new SearchConsultListFragment());
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragments, getSupportFragmentManager(), titles);
    }

    private void setSearchHistoryList() {
        db = new SearchHistoryDb(this);
        helper = SearchHistoryDbHelper.getinstance();
        searchKeyVos = helper.getKeyList(db.getReadableDatabase(), SearchHistoryDb.TABLENAME);
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, searchKeyVos);
        lv_search_history_list.setAdapter(adapter);
        lv_search_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ll_search_content.setVisibility(View.VISIBLE);
                lv_search_history_list.setVisibility(View.GONE);
                tag = searchKeyVos.get(position).getKey();
                et_search_key.setText(tag);
                initViewPager();
            }
        });
    }

    public static String tag;

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            lv_search_history_list.setVisibility(View.GONE);
            ll_search_content.setVisibility(View.VISIBLE);
            tag = et_search_key.getText().toString().trim();
            if (TextUtil.isAllSpace(tag)) {
                G.showToast(this, "搜索关键词不能为空");
                et_search_key.setText("");
            } else {
                helper.insert(db.getWritableDatabase(), tag, SearchHistoryDb.TABLENAME);
                setSearchHistoryList();
                initViewPager();
            }
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
            lv_search_history_list.setVisibility(View.GONE);
        } else if (viewId == R.id.et_search_key) {
            lv_search_history_list.setVisibility(View.VISIBLE);
        }
    }
}
