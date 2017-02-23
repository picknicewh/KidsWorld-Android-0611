package net.hongzhang.discovery.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.ViewPagerAdapter;

import java.util.List;


/**
 * Created by wanghua on 2016/12/22.
 */
public class BannerView extends FrameLayout implements OnPageChangeListener {
    /**
     * 跳转前的位置
     */
    private int previsousPosition;
    /**
     * banner页面
     */
    private ViewPager viewPager;
    /**
     * banner图片列表
     */
    private List<ImageView> viewList;
    /**
     * 圆点布局
     */
    private LinearLayout ll_dot_group;

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 5000);
        }
    };

    public void addViewPager(Context context) {
        int height = G.dp2px(context, 180);
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(lps);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        int item = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % viewList.size());
        viewPager.setCurrentItem(item);
        viewPager.addOnPageChangeListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 3000);
        this.addView(viewPager);
        addDotsLayout(context);
    }

    private void addDotsLayout(Context context) {
        ll_dot_group = new LinearLayout(context);
        ll_dot_group.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lps = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int padding = G.dp2px(context, 10);
        lps.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        lps.setMargins(padding, padding, padding, padding);
        ll_dot_group.setLayoutParams(lps);
        for (int i = 0; i < viewList.size(); i++) {
            ImageView ivDos = getDos(context, i);
            ll_dot_group.addView(ivDos);
        }
        ll_dot_group.getChildAt(previsousPosition).setEnabled(true);
        this.addView(ll_dot_group);
    }

    private ImageView getDos(Context context, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.selector_dot);
        int size = G.dp2px(context, 10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        if (position != 0) {
            layoutParams.leftMargin = size;
        }
        imageView.setLayoutParams(layoutParams);
        imageView.setEnabled(false);
        return imageView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int newPosition = position % viewList.size();
        ll_dot_group.getChildAt(newPosition).setEnabled(true);
        ll_dot_group.getChildAt(previsousPosition).setEnabled(false);
        previsousPosition = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setViewList(List<ImageView> viewList) {
        this.viewList = viewList;
    }
}
