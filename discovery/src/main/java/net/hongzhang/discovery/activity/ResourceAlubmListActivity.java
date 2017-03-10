package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.fragment.AllAlubmListFragment;
import net.hongzhang.discovery.fragment.SelectAlubmFramgment;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;

import java.util.ArrayList;
import java.util.List;

public class ResourceAlubmListActivity extends BaseActivity {
    /**
     * 页面的头部tab
     */
    private ViewPagerHead viewPagerHead;
    /**
     *页面
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
        if (type==MainRecommendPresenter.TYPE_MUISC){
            setCententTitle("幼儿听听");
        }else if (type==MainRecommendPresenter.TYPE_VIDEO){
            setCententTitle("幼儿课堂");
        }
        setRightImage(R.mipmap.ic_search);
        setLiftOnClickClose();
    }
    private void initview(){
        viewPagerHead = $(R.id.vph_music);
        viewPager = $(R.id.vp_music);
        initViewPager();
    }
    private void initViewPager(){
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("精选");
        titles.add("全部");
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        SelectAlubmFramgment alubmFramgment =  new SelectAlubmFramgment();
        alubmFramgment.setArguments(bundle);
        AllAlubmListFragment allAlubmFramgment =  new AllAlubmListFragment();
        allAlubmFramgment.setArguments(bundle);
        fragments.add(alubmFramgment);
        fragments.add(allAlubmFramgment);
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this,fragments,getSupportFragmentManager(),titles);
    }
}
