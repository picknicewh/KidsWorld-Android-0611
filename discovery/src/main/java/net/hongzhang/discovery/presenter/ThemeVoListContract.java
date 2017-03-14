package net.hongzhang.discovery.presenter;

import net.hongzhang.discovery.modle.CompilationVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public class ThemeVoListContract {
public  interface  View{
    void setThemeVoList(List<CompilationVo> compilationVos);
    void setThemeVoSize(int size);
    void showLoadingDialog();
    void stopLoadingDialog();
}
public interface Presenter{
    void getThemVoList(String themeId, int pageSize, int pageNumber);
    void startMusicActivity(String themeId, String resourceId);
    void starVedioActivity(String themeId,String resourceId);
}
}
