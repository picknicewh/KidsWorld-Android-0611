package net.hunme.school.bean;

import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/11/1
 * 名称：今日喂药流程反馈
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineSchedule {
    /**
     * 喂药详情
     */
     private MedicineVo medicine;
    /**
     *今日喂药流程
     */
    private Map<String,Object> schedule	;

    public MedicineVo getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineVo medicine) {
        this.medicine = medicine;
    }

    public Map<String, Object> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, Object> schedule) {
        this.schedule = schedule;
    }
}
