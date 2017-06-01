package net.hongzhang.school.bean;


import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：活动详情类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ActivityDetailVo {
    /**
     * 活动ID
     */
    private String activityId;
    /**
     * 是否需要评价	1需要评价 2不需要
     */
    private int appraise;
    /**
     * 内容
     */
    private String content;
    /**
     * 截止时间	毫秒数
     */
    private long deadline;
    /**
     * 开始时间	毫秒数
     */
    private long postTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 维度名称
     */
    private List<String> dimensionalityName;
    /**
     * 图片地址
     */
    private List<String> imgUrls;
    /**
     * 文件地址
     */
    private List<String> files;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getAppraise() {
        return appraise;
    }

    public void setAppraise(int appraise) {
        this.appraise = appraise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDimensionalityName() {
        return dimensionalityName;
    }

    public void setDimensionalityName(List<String> dimensionalityName) {
        this.dimensionalityName = dimensionalityName;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
