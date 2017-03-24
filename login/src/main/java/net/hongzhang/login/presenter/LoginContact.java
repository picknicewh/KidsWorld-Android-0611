package net.hongzhang.login.presenter;

import android.widget.CheckBox;

import net.hongzhang.login.mode.CharacterSeleteVo;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LoginContact {
    public  interface  View{
        void setCharacterVoList(List<CharacterSeleteVo> characterSeleteVos);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    public interface Presenter{
        void getIsGologin(CheckBox checkBox, String accountId, String password, String sign, String md5);
        void selectUserSubmit(String tsid);
        void goUserAgreementActivity(String url,int source);
        void  goUpdateMessageActivity(String phoneNumber);
    }
}
