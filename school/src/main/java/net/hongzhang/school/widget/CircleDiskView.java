package net.hongzhang.school.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.hongzhang.school.R;

/**
 * Created by wanghua on 2017/2/21.
 */
public class CircleDiskView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * 大圆盘的颜色
     */
    private int DEFAULTCIRCLECOLOR = Color.parseColor("#FF0000");
    /**
     * 分成几个模块
     */
    private int part;
    /**
     * 大圆盘的颜色
     */
    private int circle_color = Color.parseColor("#FF0000");
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 锁定的画布
     */
    private Canvas canvas;
    /**
     * 画板
     */
    private SurfaceHolder holder;
    /**
     * 用于绘制的线程
     */
    private Thread thread;
    /**
     * 线程控制开光关
     */
    private boolean isRunning;
    /**
     * x轴中心位置
     */
    private int centerX;
    /**
     * y轴中心位置
     */
    private int centerY;
    public CircleDiskView(Context context) {
        this(context, null);
    }

    public CircleDiskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CircleDiskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleDiskView);
        part = array.getInteger(R.styleable.CircleDiskView_cdv_part, 6);
        circle_color = array.getInteger(R.styleable.CircleDiskView_cdv_circle_color,DEFAULTCIRCLECOLOR);
        init();
        array.recycle();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        //设置画布透明
        // setZOrderOnTop(true);
        //holder.setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.getKeepScreenOn();
        mPaint = new Paint();
        mPaint.setColor(circle_color);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerX = widthMeasureSpec/2;
        centerY = heightMeasureSpec/2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
               canvas.drawCircle(centerX,centerY,centerX/2,mPaint);
            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }

    }
}
