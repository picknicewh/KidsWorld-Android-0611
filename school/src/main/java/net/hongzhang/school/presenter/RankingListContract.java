package net.hongzhang.school.presenter;

import net.hongzhang.school.bean.RankingInfoVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：排行榜
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RankingListContract {
    public interface View {
        void showLoadingDialog();
        void stopLoadingDialog();
        void setRankingList(List<RankingInfoVo> rankingInfoVo);
    }

    public interface Presenter {
        void getRankingList(String activityId);
    }
}
