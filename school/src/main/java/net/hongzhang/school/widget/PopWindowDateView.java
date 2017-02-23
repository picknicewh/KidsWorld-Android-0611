package net.hongzhang.school.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

/**
 * 作者： Administrator
 * 时间： 2016/9/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PopWindowDateView extends PopupWindow {
    private Activity context;
    /**
     * 内容
     */
    private View conentView;
    /**
     * 日历view
     */
    private DateView dv_pop;
    public PopWindowDateView(Activity context){
        this.context = context;
    }
    private void initview(){
        conentView = context.getLayoutInflater().inflate(R.layout.pop_dateview,null);
        dv_pop = (DateView) conentView.findViewById(R.id.dv_pop);
    }
    public void init() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        //设置SignPopupWindow弹出窗体的高
        int height =  context.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        this.setHeight(height- G.dp2px(context,70));
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
