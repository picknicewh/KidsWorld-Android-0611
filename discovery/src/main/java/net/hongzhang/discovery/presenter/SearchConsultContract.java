package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.ConsultInfoVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public interface SearchConsultContract {
    public  interface  View{
        void setConsultList(List<ConsultInfoVo> consultInfoVoList);
        void setConsultInfoSize(int size);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getSearchResourceList(String tsId, int type, int pageSize, int pageNumber, String account_id, String tag);
        void startConsultActivity(String resourceId);
    }
}
