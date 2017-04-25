package net.hongzhang.school.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.ViewPagerHead;
import net.hongzhang.school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2017/4/19
 * 名称：家园互动页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HomeInteractionSActivity extends BaseActivity {
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
        setContentView(R.layout.activity_home_interaction);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("家园互动");
        setLiftOnClickClose();
    }

    private void initView() {
        viewPagerHead = $(R.id.vph_home);
        viewPager = $(R.id.vp_home);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        type =Integer.parseInt(UserMessage.getInstance(this).getType());
        initViewPager();
    }
    private void initViewPager() {

        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        titles.add("已发布");
        titles.add("已完成");
        //  SelectAlubmFragment alubmFragment = new SelectAlubmFragment();
        //   alubmFragment.setArguments(bundle);
        //   ThemeListFragment allAlubmFramgment = new ThemeListFragment();
        //  allAlubmFramgment.setArguments(bundle);
        // fragments.add(alubmFragment);
        //  fragments.add(allAlubmFramgment);
        viewPagerHead.setViewPager(viewPager);
        viewPagerHead.setAdapter(this, fragments, getSupportFragmentManager(), titles);
    }
}
