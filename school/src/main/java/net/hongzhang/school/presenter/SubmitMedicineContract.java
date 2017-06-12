package net.hongzhang.school.presenter;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;

import net.hongzhang.baselibrary.mode.MapVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/6
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitMedicineContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setSickenType(List<MapVo> sickenType);

        void setDrigType(List<MapVo> drigType);

        void setMedicineText(String text,String sick_type);

        void setSickText(String text,String medicine);
    }

    public interface Presenter {
        OptionsPickerView optionsPickerView(Context context, String content, OptionsPickerView.OnOptionsSelectListener listener, List<MapVo> mapList);

        void submitMedicine(String tsId, String sickenType, String drigType, List<String> medicineTime, String medicineNumber, String remark, String medicineDay);

        void getSickenType();

        void getDrigType();

        void addSickenType(String tsId, String sickenName);

        void addDrigType(String tsId, String sickenName);

        void setMedicineText(String text);

        void setSickText(String text);
    }
}
