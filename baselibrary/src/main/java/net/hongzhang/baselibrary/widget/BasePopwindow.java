package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 作者： wanghua
 * 时间： 2017/6/6
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BasePopwindow extends PopupWindow {
    public View contentView;
    public Context context;
    public BasePopwindow() {
        init();
    }
    public abstract void initView();

    /**
     * 初始化popwindow各项参数
     */
    public void init() {
        initView();
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
