package net.hongzhang.school.presenter;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：学生提交活动作业
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitTaskSContract {
    public interface View {
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter {
        /**
         file	MultipartFile	视频文件
         */
       void submitTask(String activityId, String tsId, String content,  int publicity, List<String> imageList);

        List<String> getImageList(List<String> itemList,String  videoPath);
    }
}
