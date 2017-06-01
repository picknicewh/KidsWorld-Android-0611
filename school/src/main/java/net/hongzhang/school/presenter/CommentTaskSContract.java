package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.TaskCommentDetailVo;

import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CommentTaskSContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setDetailInfo(TaskCommentDetailVo taskCommentDetailVo);
    }

    public interface Presenter {
         void getAppraiseDetail(String appraiseId);

        List<Map<String, Object>> getParentList(List<TaskCommentDetailVo.ParentVo> tagList);

        List<String> getChildList(List<TaskCommentDetailVo.ParentVo> tagList);
    }
}
