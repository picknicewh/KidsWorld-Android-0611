package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：评价详情
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskCommentDetailVo {
    /**
     * appraiseId : daa6515626154dcab6715d10d9d0de4d
     * content : 不错
     * score : 80
     * list : [{"parentId":"100000","parentName":"完成情况","tags":[{"tagId":"1000001","tagName":"A1"},{"tagId":"1000003","tagName":"A3"}]},{"parentId":"300000","parentName":"能力提升","tags":[{"tagId":"3000001","tagName":"C1"},{"tagId":"3000002","tagName":"C2"}]}]
     */
    private String appraiseId;
    private String content;
    private int score;
    private List<ParentVo> list;

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ParentVo> getList() {
        return list;
    }

    public void setList(List<ParentVo> list) {
        this.list = list;
    }

    public static class ParentVo {
        /**
         * parentId : 100000
         * parentName : 完成情况
         * tags : [{"tagId":"1000001","tagName":"A1"},{"tagId":"1000003","tagName":"A3"}]
         */

        private String parentId;
        private String parentName;
        private List<ChildVo> tags;

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

        public List<ChildVo> getTags() {
            return tags;
        }

        public void setTags(List<ChildVo> tags) {
            this.tags = tags;
        }

        public static class ChildVo {
            /**
             * tagId : 1000001
             * tagName : A1
             */

            private String tagId;
            private String tagName;

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
    }
/*
    *//**
     * 评价ID
     *//*
    private String appraiseId;
    *//**
     * 内容
     *//*
    private String content;
    *//**
     * 分数
     *//*
    private int score;
    private List
    private List<Tag> PARENTS;
  public   class Tag {
        *//**
         * 父级维度标签ID
         *//*
        private String PARENT_ID;
        *//**
         * 父级维度标签名称
         *//*
        private String PARENT_NAME;
        *//**
         * 父级列表
         *//*
        private List<childTags> TAGS;

        public class childTags {
            *//**
             * 维度标签ID
             *//*
            private String TAG_ID;
            *//**
             * 维度标签名称
             *//*
            private String TAG_NAME;

            public String getTAG_ID() {
                return TAG_ID;
            }

            public void setTAG_ID(String TAG_ID) {
                this.TAG_ID = TAG_ID;
            }

            public String getTAG_NAME() {
                return TAG_NAME;
            }

            public void setTAG_NAME(String TAG_NAME) {
                this.TAG_NAME = TAG_NAME;
            }
        }

        public String getPARENT_ID() {
            return PARENT_ID;
        }

        public void setPARENT_ID(String PARENT_ID) {
            this.PARENT_ID = PARENT_ID;
        }

        public String getPARENT_NAME() {
            return PARENT_NAME;
        }

        public void setPARENT_NAME(String PARENT_NAME) {
            this.PARENT_NAME = PARENT_NAME;
        }

        public List<childTags> getTAGS() {
            return TAGS;
        }

        public void setTAGS(List<childTags> TAGS) {
            this.TAGS = TAGS;
        }
    }

    public String getAPPRAISE_ID() {
        return APPRAISE_ID;
    }

    public void setAPPRAISE_ID(String APPRAISE_ID) {
        this.APPRAISE_ID = APPRAISE_ID;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public int getSCORE() {
        return SCORE;
    }

    public void setSCORE(int SCORE) {
        this.SCORE = SCORE;
    }

    public List<Tag> getPARENTS() {
        return PARENTS;
    }

    public void setPARENTS(List<Tag> PARENTS) {
        this.PARENTS = PARENTS;
    }*/
}
