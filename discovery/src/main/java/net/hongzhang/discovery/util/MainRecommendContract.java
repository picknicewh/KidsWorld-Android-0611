package net.hongzhang.discovery.util;

import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.user.mode.BannerVo;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/12/12
 * 名称：乐园首页模型
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MainRecommendContract {
    public  interface View{
        void setRecommendVoMusicList(List<CompilationVo> compilationVos );
        void setRecommendVoClassList(List<CompilationVo> compilationVos);
        void setRecommendVoConsultList(List<CompilationVo> compilationVos);
        void setBannerList(List<BannerVo> bannerList);
        void rushData();
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{

        void getRecommendResource(String tsId,int size,int type);
        void getBanner(String tsId);
        void startVideoListActivity(String AlbumId);
        void startMusicListActivity(String AlbumId);
        void startConsultListActivity(String AlbumId);
        void startVideoActivity(String AlbumId);
        void startMusicActivity(String AlbumId, String resourceId);
        void startConsultActivity(int resourceId);
        void startSearchActivity();
        void startHistoryActivity();
        void startUserActivity();
    }
}
