package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 作者： Administrator
 * 时间： 2016/9/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class NoScrollGirdView extends GridView {
    public NoScrollGirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollGirdView(Context context) {
        super(context);
    }
    public NoScrollGirdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
