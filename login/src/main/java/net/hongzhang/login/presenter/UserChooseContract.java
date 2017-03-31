package net.hongzhang.login.presenter;

import net.hongzhang.login.mode.CharacterSeleteVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class UserChooseContract {
    public  interface  View{
        void setCharacterVoList(List<CharacterSeleteVo> characterSeleteVos);
        void setIsChoose(boolean isChoose);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getIsGologin(String accountId, String password, String sign);
        void selectUserSubmit(String tsid,String sign);
    }
}
