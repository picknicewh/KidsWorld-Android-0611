package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：家园互动列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ActivityInfoVos {

    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 分类ID
     */
    private String type;
    /**
     * 活动列表
     */
    private List<ActivityInfoVo> activityJsonList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ActivityInfoVo> getActivityJsonList() {
        return activityJsonList;
    }

    public void setActivityJsonList(List<ActivityInfoVo> activityJsonList) {
        this.activityJsonList = activityJsonList;
    }
}
