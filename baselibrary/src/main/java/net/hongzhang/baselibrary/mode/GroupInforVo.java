package net.hongzhang.baselibrary.mode;

/**
 * 作者： Administrator
 * 时间： 2016/8/31
 * 名称：本地数据库保存的群组信息
 *      不包含每个群组中含有什么联系人
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupInforVo {
    private int uid;
    private String classId;
    private String groupName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
