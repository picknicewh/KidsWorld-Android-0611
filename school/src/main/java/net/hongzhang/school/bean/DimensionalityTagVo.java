package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：教师评分用户维度
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DimensionalityTagVo {
    /**
     *	父级维度标签
     */
    private String  parentId;
    /**
     *	父级维度名称
     */
    private String parentName;
    /**
     *子标签列表
     */
    private List<ChildTagVo> tags;


    public static class ChildTagVo{
        /**
         * 维度标签
         */
        private    String tagId	;
        /**
         * 维度名称
         */
        private   String tagName	;

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<ChildTagVo> getTags() {
        return tags;
    }

    public void setTags(List<ChildTagVo> tags) {
        this.tags = tags;
    }
}
