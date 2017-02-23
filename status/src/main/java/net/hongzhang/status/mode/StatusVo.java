package net.hongzhang.status.mode;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/10/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusVo implements Serializable{
    /**
     * 角色ID
     */
    private String  tsId;
    /**
     * 角色名称
     */
    private String tsName;
    /**
     * 1=学生，2=老师
     * 	角色类别
     */
     private int tsType	;
    /**
     *角色头像URL
     *
     */
    private String img	;
    /**
     *	动态ID
     */
   private String   dynamicId;
    /**
     *1=图片 2=视屏 3=纯文字
     */
     private int  dynamicType	;
    /**
     *动态图片地址
     */
   private List<String> imgUrl	;
    /**
     *小视屏地址
     */
    private String videoUrl	;
    /**
     *文本
     */
    private String text	;
    /**
     *点赞人名称
     */
   private java.util.List<String>list	;
    /**
     *是否赞
     *  1=已赞 2=未赞
     */
    private int	 isAgree;
    /**
     * 是否收藏
     *  1=已收藏 2=未收藏
     */
    private   int isCollect;
    /**
     *发布时间
     */
   private String createTime;
    /**
     * 评论
     */
   private List<DynamidRew> dynamidRew	;
    /**
     * 评论数
    */
    private int rewCount;

    public int getRewCount() {
        return rewCount;
    }

    public void setRewCount(int rewCount) {
        this.rewCount = rewCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public int getTsType() {
        return tsType;
    }

    public void setTsType(int tsType) {
        this.tsType = tsType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public List<DynamidRew> getDynamidRew() {
        return dynamidRew;
    }

    public void setDynamidRew(List<DynamidRew> dynamidRew) {
        this.dynamidRew = dynamidRew;
    }
}

