package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.fragment.SearchConsultListFragment;
import net.hongzhang.discovery.fragment.SearchResourceListFragment;

import java.util.ArrayList;
import java.util.List;

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
     * 页面列表
     */
    private List<Fragment> fragmentList;
    /**
     * tab标题
     */
    private List<String> titles;
    /**
     * 角色id
     */
    private UserMessage userMessage;

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
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
        viewPager = $(R.id.vp_search);
        viewPagerHead = $(R.id.vph_search);
        tv_clean = $(R.id.tv_clean);
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
    }

    private void initViewPager(String tag) {
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("幼儿听听");
        titles.add("幼儿课堂");
        titles.add("教育资讯");
        for (int i = 1; i <= 2; i++) {
            SearchResourceListFragment fragment = new SearchResourceListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            bundle.putString("tag", tag);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        SearchConsultListFragment fragment = new SearchConsultListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        fragmentList.add(fragment);
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragmentList, getSupportFragmentManager(), titles);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            viewPagerHead.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            String tag = et_search_key.getText().toString();
            initViewPager(tag);
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        }
    }
}
