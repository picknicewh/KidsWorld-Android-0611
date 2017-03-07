package net.hongzhang.discovery.util;

import net.hongzhang.discovery.modle.CompilationVo;

import java.util.List;

/**
 * Created by wanghua on 2016/12/20.
 */
public class AlbumSelectContract {
    public  interface  View{
        void setAlbum(List<CompilationVo> compilationVos);
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
