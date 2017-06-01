package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：排行榜类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RankingInfoVo {

    /**
     * 作品ID
     */
    private String activityWorksId;
    private String accountTsId;
    /**
     *是否公开	1公开
     2不公开
     */
    private int  publicity;
    /**
     *	分数
     */
    private int  score;
    /**
     * 角色名称
     */
    private String tsName;
    /**
     * 头像
     */
    private String imgUrl;
    /**
     * 评价id
     */
    private String appraiseId;

    public String getActivityWorksId() {
        return activityWorksId;
    }

    public void setActivityWorksId(String activityWorksId) {
        this.activityWorksId = activityWorksId;
    }

    public String getAccountTsId() {
        return accountTsId;
    }

    public void setAccountTsId(String accountTsId) {
        this.accountTsId = accountTsId;
    }

    public int getPublicity() {
        return publicity;
    }

    public void setPublicity(int publicity) {
        this.publicity = publicity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }
}
