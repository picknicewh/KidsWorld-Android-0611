package net.hongzhang.status.presenter;

import android.widget.RelativeLayout;

import net.hongzhang.status.mode.DynamicVo;
import net.hongzhang.status.mode.StatusVo;
import net.hongzhang.status.widget.ChooseClassPopWindow;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/30
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusContract {
    public  interface View{
        void setStatusHead(List<DynamicVo> dynamicVoList);
        void setStatusList(List<StatusVo> statusList);
        void setDynamicVo(DynamicVo dynamicVo);
        void setIsClickWindow(boolean isClickWindow);
        void setTvStatusbar(boolean isVisible);
        void setTargetId(String targetId);
        void setHeadInfo(int count,String imageUrl );
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void registerReceiver();
        void unregisterReceiver();
        String getLastUpdateTime();
        void getDynamicHead();
        void getDynamicList(String groupId,String groupType,int pageSize,int pageNum,int type,String dynamicId);
        void goSettingNetWork();
        void sendStatusDosBroadcast(String targetId,String groupId);
        void goMessageDetailActivity();
        void goPublishChoose(String classId, android.view.View view);
        void showClassChoose(ChooseClassPopWindow c  , RelativeLayout rl_toolbar);

    }
}
