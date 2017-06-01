package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：老师获取家长作品列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskInfoVo {
     /*{"data":[{"accountTsId":"bea41d9d873441ad88a8b117a098cee7ee7",
     "createTime":"2017-05-11 17:25:57","tsName":"清清(父亲)","activityWorksId":"c084ebd2ec8f4d63bc7c5516304",
     "appraiseId":null,"imgUrl":"http://eduslb.openhunme.com/KidsWorld*/
    private String accountTsId;
    /**
     * 上传时间
     */
    private String createTime;
    /**
     * 角色名称
     */
    private String tsName;
    /**
     * 活动id
     */
    private String activityWorksId;
    /**
     * 评价id
     */
    private String appraiseId;
    /**
     * 角色头像
     */
    private String imgUrl;

    public String getAccountTsId() {
        return accountTsId;
    }

    public void setAccountTsId(String accountTsId) {
        this.accountTsId = accountTsId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getActivityWorksId() {
        return activityWorksId;
    }

    public void setActivityWorksId(String activityWorksId) {
        this.activityWorksId = activityWorksId;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

 /*
    private String CREATE_TIME;

    private String TS_NAME;

    private String  ACTIVITY_WORKS_ID;

    private String APPRAISE_ID;

    public String getAPPRAISE_ID() {
        return APPRAISE_ID;
    }

    public void setAPPRAISE_ID(String APPRAISE_ID) {
        this.APPRAISE_ID = APPRAISE_ID;
    }

    public String getACTIVITY_WORKS_ID() {
        return ACTIVITY_WORKS_ID;
    }

    public void setACTIVITY_WORKS_ID(String ACTIVITY_WORKS_ID) {
        this.ACTIVITY_WORKS_ID = ACTIVITY_WORKS_ID;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getTS_NAME() {
        return TS_NAME;
    }

    public void setTS_NAME(String TS_NAME) {
        this.TS_NAME = TS_NAME;
    }*/
}
