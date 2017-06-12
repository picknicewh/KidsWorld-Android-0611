package net.hongzhang.school.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wanghua on 2016/12/21.
 */
public class MySimplePagerAdapter extends FragmentPagerAdapter {
    private List<String> title;
    private List<Fragment> fragments;
    public MySimplePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
        super(fm);
        this.title  = title;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
}
