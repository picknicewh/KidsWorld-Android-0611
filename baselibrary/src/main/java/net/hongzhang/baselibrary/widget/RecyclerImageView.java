package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 作者：Restring
 * 时间：2017/3/17
 * 描述：
 * 版本：
 */

public class RecyclerImageView extends android.support.v7.widget.AppCompatImageView{

    private int index;
    public RecyclerImageView(Context context) {
        super(context);
    }

    public RecyclerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
