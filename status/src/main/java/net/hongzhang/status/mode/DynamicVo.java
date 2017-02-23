package net.hongzhang.status.mode;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/8/1
 * 描    述：获取动态班级列表实体类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class DynamicVo implements Serializable {
    private String groupId;
    private String groupName;
    private String groupType;
    private String show;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
