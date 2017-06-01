package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.TaskDetailInfoVo;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitActiveDetailContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();
        void setActiveDetailInfo(TaskDetailInfoVo taskDetailInfoVo);
    }

    public interface Presenter {
      void getActiveDetail(String activityWorksId);
    }
}
