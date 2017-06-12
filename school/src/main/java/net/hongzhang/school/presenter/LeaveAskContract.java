package net.hongzhang.school.presenter;

import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/6/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveAskContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setCourseList(List<Map<String, String>> courseList);

    }

    public interface Presenter {
        void publishLeave(String tsId, String vaContent, String startDateTime, String endDateTime);
    }
}
