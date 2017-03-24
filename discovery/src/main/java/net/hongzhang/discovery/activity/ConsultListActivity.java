package net.hongzhang.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.fragment.ConsultListFragment;
import net.hongzhang.discovery.modle.ThemeVo;
import net.hongzhang.discovery.presenter.ConsultListContract;
import net.hongzhang.discovery.presenter.ConsultListPresenter;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;

import java.util.ArrayList;
import java.util.List;

public class ConsultListActivity extends BaseActivity implements ConsultListContract.View, View.OnClickListener {

    /**
     * 页面的头部tab
     */
    private ViewPagerHead viewPagerHead;
    /**
     * 页面
     */
    private ViewPager viewPager;
    /**
     * 数据处理
     */
    private ConsultListPresenter presenter;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private int pageNumber = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_list);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("教育资讯");
        setLiftOnClickClose();
        setRightImage(R.mipmap.ic_search);
        setRightOnClickListener(this);
    }

    private void initView() {
        viewPagerHead = $(R.id.vph_consult);
        viewPager = $(R.id.vp_consult);
        fragmentList = new ArrayList<>();
        presenter = new ConsultListPresenter(ConsultListActivity.this, this);
        presenter.getThemeList(MainRecommendPresenter.TYPE_CONSULT, pageNumber, pageSize);
    }
    @Override
    public void setThemeList(List<ThemeVo> themeList) {
        titles = new ArrayList<>();
        for (int i = 0; i < themeList.size(); i++) {
            ConsultListFragment fragment = new ConsultListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("themeId", themeList.get(i).getThemeId());
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        for (int i = 0; i < themeList.size(); i++) {
            String title = themeList.get(i).getThemeName();
            titles.add(title);
        }
        viewPagerHead.setTabcount(titles.size());
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragmentList, getSupportFragmentManager(), titles);

    }

    @Override
    public void onClick(View view) {
        Intent intent  = new Intent(this,SearchConsultActivity.class);
        startActivity(intent);
    }
}
