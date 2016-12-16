package net.hunme.school.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.PopupWindow;

import net.hunme.baselibrary.util.DateUtil;
import net.hunme.baselibrary.util.G;
import net.hunme.school.R;

import java.util.Calendar;

/**
 * 作者： wh
 * 时间： 2016/11/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MulSelectDateView extends PopupWindow {
    private  static  final String  weeks[] = new String[]{"日","一","二","三","四","五","六"};
    /**
     * 当月日期的颜色
     */
    private final static int CURRENT_MOUTH_COLOR =Color.parseColor("#646464");
    /**
     * 其他月的颜色
     */
    private final static int OTHER_MOUTH_COLOR = Color.parseColor("#e1e1e1");
    /**
     * 选中背景的颜色
     */
    private final  static int SELECT_BG_COLOR = Color.parseColor("#8dc700");

    /**
     * 选中的颜色
     */
    private final  static int BG_COLOR= 0xffffff;
    /**
     * 蒙层的颜色
     */
    public  static final int MASK_COLOR= 0x80000000;
    /**
     * 当前年
     */
    private int currentYear;
    /**
     * 当前月
     */
    private int currentMouth;
    /**
     * 当前日
     */
    private int currentDay;
    /**
     * 日历
     */
    private   Calendar calendar;
    /**
     * 画笔
     */
    private Paint mPanit;
    /**
     * 选择年份的左箭头
     */
     private Drawable year_arraw_left;
    /**
     * 选择月份的左箭头
     */
    private Drawable mouth_arraw_left;
    /**
     * 选择年份的右箭头
     */
    private Drawable year_arraw_right;
    /**
     * 选择月份的右箭头
     */
    private Drawable mouth_arraw_right;
    /**
     * 日历的矩形绘画范围
     */
    private Rect frameRect;
    /**
     * 箭头的宽度
     */
    private int arrow_width ;
    /**
     *日历里面的间距
     */
     private int padding ;
     public MulSelectDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs,0);
    }
    public MulSelectDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }
    private void initView(Context context,AttributeSet attrs,int defStyleAttr){
        calendar =  Calendar.getInstance();
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MulSelectDateView,defStyleAttr,0);
        currentYear   = array.getInteger(R.styleable.DateView_year, calendar.get(Calendar.YEAR));
        currentMouth= array.getInteger(R.styleable.DateView_mouth, calendar.get(Calendar.MONTH))+1;
        currentDay = array.getInteger(R.styleable.DateView_day, calendar.get(Calendar.DAY_OF_MONTH));
        mPanit = new Paint();
        mPanit.setStyle(Paint.Style.FILL);
        mPanit.setTextSize(24);
        mPanit.setStrokeWidth(2);
        mPanit.setAntiAlias(true);
        frameRect  =new Rect();
        year_arraw_left  = context.getResources().getDrawable(R.mipmap.ic_green_left);
        year_arraw_right  = context.getResources().getDrawable(R.mipmap.ic_green_right);
        mouth_arraw_left  = context.getResources().getDrawable(R.mipmap.ic_green_left);
        mouth_arraw_right  = context.getResources().getDrawable(R.mipmap.ic_green_right);
        arrow_width = G.dp2px(context,20);
        padding = G.dp2px(context,10);
    }


    protected void onDraw(Canvas canvas) {
        drawMainBg(canvas);
        canvas.save();
        drawTop(canvas);
        drawCalender(canvas);
        canvas.restore();
    }
    /**
     * 绘制整体的背景，加蒙层
     * @param canvas 画布
     */
    private void drawMainBg(Canvas canvas){
        //绘制蒙层
        mPanit.setColor(MASK_COLOR);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, frameRect.top, mPanit);
        canvas.drawRect(0, frameRect.top, frameRect.left, frameRect.bottom, mPanit);
        canvas.drawRect(frameRect.right, frameRect.top,width,frameRect.bottom, mPanit);
        canvas.drawRect(0, frameRect.bottom, width, height, mPanit);
        //绘制中间
        mPanit.setColor(Color.WHITE);
        canvas.drawRect(frameRect.left-padding,frameRect.top-padding,frameRect.right+padding,frameRect.bottom+padding,mPanit);

    }
    /**
     *绘制上面的文字
     * @param canvas 画布
     */
    private void drawTop(Canvas canvas) {
        year_arraw_left.draw(canvas);
        mPanit.setColor(Color.BLACK);
        String year = currentYear+"年";
        canvas.drawText(year,frameRect.left+arrow_width+padding,frameRect.top+arrow_width/2+10,mPanit);
        year_arraw_right.draw(canvas);
        mouth_arraw_left.draw(canvas);
        String mouth = currentMouth+"月";
        canvas.drawText(mouth,frameRect.left+arrow_width*6+padding+5,frameRect.top+arrow_width/2+10,mPanit);
        mouth_arraw_right.draw(canvas);
        mPanit.setColor(SELECT_BG_COLOR);
        canvas.drawText("完成",frameRect.right-arrow_width-padding,frameRect.top+arrow_width/2+10,mPanit);
        mPanit.setColor(OTHER_MOUTH_COLOR);
        canvas.drawText("取消",frameRect.right-arrow_width*2-padding-mPanit.measureText("确定"),frameRect.top+arrow_width/2+10,mPanit);
        //绘制下划线
        canvas.drawLine(frameRect.left-padding,frameRect.top+arrow_width+padding,frameRect.right+padding,frameRect.top+arrow_width+padding+2,mPanit);
    }
    /**
     * 绘制下面的星期
     * @param canvas 画布
     */
    private void drawCalender(Canvas canvas){
        int eachWidth =  (frameRect.width()+padding*2)/7;
        int eachHeight  = frameRect.top+arrow_width*2+10;

        int margin = frameRect.left+padding-5;
        //绘制星期汉字
        for (int i =0;i<weeks.length;i++){
          canvas.drawText(weeks[i],frameRect.left+padding+(eachWidth)*i,eachHeight,mPanit);
        }
       drawDateView(canvas,eachWidth,margin);
    }
    /**
     * 设置每月的天数，对应的日期
     * @param canvas 画布
     */
    private void  drawDateView(Canvas canvas,int eachWidth,int margin){
        int rows = DateUtil.getRows(currentYear,currentMouth);
        int week  = DateUtil.getWeekofday(currentYear,currentMouth);
        int days =DateUtil.getMouthDays(currentYear,currentMouth);
        int lastMouthday = DateUtil.getMouthDays(currentYear,currentMouth-1)-week+1;
        int date =1;
        int countDate;
        for (int row=1;row<=rows;row++) {
            int eachHeight  = arrow_width+frameRect.top+(arrow_width*2+padding )*row;
            if (row == 1) {
                for (int i = 0;i<7;i++){
                    if (i < week) {
                        mPanit.setColor(OTHER_MOUTH_COLOR);
                        canvas.drawText(String.valueOf(lastMouthday),margin+eachWidth*i,eachHeight,mPanit);
                        lastMouthday++;
                    } else {
                        mPanit.setColor(Color.BLACK);
                        canvas.drawText(String.valueOf(date),margin+eachWidth*i,eachHeight,mPanit);
                        date++;
                    }
                }
            } else if (row == rows) {
                countDate =date;
                int j = 1;
                for (int i = countDate;i<countDate+7; i++){
                    if (i>days){
                        mPanit.setColor(OTHER_MOUTH_COLOR);
                        canvas.drawText(String.valueOf(j),margin+eachWidth*i,eachHeight,mPanit);
                        j++;
                    }else {
                        mPanit.setColor(Color.BLACK);
                        canvas.drawText(String.valueOf(date),margin+eachWidth*i,eachHeight,mPanit);
                        date++;
                    }
                }
            } else {
                for (int i = 0 ;i<7;i++){
                    mPanit.setColor(Color.BLACK);
                    canvas.drawText(String.valueOf(date),margin+eachWidth*i,eachHeight,mPanit);
                    date++;
                }
            }
        }
    }
}
