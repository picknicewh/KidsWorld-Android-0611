package net.hongzhang.school.widget;

import android.content.Context;
import android.content.Intent;
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

import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.BaseFoodActivity;
import net.hongzhang.school.activity.FoodListActivity;

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
     * 当前的年
     */
    private int n_year;

    /**
     * 当前的月
     */
    private int n_mouth;
    /**
     * 当前的日
     */
    private int n_day;

    private Context context;
    /**
     * 点击前记录的日
     */
    private int b_day;
    /**
     * 点击前记录的年
     */
    private int b_year;
    /**
     * 点击前记录的月
     */
    private int  b_mouth;
    /**
     * 格式日期
     */
    private String date;
    /**
     * 格式日期
     */
    private String formatDate;
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context,attrs, 0);
    }
    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs, defStyleAttr);
        AddView(n_year,n_mouth,n_day);
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
        n_year = a.getInteger(R.styleable.DateView_year, calendar.get(Calendar.YEAR));
        n_mouth = a.getInteger(R.styleable.DateView_mouth, calendar.get(Calendar.MONTH))+1;
        n_day = a.getInteger(R.styleable.DateView_day, calendar.get(Calendar.DAY_OF_MONTH));
        b_day = n_day;
        b_mouth = n_mouth;
        b_year =n_year;
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
        setLine();
        setDateTextView(year,mouth);
        setWeekView();
        setLine();
        setDateView(year,mouth,day);
        setLine();
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
       int lastMouthday = DateUtil.getMouthDays(year,mouth)-week;
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
     * 设置当前选中日期的颜色状态
     * @param status 状态   1表示上月日期状态
     *                     2当前被选中日期状态
     *                     3有除当天以后的日子选中时，当天日期的状态，
     */
    private void  setCurrentSeletColor(int status,TextView view){
        switch (status){
            case 1:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                view.setTextColor(context.getResources().getColor(R.color.line_gray));
                break;
            case 2:
                view.setBackgroundColor(context.getResources().getColor(R.color.main_green));
                view.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 3:
                view.setBackgroundColor(context.getResources().getColor(R.color.red));
                view.setTextColor(context.getResources().getColor(R.color.black));
                break;
        }
    }
    /**
     * 设置每个日期的数据和显示设置
     * @param  day 日期
     * @param  linearLayout
     * @param flag
     */
    private void setDate(final int day, LinearLayout linearLayout, final int flag){
        setLinearlayoutParam(linearLayout);
        final TextView tv_day = new TextView(context);
        tv_day.setText(String.valueOf(day));
        tv_day.setTextSize(16);
        tv_day.setGravity(Gravity.CENTER);
        Calendar calendar =  Calendar.getInstance();
        int sign=0;
        //当前的时间
        if (day==b_day && day==n_day && n_year== calendar.get(Calendar.YEAR)&& n_mouth==calendar.get(Calendar.MONTH)+1){
          setCurrentSeletColor(2,tv_day);
            sign= 1;
        }
        else if (day==n_day&&  b_year==n_year && b_mouth==n_mouth){
            setCurrentSeletColor(2,tv_day);
            sign=0;
        }
        if (sign==0 && day==calendar.get(Calendar.DAY_OF_MONTH) && n_year== calendar.get(Calendar.YEAR)&& n_mouth==calendar.get(Calendar.MONTH)+1){
            setCurrentSeletColor(3,tv_day);
        }
        setweekParam(tv_day);
        linearLayout.addView(tv_day);
        if (flag==1||flag==0){
            setCurrentSeletColor(1,tv_day);
            tv_day.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag==1){
                        upDate();
                    }else {
                        downDate();
                    }
                    n_day = Integer.valueOf(tv_day.getText().toString());
                    itemClick(1);
                }
            });
        }else if (flag==2){
            tv_day.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    n_day = day;
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
     * @param flag 0 左右按钮 1日期数字点击
     */
    private void itemClick(int flag){
        b_mouth =n_mouth;
        b_year =n_year;
        b_day = n_day;
        DateView.this.removeAllViews();
        AddView(n_year,n_mouth,n_day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(n_year,n_mouth-1,n_day);
        date = format1.format(new Date(calendar.getTimeInMillis()));
        formatDate = format2.format(new Date(calendar.getTimeInMillis()));
        setDate(date);
        if (flag==1){
            DateView.this.setVisibility(GONE);
            BaseFoodActivity.getCalender().setText(date);
            if (BaseFoodActivity.from==BaseFoodActivity.FOODLISTPAGE){
                FoodListActivity.getListView().setVisibility(GONE);
                FoodListActivity.getNodataView().setVisibility(GONE);
            }
            Intent intent  = new Intent(BaseFoodActivity.ACTION_GEFOOD);
            context.sendBroadcast(intent);
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
        linearLayout.addView(tv_mouth);
        TextView tv_year = new TextView(context);
        tv_year.setText(String.valueOf(year));
        tv_year.setTextColor(Color.BLACK);
        tv_year.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv_year.setGravity(Gravity.CENTER);
        linearLayout.addView(tv_year);
    }
    /**
     * 向上翻日历
     */
    private void upDate(){
        n_mouth = n_mouth-1;
        if (n_mouth<1){
            n_year--;
            n_mouth=12;
        }
    }
    /**
     * 下上翻日历
     */
    private void downDate(){
        n_mouth = n_mouth+1;
        if (n_mouth>12){
            n_year++;
            n_mouth=1;
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
