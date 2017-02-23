package net.hongzhang.user.mode;


import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/10/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DynamicInfoVo {
    private String createTime;
    /**
     *角色类别
     * 1=学生，0=老师
     */
    private int  tsType	;
    /**
     * 角色头像URL
     */
    private String img;

    /**
     * 点赞数
     */
    private List<String> list;

    /**
     * 动态ID
     */
    private String	 dynamicId	;
    /**
     *	1=图片 2=视屏 3=纯文字
     */
    private int dynamicType	;
    /**
     *动态图片地址
     */
    private  ArrayList<String> imgUrl	;
    /**
     *小视屏地址
     */
    private String	videoUrl;
    /**
     *文本
     */
    private String	text;
    /**
     *是否赞
     */
    private int  isAgree;
    /**
     * 评论
     *  1=已赞 2=未赞
     */
   //private List<Comment>  dynamidRewList	;
    /**是否收藏
     * 1=已收藏 2=未收藏
     */
    private int	  isCollect	;
    /**
     * 日期
     */
   private  String date;
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

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
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

    public ArrayList<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl( ArrayList<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
