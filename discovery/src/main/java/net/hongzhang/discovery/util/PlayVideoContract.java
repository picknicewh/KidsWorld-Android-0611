package net.hongzhang.discovery.util;

import android.view.SurfaceView;

import net.hongzhang.baselibrary.mode.ResourceVo;

import java.util.List;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoContract {
    public interface View{
        void setVideoList(List<ResourceVo> compilationVos);
        void setvideoInfo(ResourceVo resourceVo,int position);
        void setIsPlay(boolean isPlay);
        void setCurrent(int progress);
        void setDuration(int max);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface  Presenter{
        void savePlayTheRecord(String tsId, String resourceId,int broadcastPace,int type);
        void getVideoList(String tsId, String themeId);
        void subFavorate(String albumId,int cancel);
        void subComment(String resourceId,String tsId,String content);
        boolean getPrepared();
        void setup();
        void play();
        void pause();
        void changeSeeBar(int progress);
        void setPosition(int position);
        void setScreenDirection(int orientation,SurfaceView surfaceView);
    }
}
