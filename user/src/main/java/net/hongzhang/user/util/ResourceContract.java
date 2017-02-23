package net.hongzhang.user.util;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.user.mode.CompilationsVo;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceContract {
    public interface View{
        void setResourceVoList(List<ResourceVo> resourceVoList, List<CompilationsVo> compilationsVos);
        void setResourceSize(int size);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getCollectResourceList(int pageNumber,int pageSize,int type);
        void getPlayRecordList(int pageNumber,int pageSize,int type);
        void starVedioActivity(String themeId);
        void   startMusicActivity(String themeId, String resourceId);
        void starConsultActivity(String resourceId);
    }
}
