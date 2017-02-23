package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 * */
public class MedicineTVos {
    /**
     * 未完成喂药
     */
    private List<MedicineVo> notMedicine;
    /**
     *已完成喂药
     */
    private List<MedicineVo> finishMedicine;

    public List<MedicineVo> getNotMedicine() {
        return notMedicine;
    }

    public void setNotMedicine(List<MedicineVo> notMedicine) {
        this.notMedicine = notMedicine;
    }

    public List<MedicineVo> getFinishMedicine() {
        return finishMedicine;
    }

    public void setFinishMedicine(List<MedicineVo> finishMedicine) {
        this.finishMedicine = finishMedicine;
    }
}
