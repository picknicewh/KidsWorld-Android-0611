package net.hongzhang.school.presenter;

import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/4
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishActiveTContract {
    public interface View {

        void setActivityType(Map<String, String> param);

        void setDimensionality(Map<String, String> param);

        void showLoadingDialog();

        void stopLoadingDialog();
    }

    public interface Presenter {
        void publishActive(String tsId, String title, String content, String type, String dimensionality,
                           long postTime, long deadline, int appraise, List<String> itemList);

        void getActivityType();

        void getDimensionality();
    }
}
