package net.hongzhang.user.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;
import net.hongzhang.user.fragment.ConsultListFragment;
import net.hongzhang.user.fragment.ResourceListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/25
 * 名称：我的收藏和园所记录页面
 * 版本说明：
 * 附加注释：通过传递不同的source值来判断是哪个页面
 * 主要接口：
 */
public class CollectActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 幼儿听听
     */
    private RadioButton rb_music;
    private View v_music;
    /**
     * 幼儿课堂
     */
    private RadioButton rb_lesson;
    private View v_lesson;
    /**
     * 教育资讯
     */
    private RadioButton rb_consult;
    private View v_consult;
    /**
     * 页面
     */
    private ViewPager vp_collect;
    private   int source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
    }

    private void initView() {
        rb_music = $(R.id.rb_music);
        v_music = $(R.id.v_music);
        rb_lesson = $(R.id.rb_lesson);
        v_lesson = $(R.id.v_lesson);
        rb_consult = $(R.id.rb_consult);
        v_consult = $(R.id.v_consult);
        vp_collect = $(R.id.vp_collect);
        rb_consult.setOnClickListener(this);
        rb_lesson.setOnClickListener(this);
        rb_music.setOnClickListener(this);
        source =  getIntent().getIntExtra("source",0);
        setviewPager();
    }
    private ResourceListFragment getResourceListFragment(int source,int type){
        ResourceListFragment resourceListFragment = new ResourceListFragment();
        resourceListFragment.setArguments(getBundle(source,type));
        return resourceListFragment;
    }
    private  Bundle  getBundle(int source,int type){
        Bundle bundle = new Bundle();
        bundle.putInt("source",source);
        bundle.putInt("type",type);
        return bundle;
    }
    private void setviewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        ConsultListFragment consultFragment = new ConsultListFragment();
        consultFragment.setArguments(getBundle(source,3));
        fragmentList.add(getResourceListFragment(source,2));
        fragmentList.add(getResourceListFragment(source,1));
        fragmentList.add(consultFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        vp_collect.setAdapter(adapter);
        vp_collect.setCurrentItem(0);
        vp_collect.setOffscreenPageLimit(fragmentList.size()-1);
        vp_collect.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setline(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /**
     * 设置下划线
     *
     * @param position 页面的位置
     */
    private void setline(int position) {
        if (position == 0) {
            v_music.setBackgroundColor(getResources().getColor(R.color.main_text_green));
            v_lesson.setBackgroundColor(getResources().getColor(R.color.white));
            v_consult.setBackgroundColor(getResources().getColor(R.color.white));
            rb_music.setChecked(true);
            rb_lesson.setChecked(false);
            rb_consult.setChecked(false);
        } else if (position == 1) {
            v_music.setBackgroundColor(getResources().getColor(R.color.white));
            v_lesson.setBackgroundColor(getResources().getColor(R.color.main_text_green));
            v_consult.setBackgroundColor(getResources().getColor(R.color.white));
            rb_music.setChecked(false);
            rb_lesson.setChecked(true);
            rb_consult.setChecked(false);
        } else if (position == 2) {
            v_music.setBackgroundColor(getResources().getColor(R.color.white));
            v_lesson.setBackgroundColor(getResources().getColor(R.color.white));
            v_consult.setBackgroundColor(getResources().getColor(R.color.main_text_green));
            rb_music.setChecked(false);
            rb_lesson.setChecked(false);
            rb_consult.setChecked(true);
        }
        vp_collect.setCurrentItem(position);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
         source =  getIntent().getIntExtra("source",0);
        if (source==0){
            setCententTitle("收藏");
        }else {
            setCententTitle("播放记录");
        }
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rb_music) {
            setline(0);
        } else if (viewId == R.id.rb_lesson) {
            setline(1);
        } else if (viewId == R.id.rb_consult) {
            setline(2);
        }
    }
    /**
     * 适配器
     */
    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
