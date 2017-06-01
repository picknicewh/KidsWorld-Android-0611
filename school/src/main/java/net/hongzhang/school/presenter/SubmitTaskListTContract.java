package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.TaskInfoVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：教师获取家长上传列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitTaskListTContract {
    public interface View {
        void showLoadingDialog();
        void stopLoadingDialog();
         void setTaskList(List<TaskInfoVo> taskListInfoVos);
    }

    public interface Presenter {
      void getTaskList(String activityId,String tsId);
        boolean isAllAppraise(List<TaskInfoVo> taskListInfoVos);
    }
}
