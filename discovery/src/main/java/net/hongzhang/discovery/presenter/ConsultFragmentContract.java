package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.ResourceVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public interface ConsultFragmentContract {
    public  interface  View{
        void setConsultList(List<ResourceVo> resourceVos);
        void setConsultInfoSize(int size);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getConsultList( int pageSize, int pageNumber, String themeId, String account_id);
        void starConsultActivity(String resourceId);
    }
}
