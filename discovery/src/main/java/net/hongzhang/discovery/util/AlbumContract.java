package net.hongzhang.discovery.util;

import net.hongzhang.discovery.modle.RecommendVo;

import java.util.List;

/**
 * Created by wanghua on 2016/12/20.
 */
public class AlbumContract {
    public  interface  View{
        void setAlbum(List<RecommendVo> recommendVos);
        void setResourceSize(int size);
        void showLoadingDialog();
        void stopLoadingDialog();
     }
    public interface Presenter{
        void getAlbumList( int type,int pageSize,int pageNumber);
        void startMusicActivity(String themeId,String resourceId);
        void starVedioActivity(String themeId);
        void starConsultActivity(String resourceId);
    }
}
