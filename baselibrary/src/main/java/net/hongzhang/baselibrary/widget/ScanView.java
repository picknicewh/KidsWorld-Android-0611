package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import net.hongzhang.baselibrary.R;

/**
 * 作者： wh
 * 时间： 2016/11/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ScanView  extends View{
    /**
     * 蒙层的颜色
     */
     public  static final int MASK_COLOR= 0x80000000;
    /**
     * 每一个扫描的时长
     */
    public  static final int SCANNER_TIME=2000;
    /**
     * 扫描线的
     */
     private Drawable scanDrawable;
    /**
     * 照相图片
     */
    private Drawable frameDrawable;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 照相区域
     */
    private Rect frame;
    /**
     *扫描线的高度
     */
    private int scannerHeight=0;
    /**
     * 起始时间
     */
    private long startTime=-1;
    public ScanView(Context context) {
        super(context);
        init();
    }
    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        frame = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        frameDrawable = getResources().getDrawable(R.mipmap.ic_scan_recenge);
        scanDrawable = getResources().getDrawable(R.mipmap.ic_scan_line) ;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int margin = (int) (0.6*width);
        frame.left  = width/2-margin/2;
        frame.right = width/2+margin/2;
        frame.top =  height/2-margin/2;
        frame.bottom = height/2+margin/2;
        frameDrawable.setBounds(frame.left-10,frame.top-10,frame.right+10,frame.bottom+10);
        //把扫描线等比例放在图形区域中
        scannerHeight = scanDrawable.getIntrinsicHeight()*frame.width()/scanDrawable.getIntrinsicWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制蒙层,以中间的照相区域分开分别为上下左右四个矩形的区域
        mPaint.setColor(MASK_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, mPaint);
        canvas.drawRect(frame.right, frame.top,width,frame.bottom, mPaint);
        canvas.drawRect(0, frame.bottom, width, height, mPaint);


        //绘制扫描线，扫描线是来回扫描的，设置每个2秒重新来回扫描
        //扫描线的的宽度x轴方向始终固定，y轴一直改变从0-frame.height
         long now = System.currentTimeMillis();
         if (startTime<0){
            startTime = now;
         }
         int timePass = (int) ((now-startTime)%SCANNER_TIME);//控制在0-2000
         if (timePass>0 && timePass<=SCANNER_TIME/2){
             int scannerShift = frame.height()* 2*timePass / SCANNER_TIME;
             canvas.save();
             //设置显示区域在frame这个矩形中
             canvas.clipRect(frame);
             scanDrawable.setBounds(frame.left,frame.top+scannerShift,frame.right,frame.top+scannerShift+scannerHeight);
             scanDrawable.draw(canvas);
             canvas.restore();
         }
        //绘制照相区域图片
        frameDrawable.draw(canvas);
        //绘制矩形的区域
        //由于扫描线一直在不挺的改变，所有要不停的刷新
        invalidate();
    }
    public Rect getFrameRect() {
        return frame;
    }

}
