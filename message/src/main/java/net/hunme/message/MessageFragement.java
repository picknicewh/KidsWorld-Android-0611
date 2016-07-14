package net.hunme.message;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hunme.baselibrary.activity.BaseFragement;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.widget.NavigationBar;
import net.hunme.baselibrary.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MessageFragement extends BaseFragement {

    private PagerSlidingTabStrip pst;

    private List<android.support.v4.app.Fragment> fragmentlist;

    private NavigationBar navigationBar;

    private ViewPager viewpager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        init(view);
        setFragement();
        return view;
    }
   private  void init(View v){
        navigationBar = $(v,R.id.nb_message);
        pst = $(v,R.id.pst_meaasge);
        viewpager = $(v,R.id.vp_view);
        navigationBar.setTitle("消息");
       pst.setTextSize(G.dp2px(getActivity(), 18));//设置tab的字体大小
   }
    private  void  setFragement(){
        fragmentlist = new ArrayList<>();
        fragmentlist.add(new RecentlyFragement());
        fragmentlist.add(new ContracFragement());
        final String[] title = {"最近", "联系人"};

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentlist.size();
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {

                return fragmentlist.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        };
        viewpager.setAdapter(adapter);
        pst.setViewPager(viewpager);
    }
}
