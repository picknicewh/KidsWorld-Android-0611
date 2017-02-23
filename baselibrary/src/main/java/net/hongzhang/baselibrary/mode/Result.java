package net.hongzhang.baselibrary.mode;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/19
 * 描    述：服务端返回基本数据格式
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class Result<T> implements Serializable {
    private T data;      //返回数据

    private String sign;  //签名

    private String msec;  //当前时间毫秒数

    private String code;  //状态 0-访问成功 1-访问失败

    public boolean isSuccess(){
        return "0".equals(getCode());
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
