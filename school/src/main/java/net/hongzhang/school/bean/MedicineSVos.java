package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/1
 * 名称：家长端获取喂药列表类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineSVos {
    /**
     * 喂药列表
     */
    private List<MedicineVo> medicineList;
    /**
     *今日喂药流程反馈
     */
    private List<MedicineSchedule> medicineScheduleJson;

    public  List<MedicineSchedule>  getMedicineScheduleJson() {
        return medicineScheduleJson;
    }

    public void setMedicineScheduleJson( List<MedicineSchedule>   medicineScheduleJson) {
        this.medicineScheduleJson = medicineScheduleJson;
    }

    public List<MedicineVo> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<MedicineVo> medicineList) {
        this.medicineList = medicineList;
    }
}
