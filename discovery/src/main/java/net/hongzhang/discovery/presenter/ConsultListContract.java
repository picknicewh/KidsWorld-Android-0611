package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.ThemeVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public interface ConsultListContract {
    public  interface  View{
        void  setThemeList(List<ThemeVo> themeList);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getThemeList(int type,int pageNumber,int pageSize);
        void starConsultActivity(String resourceId);
    }
}
