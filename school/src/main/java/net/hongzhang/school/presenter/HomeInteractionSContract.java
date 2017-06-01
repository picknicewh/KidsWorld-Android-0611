package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.ActivityInfoVos;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HomeInteractionSContract {
    public interface View {
        void showLoadingDialog();
        void stopLoadingDialog();
        void setPublishActivityList(List<ActivityInfoVos> activityVosList);
        void setEndActivityList(List<ActivityInfoVos> activityVosList);
    }

    public interface Presenter {
        void getPublishActivityList(String tsId);
        void getEndActivityList(String tsId);
    }
}
