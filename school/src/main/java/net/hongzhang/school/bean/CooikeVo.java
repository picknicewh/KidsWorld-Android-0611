package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/9/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CooikeVo {
    /**
     * 日期
     */
    private String date	;
    /**
     * 星期
     */
    private    String day	;
    /**
     * 	菜谱
     */
    private List<DishesVo> dishesList;

    public List<DishesVo> getDishesList() {
        return dishesList;
    }

    public void setDishesList(List<DishesVo> dishesList) {
        this.dishesList = dishesList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
