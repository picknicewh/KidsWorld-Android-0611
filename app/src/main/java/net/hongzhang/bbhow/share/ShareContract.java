package net.hongzhang.bbhow.share;

import net.hongzhang.status.mode.DynamicVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/28
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public interface ShareContract {
    interface View {
        void setClassList(List<DynamicVo> dynamicVoList);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    interface Presenter {
        void getClassList(String tsId);
    }
}
