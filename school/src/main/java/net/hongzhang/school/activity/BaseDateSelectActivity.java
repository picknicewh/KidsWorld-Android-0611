package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.school.R;
import net.hongzhang.school.widget.CustomDateTimeDialog;

import java.util.Date;


/**
 * 作者： wanghua
 * 时间： 2017/4/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BaseDateSelectActivity extends BaseActivity implements View.OnClickListener {
    public TextView tv_start;

    public TextView tv_end;
    /**
     * 发布时间选择控件
     */
    public CustomDateTimeDialog startTimeDialog;
    /**
     * 截至时间选择控件
     */
    public CustomDateTimeDialog endTimeDialog;
    /**
     * 最后一次点击时间
     */
    public long lastClickTime;
    /**
     * 开始时间
     */
    public String startDate;
    /**
     * 结束时间
     */
    public String endDate;
    public long startTime;
    public long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimeDialog = new CustomDateTimeDialog(this, R.style.MyDialog, 1);
        endTimeDialog = new CustomDateTimeDialog(this, R.style.MyDialog, 0);

    }

    /**
     * 是否为快速双击
     */
    public boolean isFastDoubleClick() {
        long now = System.currentTimeMillis();
        long offset = now - lastClickTime;
        if (offset <= 1000) {
            return true;
        }
        lastClickTime = now;
        return false;
    }

    public void initDate() {
        Date date = new Date(System.currentTimeMillis());
        startDate = DateUtil.DATE_TIME.format(date);
        tv_start.setText(DateUtil.format_chinese.format(date));
        tv_end.setText("请选择时间");
    }

    /**
     * 记录最后点击时间
     */
    public void markLastClickTime() {
        lastClickTime = System.currentTimeMillis();
    }

    /**
     * 设置选择时间
     */
    public void setDateTextView(long millis, int flag) {
        Date date = new Date(millis);
        String dateStr = DateUtil.format_chinese.format(date);
        if (flag == 1) {
            tv_start.setText(dateStr);
            startDate = DateUtil.DATE_TIME.format(date);
            startTime = millis;
        } else {
            tv_end.setText(dateStr);
            endDate = DateUtil.DATE_TIME.format(date);
            endTime = millis;
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTv_start(TextView tv_start) {
        this.tv_start = tv_start;
    }

    public void setTv_end(TextView tv_end) {
        this.tv_end = tv_end;
    }
}


