package net.hongzhang.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.fragment.SelectAlubmFragment;
import net.hongzhang.discovery.fragment.ThemeListFragment;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;

import java.util.ArrayList;
import java.util.List;
/**
 * 作者： wh
 * 时间： 2017/3/5
 * 名称： 幼儿听听或者幼儿课堂页面
 * 版本说明：
 * 附加注释：根据type的不同进入不同的页面 type=2 幼儿听听 type=1 幼儿课堂
 * 主要接口：
 */
public class ResourceAlubmListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 页面的头部tab
     */
    private ViewPagerHead viewPagerHead;
    /**
     * 页面
     */
    private ViewPager viewPager;
    /**
     * 标题
     */
    private List<String> titles;
    /**
     * 页面
     */
    private List<Fragment> fragments;
    /**
     * 类型
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_alubm_list);
        initview();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        type = getIntent().getIntExtra("type", MainRecommendPresenter.TYPE_MUISC);
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            setCententTitle("幼儿听听");
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            setCententTitle("幼儿课堂");
        }
        setRightImage(R.mipmap.ic_search);
        setRightOnClickListener(this);
        setLiftOnClickClose();
    }

    private void initview() {
        viewPagerHead = $(R.id.vph_music);
        viewPager = $(R.id.vp_music);
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("精选");
        titles.add("全部");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        SelectAlubmFragment alubmFragment = new SelectAlubmFragment();
        alubmFragment.setArguments(bundle);
        ThemeListFragment allAlubmFramgment = new ThemeListFragment();
        allAlubmFramgment.setArguments(bundle);
        fragments.add(alubmFragment);
        fragments.add(allAlubmFramgment);
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragments, getSupportFragmentManager(), titles);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, SearchPlayActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
