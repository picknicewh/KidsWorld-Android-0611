package net.hongzhang.school.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/26
 * 描    述：
 * 版    本：
 * 修订历史：
 * ================================================
 */
public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
