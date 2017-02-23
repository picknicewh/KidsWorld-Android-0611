package net.hongzhang.user.mode;

/**
 * 作者： Administrator
 * 时间： 2016/11/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ScanVo {
    /**
     *请求登录的IP地址
     */
    private String Login_ip;
    /**
     *令牌
     */
    private String token	;

    public String getLogin_ip() {
        return Login_ip;
    }

    public void setLogin_ip(String login_ip) {
        Login_ip = login_ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
