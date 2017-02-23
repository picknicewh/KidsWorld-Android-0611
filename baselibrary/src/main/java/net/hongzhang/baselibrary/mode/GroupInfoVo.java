package net.hongzhang.baselibrary.mode;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/25
 * 名称：群
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupInfoVo implements Serializable {
    /**
     * groupName : 苗苗班
     * ryId : 298f1648653840fdaa6c396830025af5
     * classId : 298f1648653840fdaa6c396830025af5
     * menberList : null
     */
    /**
     * 群主名字
     */
    private String groupName;
    /**
     *融云通讯ID
     */
    private String ryId;
    /**
     *班级ID
     */
    private String classId;
    /**
     *群成员
     */
    private List<ContractInfoVo> menberList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<ContractInfoVo> getMenberList() {
        return menberList;
    }

    public void setMenberList(List<ContractInfoVo> menberList) {
        this.menberList = menberList;
    }

}
