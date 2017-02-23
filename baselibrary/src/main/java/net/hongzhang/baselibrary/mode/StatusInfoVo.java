package net.hongzhang.baselibrary.mode;

/**
 * 作者： wh
 * 时间： 2016/10/20
 * 名称：动态通知类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusInfoVo{


    /**
     *
     */
    private int uid;
    /**
     * 动态id
     */
    private String createTime;
    /**
     * 用户id
     */
    private String tsId;
    /**
     * 用户头像
     */
    private String  imgUrl;
    /**
     *是否已经阅读
     */
    private int   isRead;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public String getTsId() {
        return tsId;
    }

    public String getCreateTime() {
        return createTime;
    }
    public int getIsRead() {
        return isRead;
    }
    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

}
