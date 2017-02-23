package net.hongzhang.message.bean;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/9/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupMemberVos {
    /**
     * qun chengyna
     */
    private List<GroupMemberVo>  memberList;
    /**
     *群主ID
     */
    private String ganapatiId;
    /**
     *0=固定群，1=自建群
     */
    private int fixed;

    public List<GroupMemberVo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GroupMemberVo> memberList) {
        this.memberList = memberList;
    }

    public String getGanapatiId() {
        return ganapatiId;
    }

    public void setGanapatiId(String ganapatiId) {
        this.ganapatiId = ganapatiId;
    }

    public int getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }
}
