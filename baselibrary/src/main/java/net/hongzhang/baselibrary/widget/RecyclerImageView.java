package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable=getDrawable();
                if(drawable!=null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp=getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }
}
