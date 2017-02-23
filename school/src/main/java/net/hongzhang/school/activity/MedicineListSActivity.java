package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.MedicineSVos;
import net.hongzhang.school.bean.MedicineSchedule;
import net.hongzhang.school.bean.MedicineVo;
import net.hongzhang.school.fragment.MedicineFeedListFragment;
import net.hongzhang.school.fragment.MedicineProcessFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：学生喂药界面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineListSActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    /**
     * 一页条数
     */
    private static final int pageSize = 999;
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
    /**
     * 页码
     */
    private int pageNumber = 1;
    /**
     * 当前页面位置
     */
    private int currentPage = 0;
    private MedicineFeedListFragment listFragment;
    private MedicineProcessFragment processFragment;
    /**
     *  无网络状态
     */
    private RelativeLayout rl_nonetwork;
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
                Intent intent = new Intent(MedicineListSActivity.this, MedicineEntrustActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        rl_nonetwork = $(R.id.rl_nonetwork);
        vp_vp_feed_t = $(R.id.vp_feed_t);
        rb_feed_list = $(R.id.rb_feed_list);
        rb_feed_process = $(R.id.rb_feed_process);
        line_feed = $(R.id.line_feed);
        line_process = $(R.id.line_process);
        rb_feed_list.setOnClickListener(this);
        rb_feed_process.setOnClickListener(this);
        setViewpager(currentPage);
    }

    private void getFeedList() {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        Type type = new TypeToken<Result<MedicineSVos>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINESLIST, map, this);
        if (G.isNetworkConnected(this)){
            rl_nonetwork.setVisibility(View.GONE);
            showLoadingDialog();
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
        }
    }
    private void setViewpager(int currentPage) {
        List<Fragment> fragments = new ArrayList<>();
        listFragment = new MedicineFeedListFragment();
        processFragment = new MedicineProcessFragment();
        fragments.add(listFragment);
        fragments.add(processFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp_vp_feed_t.setAdapter(adapter);
        vp_vp_feed_t.setCurrentItem(currentPage);
        vp_vp_feed_t.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setline(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 设置下划线
     *
     * @param position 页面的位置
     */
    private void setline(int position) {
        currentPage = position;
        if (position == 0) {
            line_feed.setBackgroundColor(getResources().getColor(R.color.main_green));
            line_process.setBackgroundColor(getResources().getColor(R.color.white));
            rb_feed_list.setChecked(true);
            rb_feed_process.setChecked(false);
        } else {
            line_process.setBackgroundColor(getResources().getColor(R.color.main_green));
            line_feed.setBackgroundColor(getResources().getColor(R.color.white));
            rb_feed_list.setChecked(false);
            rb_feed_process.setChecked(true);
        }
        vp_vp_feed_t.setCurrentItem(position);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rb_feed_list) {
            setline(0);
        } else if (viewId == R.id.rb_feed_process) {
            setline(1);
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        stopLoadingDialog();
        if (Apiurl.SCHOOL_MEDICINESLIST.equals(uri)) {
            Result<MedicineSVos> data = (Result<MedicineSVos>) date;
            if (data != null) {
                MedicineSVos medicineSVos = data.getData();
                List<MedicineVo> medicineVos = medicineSVos.getMedicineList();
                List<MedicineSchedule> medicineScheduleVos = medicineSVos.getMedicineScheduleJson();
                listFragment.setMedicineVo(medicineVos);
                processFragment.setMedicineScheduleVos(medicineScheduleVos);
                Log.i("nnnnnnnn", "============222222==================" + medicineVos.size());
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("nnnnnnnn", "================onResume===================");
        getFeedList();
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
