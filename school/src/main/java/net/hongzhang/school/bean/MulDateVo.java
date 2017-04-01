package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/4/1
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MulDateVo {
    /**
     * 日期
     */
    private String date;
    /**
     * 标记=-1上个月=0本月=1下个月
     */
    private int sign;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
