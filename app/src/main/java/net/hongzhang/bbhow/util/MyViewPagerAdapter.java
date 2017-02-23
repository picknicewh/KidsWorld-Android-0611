package net.hongzhang.bbhow.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/29
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter{
    private int mSize;
    private List<Fragment> fragmentList;
    private long baseId = 0;

    public MyViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
        mSize = fragmentList == null ? 0 : fragmentList.size();
    }
    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

}
