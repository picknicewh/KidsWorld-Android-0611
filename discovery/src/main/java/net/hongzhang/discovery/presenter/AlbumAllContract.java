package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.ThemeVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public class AlbumAllContract {
public  interface  View{
    void setAllAlbumList(List<ThemeVo> themeVos);
    void showLoadingDialog();
    void stopLoadingDialog();
}
public interface Presenter{
    void getAllAlbumList( int type,int pageSize,int pageNumber);
    void startMusicActivity(String themeId,String resourceId);
    void starVedioActivity(String themeId);
}
}
