package net.hongzhang.school.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import net.hongzhang.baselibrary.widget.wheel.DateTimePickerUtils;
import net.hongzhang.baselibrary.widget.wheel.DateWheelAdapter;
import net.hongzhang.baselibrary.widget.wheel.NumericWheelView;
import net.hongzhang.baselibrary.widget.wheel.OnWheelChangedListener;
import net.hongzhang.baselibrary.widget.wheel.WheelView;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.LeaveAskActivity;

import java.util.Calendar;


/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---时间选择对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class CustomDateTimeDialog extends Dialog implements View.OnClickListener {
    private LeaveAskActivity context;
    /**
     * 年
     */

    private NumericWheelView wv_year;
    /**
     * 月
     */
    private NumericWheelView wv_month;
    /**
     * 日
     */
    private NumericWheelView wv_day;
    /**
     * 时
     */
    private NumericWheelView wv_hour;
    /**
     * 分
     */
    private NumericWheelView wv_minute;
    /**
     * 年适配器
     */
    private DateWheelAdapter yearAdapter;
    /**
     * 月适配器
     */
    private DateWheelAdapter monthAdapter;
    /**
     * 日适配器
     */
    private DateWheelAdapter dayAdapter;
    /**
     * 时适配器
     */
    private DateWheelAdapter hourAdapter;
    /**
     * 分适配器
     */
    private DateWheelAdapter minuteAdapter;
    /**
     * 日历
     */
    private Calendar calendar;
    /**
     * 确定按钮
     */
    private Button b_confirm;
    private Button b_cancle;
    /**
     * wheelView字体大小
     */
    private int textSize;
    /**
     * 开始还是结束标记
     */
    private int flag;
    public CustomDateTimeDialog(LeaveAskActivity context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public CustomDateTimeDialog(LeaveAskActivity context, int theme,int flag) {
        super(context, theme);
        this.context = context;
        this.flag = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker, null);
        setContentView(view);
        initview(view);

        textSize = (int) b_confirm.getTextSize();
        // 年
        int year = calendar.get(Calendar.YEAR);
        yearAdapter = new DateWheelAdapter(2008, 2020, "年");
        wv_year.setTextSize(textSize);
        wv_year.setAdapter(yearAdapter);
        wv_year.setCurrentValue(year, false);
        // 月
        int month = calendar.get(Calendar.MONTH) + 1;
        monthAdapter = new DateWheelAdapter(1, 12, "月");
        wv_month.setTextSize(textSize);
        wv_month.setAdapter(monthAdapter);
        wv_month.setCurrentValue(month, false);
        // 日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateWheelAdapter(1, 31, "日");
        wv_day.setTextSize(textSize);
        wv_day.setAdapter(dayAdapter);
        wv_day.setCurrentValue(day, false);
        // 时
        int hour = calendar.get(Calendar.HOUR);
        hourAdapter = new DateWheelAdapter(0, 23, "时");
        wv_hour.setTextSize(textSize);
        wv_hour.setAdapter(hourAdapter);
        wv_hour.setCurrentValue(hour, false);
        // 分
        int minute = calendar.get(Calendar.MINUTE);
        minuteAdapter = new DateWheelAdapter(0, 59, "分");
        wv_minute.setTextSize(textSize);
        wv_minute.setAdapter(minuteAdapter);
        wv_minute.setCurrentValue(minute, false);

        wv_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (monthAdapter.getValue() == 2) {
                    int dayOfMonth = (newValue % 4 == 0) ? 29 : 28;
                    setDayAdapter(dayOfMonth);
                }
            }
        });

        wv_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                int dayOfMonth = DateTimePickerUtils.getDaysInMonth(newValue - 1, yearAdapter.getValue());
                setDayAdapter(dayOfMonth);
            }
        });
    }

    private void initview(View view) {
        wv_year = (NumericWheelView) view.findViewById(R.id.wv_year);
        wv_month = (NumericWheelView) view.findViewById(R.id.wv_month);
        wv_day = (NumericWheelView) view.findViewById(R.id.wv_day);
        wv_hour = (NumericWheelView) view.findViewById(R.id.wv_hour);
        wv_minute = (NumericWheelView) view.findViewById(R.id.wv_minute);
        b_confirm = (Button) view.findViewById(R.id.b_confirm);
        b_cancle = (Button) view.findViewById(R.id.b_cancel);
       b_confirm.setOnClickListener(this);
        b_cancle.setOnClickListener(this);
    }

    /**
     * 根据条件设置天适配器
     *
     * @param dayOfMonth
     */
    private void setDayAdapter(int dayOfMonth) {
        dayAdapter = new DateWheelAdapter(1, dayOfMonth, "日");
        if (wv_day.getCurrentValue() > dayOfMonth) {
            //当前数值大于天数
            wv_day.setCurrentValue(1);
        }
        wv_day.setAdapter(dayAdapter);
    }

    private void setCalendar() {
        int year = yearAdapter.getValue();
        int month = monthAdapter.getValue();
        int day = dayAdapter.getValue();
        int hour = hourAdapter.getValue();
        int minute = minuteAdapter.getValue();
        calendar.set(year, month - 1, day, hour, minute);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
        setCurrentValues();
    }

    /**
     * 设置WheelView的Value
     */
    private void setCurrentValues() {
        wv_year.setCurrentValue(calendar.get(Calendar.YEAR), false);
        wv_month.setCurrentValue(calendar.get(Calendar.MONTH) + 1, false);
        wv_day.setCurrentValue(calendar.get(Calendar.DAY_OF_MONTH), false);
        wv_hour.setCurrentValue(calendar.get(Calendar.HOUR_OF_DAY), false);
        wv_minute.setCurrentValue(calendar.get(Calendar.MINUTE), false);
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.b_confirm) {
            setCalendar();
            long millis = calendar.getTimeInMillis();
            context.setDateTextView(millis,flag);
            dismiss();
        } else if (viewid == R.id.b_cancel) {
            cancel();
        }
    }
}
