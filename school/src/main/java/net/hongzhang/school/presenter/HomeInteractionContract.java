package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.ActivityInfoVo;
import net.hongzhang.school.bean.ActivityInfoVos;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HomeInteractionContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setActivityList(List<ActivityInfoVos> activityVosList);
    }

    public interface Presenter {
        void getActivityList(String tsId);
        int getStatus( ActivityInfoVo activityInfoVo);
    }
}
