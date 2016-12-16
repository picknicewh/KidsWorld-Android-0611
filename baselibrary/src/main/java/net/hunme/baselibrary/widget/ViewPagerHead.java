package net.hunme.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.R;

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
    private static  final  int  defaultcolor  = Color.parseColor("#494949");
    /**
     * 默认选择字体颜色
     */
    private static  final  int selecttextcolor = Color.parseColor("#8ec500");
    /**
     * 默认选中下划线的颜色
     */
    private static  final  int selectunderlinecolor= Color.parseColor("#8ec500");
    /**
     * 默认未选中下划线的颜色
     */
    private static  final  int defaultunderlinecolor= Color.parseColor("#efeff4");
    /**
     * 默认背景的颜色
     */
    private static  final  int backgroundcolor= Color.parseColor("#efeff4");
    /**
     * 默认背景的颜色
     */
    private static  final  int defaultunderlineHeight= 2;
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

    public ViewPagerHead(Context context) {
        super(context);
    }
    public ViewPagerHead(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public ViewPagerHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerHead);
        selectUnderLinecolor = array.getColor(R.styleable.ViewPagerHead_vphUnderlineColor,selectunderlinecolor);
        underLineHeight = (int) array.getDimension(R.styleable.ViewPagerHead_vphUnderlineHeight,defaultunderlineHeight);
        selectTextcolor = array.getColor(R.styleable.ViewPagerHead_vphSelectTextColor,selecttextcolor);
        defaultTextcolor = array.getColor(R.styleable.ViewPagerHead_vphDefaultTextColor,defaultcolor);
        backgroundColor = array.getColor(R.styleable.ViewPagerHead_vphTabBackground,backgroundcolor);
        defalutUnderLinecolor = array.getColor(R.styleable.ViewPagerHead_vphUnderlineColor,defaultunderlinecolor);
    }
    private void init(){
     this.setOrientation(LinearLayout.HORIZONTAL);
    }
    private void setHead(Context context){
        LinearLayout linearLayout = new LinearLayout(context);

        this.addView(linearLayout);
    }
    private void setTextView(Context context){
        TextView textView = new TextView(context);
    }
}
