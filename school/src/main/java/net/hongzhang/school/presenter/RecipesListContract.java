package net.hongzhang.school.presenter;

import net.hongzhang.baselibrary.mode.ClassVo;
import net.hongzhang.school.bean.RecipesVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RecipesListContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();
        void setClassList(List<ClassVo> classVos);
        void setRecipesVoList(List<RecipesVo> RecipesVoList);
    }

    public interface Presenter {
        void getClassList(String tsId);
       void  getRecipesList(String classId);
    }
}
