package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/9/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DishesVo {
    /**
     * 	类别（如：早餐，早餐加餐.....）
     */
    private String dinnerTime;
    /**
     * 图片地址
     */
    private List<String> cookUrl;
    /**
     * 菜名

     */
    private String  cookName;
    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public List<String> getCookUrl() {
        return cookUrl;
    }

    public void setCookUrl(List<String> cookUrl) {
        this.cookUrl = cookUrl;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

}
