package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/6/5
 * 名称：新版请假类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class VacationVo {
    /**
     * 	请假人名称
     */
    String ts_name	;
    /**
     * 	事由
     */
    String va_content;
    /**
     * 	请假天数 0.0（一位小数）
     */
    String day_number;
    /**
     * 	返校日期	YYYY—MM—DD HH:mm:ss
     */
    String back_school_date;

    public String getTs_name() {
        return ts_name;
    }

    public void setTs_name(String ts_name) {
        this.ts_name = ts_name;
    }

    public String getVa_content() {
        return va_content;
    }

    public void setVa_content(String va_content) {
        this.va_content = va_content;
    }

    public String getDay_number() {
        return day_number;
    }

    public void setDay_number(String day_number) {
        this.day_number = day_number;
    }

    public String getBack_school_date() {
        return back_school_date;
    }

    public void setBack_school_date(String back_school_date) {
        this.back_school_date = back_school_date;
    }
}
