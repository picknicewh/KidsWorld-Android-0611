package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;
import net.hunme.school.fragment.MedicineFeedListFragment;
import net.hunme.school.fragment.MedicineProcessFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：学生喂药界面
 * 版本说明：
 * 附加注释：
 * 主要接口：
*/
public class MedicineListSActivity extends BaseActivity  implements View.OnClickListener {
    /**
     * 页面
     */
    private ViewPager vp_vp_feed_t;
    /**
     * 喂药列表
     */
    private RadioButton rb_feed_list;
    /**
     * 喂药进程
     */
    private RadioButton rb_feed_process;
    /**
     * 喂药列表下划线
     */
    private View line_feed;
    /**
     * 喂药进程下划线
     */
    private View line_process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_s_list);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("喂药");
        setSubTitle("委托");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineListSActivity.this,MedicineEntrustActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initView(){
        vp_vp_feed_t = $(R.id.vp_feed_t);
        rb_feed_list = $(R.id.rb_feed_list);
        rb_feed_process=  $(R.id.rb_feed_process);
        line_feed = $(R.id.line_feed);
        line_process = $(R.id.line_process);
        rb_feed_list.setOnClickListener(this);
        rb_feed_process.setOnClickListener(this);
        setViewpager();
    }
    private void setViewpager(){
        final List<Fragment> fragments = new ArrayList<>();
        MedicineFeedListFragment listFragment = new MedicineFeedListFragment();
        MedicineProcessFragment processFragment = new MedicineProcessFragment();
        fragments.add(listFragment);
        fragments.add(processFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        vp_vp_feed_t.setAdapter(adapter);
        vp_vp_feed_t.setCurrentItem(0);
        vp_vp_feed_t.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
     *设置下划线
     * @param position 页面的位置
     */
    private void setline(int position){
        if (position==0){
            line_feed.setBackgroundColor(getResources().getColor(R.color.main_green));
            line_process.setBackgroundColor(getResources().getColor(R.color.white));
            rb_feed_list.setChecked(true);
            rb_feed_process.setChecked(false);
        }else {
            line_process.setBackgroundColor(getResources().getColor(R.color.main_green));
            line_feed.setBackgroundColor(getResources().getColor(R.color.white));
            rb_feed_list.setChecked(false);
            rb_feed_process.setChecked(true);

        }
        vp_vp_feed_t.setCurrentItem(position);
    }
    @Override
    public void onClick(View view) {
        int viewId =  view.getId();
        if (viewId==R.id.rb_feed_list){
            setline(0);
        }else if (viewId==R.id.rb_feed_process){
            setline(1);
        }
    }

    /**
     * 适配器
     */
    private class  FragmentAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragmentList;
        public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList  =fragmentList;
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
