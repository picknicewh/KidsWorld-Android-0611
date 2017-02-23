package net.hongzhang.discovery.util;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.modle.CompilationVo;

import java.util.List;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoDetailContract {
    public interface View{
        void setVideoList(List<ResourceVo> resourceVos);
        void setVideoInfo(ResourceVo resourceVo,int position);
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

    }
}
