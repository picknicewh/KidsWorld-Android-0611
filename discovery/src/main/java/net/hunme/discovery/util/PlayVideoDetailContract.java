package net.hunme.discovery.util;

import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.discovery.modle.CompilationVo;

import java.util.List;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoDetailContract {
    public interface View{
        void setVideoList(List<ResourceVo> resourceVos);
        void setVideoInfo(ResourceVo resourceVo,int position);
        void setIsPlay(boolean isPlay);
        void setDuration(int duration);
        void setCurrent(int progress);
        void setRecommendList(List<CompilationVo> compilationVos);
        void showLoadingDialog();
        void stopLoadingDialog();

    }
    public interface Presenter{
        void getVideoList(String tsId, String themeId);
        void getRecommendList(String tsId,int size,int type);
        void getCommentList(String resourceId,int pageSize,int pageNumber);
        void subComment(String tsId,String resourceId,String content);
        void subFavorate(String albumId, int cancel);
        void setup();
        void play();
        void pause();
        void nextSong();
        void preSong();
        void changeSeeBar(int progress);
        void setPosition(int position);
        boolean getPrepared();
        void setScreenDirection(int orientation, RelativeLayout rl_control,RelativeLayout rl_control_full, LinearLayout ll_detail, SurfaceView surfaceView);
    }
}
