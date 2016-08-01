package net.hunme.message.bean;

/**
 * 作者： Administrator
 * 时间： 2016/8/1
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ClassInforJson {

    /**
     * data : 系统繁忙，请稍后再试
     * sign : null
     * code : -9
     * msec : 1470029319224
     */

    private String data;
    private Object sign;
    private String code;
    private String msec;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getSign() {
        return sign;
    }

    public void setSign(Object sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsec() {
        return msec;
    }

    public void setMsec(String msec) {
        this.msec = msec;
    }
}
