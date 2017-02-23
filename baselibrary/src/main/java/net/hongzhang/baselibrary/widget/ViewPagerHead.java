package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/12/16
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ViewPagerHead extends LinearLayout {
    /**
     * 默认字体字体颜色
     */
    private static  final  int  DEFAULTTEXTCOLOR  = Color.parseColor("#494949");
    /**
     * 默认选择字体颜色
     */
    private static  final  int SELECTTEXTCOLOR = Color.parseColor("#8ec500");
    /**
     * 默认选中下划线的颜色
     */
    private static  final  int SELECTUNDERLINECOLOR= Color.parseColor("#8ec500");
    /**
     * 默认未选中下划线的颜色
     */
    private static  final  int DEFAULTUNDERLINECOLOR= Color.parseColor("#efeff4");
    /**
     * 默认背景的颜色
     */
    private static  final  int BACKGROUNDCOLOR= Color.parseColor("#efeff4");
    /**
     * 默认背景的颜色
     */
    private static  final  int DEFAULTUNDERLINEHEIGHT= 2;

    /**
     * 加载头部的viewPager
     */
    private ViewPager viewPager;
    /**
     * 下划线的颜色
     */
    private int selectUnderLinecolor;
    /**
     * 下划线的颜色
     */
    private int defalutUnderLinecolor;
    /**
     *选中文字的颜色
     */
    private int selectTextcolor;
    /**
     * 没选中文字的颜色
     */
    private int defaultTextcolor;
    /**
     * 背景颜色
     */
    private int backgroundColor;
    /**
     * 下滑线的高度
     */
    private int underLineHeight;
    /**
     * 选项卡的个数
     */
    private int Tabcount;

    /**
     * 选项卡的title
     */
    private List<String>  titles;
    /**
     * 每个选项页面
     */
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;
    public ViewPagerHead(Context context) {
        this(context,null);
    }
    public ViewPagerHead(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ViewPagerHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerHead);
        selectUnderLinecolor = array.getColor(R.styleable.ViewPagerHead_vphUnderlineColor,SELECTUNDERLINECOLOR);
        defalutUnderLinecolor = array.getColor(R.styleable.ViewPagerHead_vphUnderlineColor,DEFAULTUNDERLINECOLOR);
        underLineHeight = (int) array.getDimension(R.styleable.ViewPagerHead_vphUnderlineHeight,DEFAULTUNDERLINEHEIGHT);
        selectTextcolor = array.getColor(R.styleable.ViewPagerHead_vphSelectTextColor,SELECTTEXTCOLOR);
        defaultTextcolor = array.getColor(R.styleable.ViewPagerHead_vphDefaultTextColor,DEFAULTTEXTCOLOR);
        backgroundColor = array.getColor(R.styleable.ViewPagerHead_vphTabBackground,BACKGROUNDCOLOR);
        Tabcount = array.getInt(R.styleable.ViewPagerHead_vphTabcount,2);
        init(context);
    }
    private void init(final Context context){
        titles = new ArrayList<>();
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.WHITE);
        drawHead(context,0);
        drawLine(context,0);
    }
    public void setAdapter(final Context context,List<Fragment> fragments,FragmentManager fragmentManager){
        if (fragmentManager!=null&& fragments!=null){
            viewPager.setAdapter(new FragmentAdapter(fragmentManager,fragments));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    ViewPagerHead.this.removeAllViews();
                    drawHead(context,position);
                    drawLine(context,position);
                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
    private void drawHead(Context context,int checkedPosition){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundColor(backgroundColor);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(layoutParams);
        for (int i= 0 ;i < Tabcount;i++ ){
            TextView textView = getTextView(context,i);
            if (i==checkedPosition){
                textView.setTextColor(selectTextcolor);
            }else {
                textView.setTextColor(defaultTextcolor);
            }
            linearLayout.addView(textView);
        }
        this.addView(linearLayout);
    }
    private TextView getTextView(final Context context, final int i){
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        int padding = G.dp2px(context,15);
        layoutParams.setMargins(padding,padding,padding,padding);
        textView.setLayoutParams(layoutParams);
        textView.setTag(i);
        if (titles.size()>0){
            textView.setText(titles.get(i));
        }
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerHead.this.removeAllViews();
                drawHead(context, (Integer) view.getTag());
                drawLine(context, (Integer) view.getTag());
                viewPager.setCurrentItem( (Integer) view.getTag());
            }
        });
        return textView;
    }
    private void drawLine(Context context,int checkedPosition){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        int padding = G.dp2px(context,-underLineHeight);
        layoutParams.setMargins(padding,0,0,0);
        for (int i= 0 ;i<Tabcount;i++ ){
            View view =  getViewLine(context);
            if (checkedPosition==i){
                view.setBackgroundColor(selectUnderLinecolor);
            }else {
                view.setBackgroundColor(defalutUnderLinecolor);
            }
            linearLayout.addView(view);
        }
        this.addView(linearLayout);
    }
    private View getViewLine(Context context){
         View view = new View(context);
         LinearLayout.LayoutParams layoutParams = new LayoutParams(0, G.dp2px(context,underLineHeight));
         layoutParams.weight = 1;
         view.setLayoutParams(layoutParams);
         return  view;
    }

    /**
     * 适配器
     */
    private class  FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
