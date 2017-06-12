package net.hongzhang.school.presenter;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;

import net.hongzhang.baselibrary.mode.MapVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitRecipesContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setRecipesTime(List<MapVo> times);
    }

    public interface Presenter {
        OptionsPickerView optionsPickerView(Context context, OptionsPickerView.OnOptionsSelectListener listener);

        void getRecipesTime();

        void submitRecipes(String tsId, String classId, String title, String type,List<String> imageList);
    }
}

