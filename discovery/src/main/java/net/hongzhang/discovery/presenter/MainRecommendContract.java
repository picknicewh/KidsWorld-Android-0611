package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.baselibrary.mode.ResourceVo;
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
        void setRecommendVoConsultList(List<ResourceVo> resourceVoList);
        void setBannerList(List<BannerVo> bannerList);
        void rushData();
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getRecommendResource(String tsId,int size,int type);
        void getSongList(String tsId, String themeId);
        void getRecommendConsult(String tsId,int pageSize ,String account_id);
        void getBanner(String tsId);
        void startVideoListActivity();
        void startMusicListActivity();
        void startConsultListActivity();
        void startVideoActivity(String AlbumId);
       // void startMusicActivity(List<ResourceVo> resourceVos,String AlbumId, String resourceId, ImageView imageView);
        void startMusicActivity(List<ResourceVo> resourceVos,String resourceId);
        void startConsultActivity(String resourceId);
        void startThemeVoListActivity(String themeName,String themeId);
        void startSearchActivity();
        void startHistoryActivity();
        void startUserActivity();
    }
}
