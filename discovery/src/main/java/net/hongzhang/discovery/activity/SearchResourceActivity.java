package net.hongzhang.discovery.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
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
     * tab标题
     */
    private List<String> titles;
    private SharedPreferences sp;
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
        titles = new ArrayList<>();
        sp = getSharedPreferences("USER",MODE_PRIVATE);
        editor = sp.edit();
        titles.add("幼儿听听");
        titles.add("幼儿课堂");
        titles.add("教育资讯");
    }
    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            SearchResourceListFragment fragment = new SearchResourceListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            //  bundle.putString("tag", tag);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        SearchConsultListFragment fragment = new SearchConsultListFragment();
        Bundle bundle = new Bundle();
        //  bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        fragments.add(fragment);
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragments, getSupportFragmentManager(), titles);
    }
    private SharedPreferences.Editor editor;
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            viewPagerHead.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            String tag = et_search_key.getText().toString();
            editor.putString("tag", tag);
            editor.commit();
            initViewPager();
            G.log("=============================++++"+sp.getString("tag",""));
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        }
    }
}
