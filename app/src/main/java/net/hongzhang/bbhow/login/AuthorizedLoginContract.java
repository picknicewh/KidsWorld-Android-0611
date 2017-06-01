package net.hongzhang.bbhow.login;

/**
 * 作者： wanghua
 * 时间： 2017/5/31
 * 名称：授权登录
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class AuthorizedLoginContract {
    interface View {
        void setToken(String token);
        void showLoadingDialog();
        void stopLoadingDialog();
    }
    interface Presenter {
        void authorizedLogin(String tsId,String keyNumber);
    }
}
