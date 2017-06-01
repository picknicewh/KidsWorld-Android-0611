package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/5/18
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SelectTagVo {
    private int  childPosition;
    private int groupPosition;
    private String  tagName;

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
