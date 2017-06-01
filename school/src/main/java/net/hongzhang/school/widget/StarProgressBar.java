package net.hongzhang.school.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

/**
 * 作者： wanghua
 * 时间： 2017/4/26
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StarProgressBar extends View {
    /**
     * 过程的图片资源
     */
    private int progressStarRes;
    /**
     * 背景星星的图片资源
     */
    private int backgroundStarRes;
    /**
     * 背景星星的图片资源
     */
    private int halfStarRes;
    /**
     * 过程图片
     */
    private Bitmap progressBitmap;
    /**
     * 背景图片
     */
    private Bitmap backgroundBitmap;
    private Bitmap halfBitmap;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 星星的个数
     */
    private int count = 5;
    /**
     * 背景星星的绘制位置
     */
    private Rect backgroundRect;
    /**
     * 过程星星的绘制位置
     */
    private Rect porgressRect;
    /**
     * 每一个星星的宽度
     */
    private int itemWidth;
    /**
     * 每一个星星的高度
     */
    private int itemHeight;
    /**
     * 图片的实际大小位置
     */
    private Rect dstRect;
    private int padding;
    private int progress;
   // private int parentPadding;
    private OnProgressChangeListener onProgressChangeListener;

    public StarProgressBar(Context context) {
        this(context, null);
    }

    public StarProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StarProgressBar);
        backgroundStarRes = array.getResourceId(R.styleable.StarProgressBar_emptyStart, R.mipmap.ic_empty_star);
        progressStarRes = array.getResourceId(R.styleable.StarProgressBar_fullStart, R.mipmap.ic_full_star);
        halfStarRes = array.getResourceId(R.styleable.StarProgressBar_halfStart, R.mipmap.ic_half_star);
        canTouch  = array.getBoolean(R.styleable.StarProgressBar_canTouch,true);
        progress = array.getInteger(R.styleable.StarProgressBar_StarProgress,0);
        array.recycle();
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        halfBitmap = BitmapFactory.decodeResource(getResources(), halfStarRes);
        progressBitmap = BitmapFactory.decodeResource(getResources(), progressStarRes);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundStarRes);
        backgroundRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        porgressRect = new Rect(0, 0, progressBitmap.getWidth(), progressBitmap.getHeight());
        dstRect = new Rect();
        itemHeight = backgroundBitmap.getHeight();
        itemWidth = backgroundBitmap.getWidth();
        endX=progress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, itemHeight);//设置view的大小
        padding = (width - (paddingLeft + paddingRight + itemWidth * 5)) / 4;
        setProgress(progress);
        G.log("---------------------  ..c " + padding);
    }

    /**
     * Bitmap bitmap：要绘制的位图对象
     * Rect src： 是对图片进行裁截，若是空null则显示整个图片
     * RectF dst：是图片在Canvas画布中显示的区域
     * Paint paint：画笔，这个不用多说
     * 方法：canvas.drawBitmap(bitmap, matrix, paint);
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < count; i++) {
            int left = i * (itemWidth + padding);
            int right = itemWidth * (i + 1) + padding * i;
            dstRect.set(left, 0, right, itemHeight);
            canvas.drawBitmap(backgroundBitmap, backgroundRect, dstRect, mPaint);
        }

        if (endX > 0) {
            for (int i = 0; i < count; i++) {
                int left = i * (itemWidth + padding);
                int right = itemWidth * (i + 1) + padding * i;
                int x1 = left - padding / 2;
                int x2 = left + itemWidth / 2;
                int x3 = right + padding / 2;
                float count = i;
                dstRect.set(left, 0, right, itemHeight);
                if (x1 < endX && x2 > endX) {
                    canvas.drawBitmap(halfBitmap, porgressRect, dstRect, mPaint);
                    progress = (int) (((float) (count + 0.5)) * 20);
                    break;
                } else if (x2 < endX && x3 > endX) {
                    canvas.drawBitmap(progressBitmap, porgressRect, dstRect, mPaint);
                    progress = (int) ((count + 1) * 20);
                    break;
                } else {
                    canvas.drawBitmap(progressBitmap, porgressRect, dstRect, mPaint);
                }
            }
            G.log("---------------------  progress " + endX);
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onProgress(progress);

            }
        }

    }

    private float startX;
    private float endX;
    private boolean canTouch;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                endX = startX;
            //    invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                //最后一次起来位置
                endX = event.getX();
                break;

        }
        if (canTouch) {
            invalidate();
        }

        return true;
    }
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        float i = ((float) progress / (float) 20);
        endX = itemWidth * i + padding * (i - 1);
        invalidate();
    }

    public interface OnProgressChangeListener {
        void onProgress(int progress);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
