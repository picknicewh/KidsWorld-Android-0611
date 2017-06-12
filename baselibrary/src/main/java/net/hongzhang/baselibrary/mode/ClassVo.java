package net.hongzhang.baselibrary.mode;

/**
 * 作者： wanghua
 * 时间： 2017/6/8
 * 名称：班级信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ClassVo {
    /**
     * 班级ID
     */
   private String	  groupId;
    /**
     * 班级名称
     */
    private String groupName ;
    /**
     * 类别	此处默认为1
     */
    private int 	groupType	;

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

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }
}
