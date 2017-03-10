package net.hongzhang.baselibrary.util;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 适配器
 */
public class FragmentAdapter extends FragmentPagerAdapter {
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

