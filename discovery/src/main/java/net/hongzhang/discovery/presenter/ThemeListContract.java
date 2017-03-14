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


public class ThemeListContract {
public  interface  View{
    void setThemeList(List<ThemeVo> themeVos);
    void showLoadingDialog();
    void stopLoadingDialog();
}
public interface Presenter{
    void getThemeList( int type,int pageSize,int pageNumber);
    void starThemeVoListActivity(String themeId,String themeName,int type);

}
}
