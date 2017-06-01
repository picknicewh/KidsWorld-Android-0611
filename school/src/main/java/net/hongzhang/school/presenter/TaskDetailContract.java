package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.ActivityDetailVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：活动详情
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskDetailContract {
    public interface View {
        void showLoadingDialog();
        void stopLoadingDialog();
        void setDetailInfo(ActivityDetailVo activityDetailVo);
    }

    public interface Presenter {
        void getTaskDetail(String activityId);
        String getChecks(List<String> values);
    }
}
