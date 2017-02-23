package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/9/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SyllabusVo {
    /**
     * 标题
     */
    private String  title;
    /**
     * 图片地址
     */
   private List<String>  imgs;
    /**
     * 发布时间
     */
   private String	 creationTime;

    /**
     * 	课程表ID
     */
    private String syllabusId;
    public String getSyllabusId() {
        return syllabusId;
    }

    public void setSyllabusId(String syllabusId) {
        this.syllabusId = syllabusId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
