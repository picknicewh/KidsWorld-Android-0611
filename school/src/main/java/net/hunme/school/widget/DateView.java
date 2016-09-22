package net.hunme.school.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.school.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者： wh
 * 时间： 2016/9/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DateView extends LinearLayout implements View.OnClickListener{
    private String  weeks[] = new String[]{"日","一","二","三","四","五","六"};
    /**
     * 左
     */
    public static final int TAG_LEFT = -1;
    /**
     * 中
     */
    public static final int TAG_MIDDLE = 0;
    /**
     * 右
     */
    public static final int TAG_RIGHT = 1;
    /**
     * 年
     */
    private int my_year;

    /**
     * 月
     */
    private int my_mouth;
    /**
     * 日
     */
    private int my_day;

    private Context context;
    /**
     * 点击前记录的日
     */
    private int my_date;
    /**
     * 点击前记录的年
     */
    private int my_date_year;
    /**
     * 点击前记录的月
     */
    private int  my_date_mouth;
    /**
     * 格式日期
     */
    private String date;
    /**
     * 格式日期
     */
    private String formatDate;
    private Calendar my_calendar;
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context,attrs, 0);
    }
    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs, defStyleAttr);
        AddView(my_year,my_mouth,my_date);
    }
    /**
     * 设置整体布局，和初始化数据
     *  @param context
     *  @param  attrs
     *  @param  defStyleAttr
     */
    private void initView(Context context,AttributeSet attrs, int defStyleAttr){
        this.context = context;
        this.setBackgroundColor(Color.WHITE);
        Calendar calendar =  Calendar.getInstance();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DateView, defStyleAttr, 0);
        my_year = a.getInteger(R.styleable.DateView_year, calendar.get(Calendar.YEAR));
        my_mouth = a.getInteger(R.styleable.DateView_mouth, calendar.get(Calendar.MONTH))+1;
        my_day = a.getInteger(R.styleable.DateView_day, calendar.get(Calendar.DAY_OF_MONTH));
        my_date = my_day;
        my_date_mouth = my_mouth;
        my_date_year = my_year;
        date = format1.format(new Date(calendar.getTimeInMillis()));
        formatDate = format2.format(new Date(calendar.getTimeInMillis()));
    }
    /**
     * 添加view
     *  @param year 年
     *  @param  mouth 月
     *  @param  day 日
     */
    private void AddView(int year,int mouth,int day){
        setDateTextView(year,mouth);
        setWeekView();
        setLine();
        setDateView(year,mouth,day);
    }
    /**
     * 设置头部星期文字
     */
    private void  setWeekView(){
        LinearLayout linearLayout  = new LinearLayout(context);
        setLinearlayoutParam(linearLayout);
        for (int i = 0 ; i <7;i++){
            TextView textView = new TextView(context);
            textView.setText(weeks[i]);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setGravity(Gravity.CENTER);
            setweekParam(textView);
            linearLayout.addView(textView);
        }
        this.addView(linearLayout);
    }
    /**
     * 设置头部星期的参数
     * @param  view 控件
     */
    private void setweekParam(TextView view){
        if (view!=null){
            this.removeView(view);
        }
        int width =(G.size.W)/7;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);
    }

    /**
     * 设置下划线
     */
    private void setLine(){
        View line = new View(context);
        line.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, G.dp2px(context,1));
        lp.setMargins(G.dp2px(context,3),0,G.dp2px(context,3),0);
        line.setLayoutParams(lp);
        this.addView(line);
    }
    /**
     * 设置每月的天数，对应的日期
     * @param year 年
     *  @param  mouth 月
     *  @param  day 日
     */
   private void  setDateView(int year,int mouth,int day){
       int rows = DateUtil.getRows(year,mouth);
       int week  = DateUtil.getWeekofday(year,mouth);
       int days =DateUtil.getMouthDays(year,mouth);
       int lastMouthday = DateUtil.getMouthDays(year,mouth-1)-week+1;
       int date =1;
       int countdate;
       for (int row=1;row<=rows;row++) {
           if (row == 1) {
               LinearLayout linearLayout = new LinearLayout(context);
               for (int i = 0;i<7;i++){
                   if (i < week) {
                       setDate(lastMouthday,linearLayout,1);
                       lastMouthday++;
                   } else {
                       setDate( date,linearLayout,2);
                       date++;
                   }
               }
               this.addView(linearLayout);
           } else if (row == rows) {
               LinearLayout linearLayout = new LinearLayout(context);
               countdate =date;
               int j = 1;
               for (int i = countdate ; i<countdate+7; i++){
                   if (i>days){
                       setDate(j++,linearLayout,0);
                   }else {
                       setDate(date,linearLayout,2);
                       date++;
                   }
               }
               this.addView(linearLayout);
           } else {
               LinearLayout linearLayout = new LinearLayout(context);
               for (int i = 0 ; i<7;i++){
                   setDate( date,linearLayout,2);
                   date++;
               }
               this.addView(linearLayout);
           }
       }
   }
    /**
     * 设置每个日期的数据和显示设置
     * @param  date 日期
     * @param  linearLayout
     * @param flag
     */
    private void setDate(final int date, LinearLayout linearLayout, final int flag){
        setLinearlayoutParam(linearLayout);
        final TextView textView = new TextView(context);
        textView.setText(String.valueOf(date));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        Calendar calendar =  Calendar.getInstance();
        int sign=0;
        if (date==my_date && date==my_day && my_year== calendar.get(Calendar.YEAR)&& my_mouth==calendar.get(Calendar.MONTH)+1){
            textView.setBackgroundColor(context.getResources().getColor(R.color.main_green));
            textView.setTextColor(context.getResources().getColor(R.color.black));
            sign= 1;
        }else if (date==my_date && date!=my_day && my_date_year==my_year&& my_date_mouth==my_mouth){
            textView.setBackgroundColor(context.getResources().getColor(R.color.main_green));
            textView.setTextColor(context.getResources().getColor(R.color.black));
            sign=0;
        }
        if (sign==0 && date==my_day && my_year== calendar.get(Calendar.YEAR)&& my_mouth==calendar.get(Calendar.MONTH)+1){
            textView.setBackgroundColor(context.getResources().getColor(R.color.red));
            textView.setTextColor(context.getResources().getColor(R.color.black));
        }
        setweekParam(textView);
        linearLayout.addView(textView);
        if (flag==1||flag==0){
            textView.setTextColor(context.getResources().getColor(R.color.line_gray));
            textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag==1){
                        upDate();
                    }else {
                        downDate();
                    }
                    my_date = Integer.valueOf(textView.getText().toString());
                    itemClick(1);
                }
            });
        }else if (flag==2){
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    my_date = date;
                    itemClick(1);
                }
            });
        }
    }
    /**
     * 设置每行linearLayout 的参数
     * @param  linearLayout
     */
   private void setLinearlayoutParam( LinearLayout linearLayout){
       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
       int padding= G.dp2px(context,10);
       lp.setMargins(padding*2,padding,padding*2,padding);
       linearLayout.setLayoutParams(lp);
       linearLayout.setOrientation(HORIZONTAL);
   }
    /**
     * 设置顶部日期view的布局
     * @param  year 年
     * @param  mouth 月
     */
   private void setDateTextView(int year,int mouth){
       RelativeLayout relativeLayout = new RelativeLayout(context);
    /*   TextView tv_center = new TextView(context);
       tv_center.setTag(TAG_MIDDLE);
       tv_center.setText(year+"年"+mouth+"月"+day+"日");
       tv_center.setTextColor(Color.BLACK);
       tv_center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18)
       setDateTextViewparam(tv_center,2,relativeLayout);;*/
       setDateCenterView(year,mouth,relativeLayout);
       ImageView iv_left = new ImageView(context);
       iv_left.setTag(TAG_LEFT);
       iv_left.setImageResource(R.mipmap.ic_green_left);
       setDateTextViewparam(iv_left,0,relativeLayout);
       ImageView iv_right = new ImageView(context);
       iv_right.setTag(TAG_RIGHT);
       iv_right.setImageResource(R.mipmap.ic_green_right);
       setDateTextViewparam(iv_right,1,relativeLayout);
       this.addView(relativeLayout);
   }
    public String getDate(){
        return this.date;
    }
    public  String getFormatDate(){
        return this.formatDate;
    }
    public void setFormatDate(String formatDate){
        this.formatDate = formatDate;
    }
    public void setDate(String date){
        this.date = date;
    }
    /**
     * 日期点击事件
     * @param flag 0左右按钮 日期数字点击
     */
    private void itemClick(int flag){
        my_date_mouth =my_mouth;
        my_date_year = my_year;
        DateView.this.removeAllViews();
        AddView(my_year,my_mouth,my_date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(my_year,my_mouth-1,my_date);
        date = format1.format(new Date(calendar.getTimeInMillis()));
        formatDate = format2.format(new Date(calendar.getTimeInMillis()));
        setDate(date);
        if (flag==1){
            DateView.this.setVisibility(GONE);
        }else {
            DateView.this.setVisibility(VISIBLE);
        }
    }
    /**
     * 设置顶部日期view的布局参数
     * @param view
     * @param  position 位置 左中右
     * @param  relativeLayout 布局
     */
    private void setDateTextViewparam(View view,int position,RelativeLayout relativeLayout){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int padding= G.dp2px(context,10);
        lp.setMargins(padding*2,padding,padding,padding);
        switch (position){
            case 0:
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT ,RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.CENTER_VERTICAL ,RelativeLayout.TRUE);
                break;
            case 1:
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.CENTER_VERTICAL ,RelativeLayout.TRUE);
                break;
            case 2:
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
        }
        view.setOnClickListener(this);
        view.setLayoutParams(lp);
        relativeLayout.addView(view);
    }
    /**
     * 设置顶部日期中间的布局
     * @param  year 年
     * @param  mouth 月
     */
    private void setDateCenterView(int year,int mouth,RelativeLayout rl_miantop){
        LinearLayout linearLayout = new LinearLayout(context);
        setDateTextViewparam(linearLayout,2,rl_miantop);
        linearLayout.setTag(TAG_MIDDLE);
        linearLayout.setOrientation(HORIZONTAL);
        TextView tv_mouth = new TextView(context);
        tv_mouth.setText(DateUtil.getMouthByChinese(mouth));
        tv_mouth.setTextColor(Color.BLACK);
        tv_mouth.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv_mouth.setGravity(Gravity.CENTER);
        TextView tv_year = new TextView(context);
        tv_year.setText(String.valueOf(year));
        tv_year.setTextColor(Color.BLACK);
        tv_year.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv_year.setGravity(Gravity.CENTER);
        linearLayout.addView(tv_mouth);
        linearLayout.addView(tv_year);
    }
    /**
     * 向上翻日历
     */
    private void upDate(){
        my_mouth = my_mouth-1;
        if (my_mouth<1){
            my_year--;
            my_mouth=12;
        }
    }
    /**
     * 下上翻日历
     */
    private void downDate(){
        my_mouth = my_mouth+1;
        if (my_mouth>12){
            my_year++;
            my_mouth=1;
        }
    }
    @Override
    public void onClick(View view) {
      int  tag = (int) view.getTag();
      switch (tag){
          case TAG_LEFT:
              upDate();
              itemClick(0);
              break;
          case TAG_MIDDLE:
              break;
          case TAG_RIGHT:
              downDate();
              itemClick(0);
              break;
      }
    }

}
