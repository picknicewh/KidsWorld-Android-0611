package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.MyMedicineVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineListContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setMedicineDate(List<String> medicineDates);
         void setMedicine(List<MyMedicineVo> medicineVos);
    }

    public interface Presenter {
        void getMedicineDate(String  tsId);
        void getMedicine(String  tsId,String date);
    }
}
