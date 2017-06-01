package net.hongzhang.status.presenter;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublicStatusContract {
    public interface View {

        void setSubTitleEnable(boolean enable);
        void showLoadingDialog();

        void stopLoadingDialog();
    }

    public interface Presenter {

        void publishstatus(String dyContent, String dynamicType, List<String> itemList, String classId);
        void publishcaurse(String dyContent,List<String> itemList );
        void getExitPrompt();
    }
}
