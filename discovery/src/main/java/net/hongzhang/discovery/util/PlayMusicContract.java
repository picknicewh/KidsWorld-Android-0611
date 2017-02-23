package net.hongzhang.discovery.util;

import net.hongzhang.baselibrary.mode.ResourceVo;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public interface PlayMusicContract  {
    interface View {
        void setSongList(List<ResourceVo> resourceVos);
        void setSongInfo(int position);
        void setIsPlay(boolean isPlay);
        void setPosition(int position);
        void setPlayMode(PlayMode playMode);
        void showLoadingDialog();
        void stopLoadingDialog();

    }

    interface Presenter{
        void  getResourceDetail(String tsId, String resourceId);
        void getIsUserPay(String tsId,String accountId);
        void  getSongList(String tsId, String themeId);
        void savePlayTheRecord(String tsId, String resourceId,int broadcastPace,int type);
        void setup();
        void play();
        void pause();
        void nextSong();
        void preSong();
        void sendLockText(ResourceVo resourceVo, boolean isPlaying);
        void showNotification(boolean isPlaying,ResourceVo resourceVo);
        void changeSeeBar(int progress);
        void cleanNotification();
        void registerReceiver();
        void unRisterReceiver();
    }
}
