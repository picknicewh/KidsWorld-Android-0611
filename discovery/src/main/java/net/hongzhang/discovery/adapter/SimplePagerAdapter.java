package net.hongzhang.discovery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wanghua on 2016/12/21.
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {
    private String[]  title;
    private List<Fragment> fragments;
    public SimplePagerAdapter(FragmentManager fm, List<Fragment> fragments, String[]  title) {
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
        return title[position];
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
}
