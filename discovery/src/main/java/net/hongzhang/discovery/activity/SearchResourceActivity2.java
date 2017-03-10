package net.hongzhang.discovery.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.FragmentAdapter;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.fragment.SearchConsultListFragment;
import net.hongzhang.discovery.fragment.SearchResourceListFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchResourceActivity2 extends BaseActivity implements View.OnClickListener {
    /**
     * 搜索框
     */
    private EditText et_search_key;
    /**
     * 搜索
     */
    private TextView tv_search;
    /**
     * 清除
     */
    private TextView tv_clean;
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
    /**
     * 角色id
     */
    private UserMessage userMessage;
    private SharedPreferences sp;
    private LinearLayout ll_search_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resource2);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("搜索");
        setLiftOnClickClose();
    }

    private void initView() {
        et_search_key = $(R.id.et_search_key);
        tv_search = $(R.id.tv_search);
     //   viewPager = $(R.id.vp_search);
        tv_clean = $(R.id.tv_clean);
        rb_music = $(R.id.rb_music);
        v_music = $(R.id.v_music);
        rb_lesson = $(R.id.rb_lesson);
        v_lesson = $(R.id.v_lesson);
        rb_consult = $(R.id.rb_consult);
        v_consult = $(R.id.v_consult);
        vp_collect = $(R.id.vp_collect);
        ll_search_content = $(R.id.ll_search_content);
        rb_consult.setOnClickListener(this);
        rb_lesson.setOnClickListener(this);
        rb_music.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        userMessage = UserMessage.getInstance(this);
        sp = getSharedPreferences("USER",MODE_PRIVATE);
        editor = sp.edit();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            SearchResourceListFragment fragment = new SearchResourceListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        fragments.add(new SearchConsultListFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp_collect.setAdapter(adapter);
        vp_collect.setCurrentItem(0);
        vp_collect.setOffscreenPageLimit(fragments.size() - 1);
        vp_collect.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setLine(position);
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
    private void setLine(int position) {
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

    private SharedPreferences.Editor editor;
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_search) {
            ll_search_content.setVisibility(View.VISIBLE);
            String tag = et_search_key.getText().toString();
            editor.putString("tag", tag);
            editor.commit();
            initViewPager();
        } else if (viewId == R.id.tv_clean) {
            et_search_key.setText("");
        }else  if (viewId == R.id.rb_music) {
            setLine(0);
        } else if (viewId == R.id.rb_lesson) {
            setLine(1);
        } else if (viewId == R.id.rb_consult) {
            setLine(2);
        }
    }
}
