package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/11
 * 名称：家长上传作品详情类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskDetailInfoVo {
    /**
     * 作品ID
     */
    private String activityWorksId;
    private String accountTsId;
    /**
     * 角色名称
     */
    private String tsName;
    /**
     * 标题
     */
    private  String title ;
    /**
     *  内容
     */
   private String content;
    /**
     *  上传时间
     */
   private String uploadTime ;
    /**
     *  视频
     */
   private String video ;
    /**
     *  截止时间
     */
    private String deadline;
    /**
     * 评价ID
     */
   private String appraiseId;
    /**
     * 图片
     */
    private List<String> imgUrls ;

    public String getActivityWorksId() {
        return activityWorksId;
    }

    public void setActivityWorksI(String activityWorksId) {
        this.activityWorksId = activityWorksId;
    }

    public String getAccountTsId() {
        return accountTsId;
    }

    public void setAccountTsId(String accountTsId) {
        this.accountTsId = accountTsId;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
