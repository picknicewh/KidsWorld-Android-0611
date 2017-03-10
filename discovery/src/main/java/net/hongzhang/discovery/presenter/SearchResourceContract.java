package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.CompilationVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public interface SearchResourceContract {
    public  interface  View{
        void setResourceList(List<CompilationVo> musicCompilationVos);
        void setResourceSize(int size);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getSearchResourceList(String tsId,int type,int pageSize, int pageNumber, String account_id,String tag);
        void startVideoActivity(String themeId);
        void startMusicActivity(String themeId, String resourceId);

    }
}
