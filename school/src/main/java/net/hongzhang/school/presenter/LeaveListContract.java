package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.VacationVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveListContract {
    public interface View {
        void showLoadingDialog();

        void stopLoadingDialog();

        void setLeaveList(List<VacationVo> vacationVos,int beforeSize,int afterSize,List<String> titleList,List<Integer> resIdList);


    }

    public interface Presenter {
        void getTodayLeave(String tsId);

        void getAlreadyLeave(String tsId, int pageNumber, int pageSize);

        void getLeaveing(String tsId, int pageNumber, int pageSize);

        void getHistoryLeaveList(String tsId, int pageNumber, int pageSize);
    }
}
