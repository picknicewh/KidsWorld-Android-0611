package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.modle.SearchKeyVo;

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
    interface View {
        void setCompilationVoList(List<CompilationVo> musicCompilationVos);

        void setResourceList(List<ResourceVo> resourceList);

        void setResourceSize(int compilationVo);

        void setSearchHistoryList(List<SearchKeyVo> searchKeyVoList);

        void showLoadingDialog();

        void stopLoadingDialog();

        void setloadMoreVis(boolean isVis);
    }

    interface Presenter {
        void getSearchResourceList(String tsId, int type, int pageSize, int pageNumber, String account_id, String tag, int flag);

        void getSongList(String tsId, String themeId, String resourceId);

        void getSearchHistoryList(int type);

        void saveSearchKey(String key, int type, String tsId, String targetName, String targetId);

        void insertKey(int type, String tag);

        void startVideoActivity(String themeId, String resourceId);

        void startMusicActivity(List<ResourceVo> resourceVos, String resourceId);
    }
}
