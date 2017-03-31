package net.hongzhang.baselibrary.mode;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 作者： wh
 * 时间： 2016/11/30
 * 名称：资源
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceVo implements Parcelable ,Serializable{

    /**
     * code : null
     * albumId : null
     * themeId : null
     * albumName : null
     * namejc : null
     * imageUrl : http://img.mp.itc.cn/upload/20161220/35dc85374b6d4e9abfa51af4e200f76e_th.jpg
     * ordernumber : 99
     * recommended : 1
     * pay : 2
     * searchCount : 7642
     * updateName : admin
     * updateTime : 1487606400000
     * createName : hsh
     * createTime : 1482203187000
     * subtitlesUrl : null
     * commentCount : 3133
     * pvcount : 0
     * audit : 1
     * grade : 3
     * sets : 0
     * author : 来自网络
     * broadcastPace : 0
     * isPraise : 2
     * praiseNumber : null
     * isFavorites : 2
     * themeName : null
     * labelName : null
     * labelId : null
     * resourceUrl : null
     * fileSize : null
     * resourceId : 60f69f20ca144160850bc0bd38d3b2a3
     * source : 3
     * length : null
     * type : 3
     * content :
     * description : null
     * status : 1
     * resourceName : 一次旅行胜过N堂大课，带娃行走云南腾冲
     */
    /**
     * 资源编码（视频，音频）
     */
    private String code;
    /**
     * 专辑id
     */
    private String albumId;
    /**
     * 主题id
     */
    private String themeId;
    /**
     * 专辑名称
     */
    private String albumName;
    private String namejc;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 排序号
     */
    private int ordernumber;
    /**
     * 是否推荐（1：推荐，2：不推荐）
     */
    private int recommended;
    /**
     * 是否收费（1：收费，2：免费）
     */
    private int pay;
    /**
     * 搜索次数
     */
    private String searchCount;
    /**
     * 更新者
     */
    private String updateName;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 创建者
     */
    private String createName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 字幕地址
     */
    private String subtitlesUrl;
    /**
     * 评论数
     */
    private int commentCount;

    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 总点击数
     */
    private int pvcount;
    /**
     * 审核状态（1：已通过，2：未通过）
     */
    private int audit;
    /**
     * 学段（1：小班，2：中班，3：大班）
     */
    private int grade;
    /**
     * 作者
     */
    private String author;

    /**
     * 第几集（1：第一集，2：第二集，.....）
     */
    private int sets;

    /**
     * 播放时长
     */
    private String broadcastPace;
    /**
     * 1已点赞 2=未点赞
     */
    private int isPraise;
    /**
     * 点赞数
     */
    private int praiseNumber;
    /**
     * 1已收藏 2未收藏
     */
    private int isFavorites;

    /**
     * 简介
     */
    private String description;
    /**
     * 资源ID
     */
    private String resourceId;
    /**
     *主题名
     */
    private String themeName;
    /**
     * 标签名字
     */
    private String labelName;
    /**
     * 标签id
     */
    private String labelId;
    /**
     * 资源地址
     */
    private String resourceUrl;


    /**
     * 长度（时分秒形式）
     */
    private String length;
    /**
     * 类型（1：视频，2：音频，3：资讯）
     */
    private int type;
    /**
     * 内容（资讯：文本内容，音频：文本歌词）
     */
    private String content;
    /**
     * 来源（目前不用）
     */
    private int source;
    /**
     * 状态（1：显示，2：隐藏）
     */
    private int status;
    /**
     * 资源
     */
    private String resourceName;


    protected ResourceVo(Parcel in) {
        code = in.readString();
        albumId = in.readString();
        themeId = in.readString();
        albumName = in.readString();
        namejc = in.readString();
        imageUrl = in.readString();
        ordernumber = in.readInt();
        recommended = in.readInt();
        pay = in.readInt();
        searchCount = in.readString();
        updateName = in.readString();
        updateTime = in.readString();
        createName = in.readString();
        createTime = in.readString();
        subtitlesUrl = in.readString();
        commentCount = in.readInt();
        fileSize = in.readString();
        pvcount = in.readInt();
        audit = in.readInt();
        grade = in.readInt();
        author = in.readString();
        sets = in.readInt();
        broadcastPace = in.readString();
        isPraise = in.readInt();
        praiseNumber = in.readInt();
        isFavorites = in.readInt();
        description = in.readString();
        resourceId = in.readString();
        themeName = in.readString();
        labelName = in.readString();
        labelId = in.readString();
        resourceUrl = in.readString();
        length = in.readString();
        type = in.readInt();
        content = in.readString();
        source = in.readInt();
        status = in.readInt();
        resourceName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(albumId);
        dest.writeString(themeId);
        dest.writeString(albumName);
        dest.writeString(namejc);
        dest.writeString(imageUrl);
        dest.writeInt(ordernumber);
        dest.writeInt(recommended);
        dest.writeInt(pay);
        dest.writeString(searchCount);
        dest.writeString(updateName);
        dest.writeString(updateTime);
        dest.writeString(createName);
        dest.writeString(createTime);
        dest.writeString(subtitlesUrl);
        dest.writeInt(commentCount);
        dest.writeString(fileSize);
        dest.writeInt(pvcount);
        dest.writeInt(audit);
        dest.writeInt(grade);
        dest.writeString(author);
        dest.writeInt(sets);
        dest.writeString(broadcastPace);
        dest.writeInt(isPraise);
        dest.writeInt(praiseNumber);
        dest.writeInt(isFavorites);
        dest.writeString(description);
        dest.writeString(resourceId);
        dest.writeString(themeName);
        dest.writeString(labelName);
        dest.writeString(labelId);
        dest.writeString(resourceUrl);
        dest.writeString(length);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeInt(source);
        dest.writeInt(status);
        dest.writeString(resourceName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResourceVo> CREATOR = new Creator<ResourceVo>() {
        @Override
        public ResourceVo createFromParcel(Parcel in) {
            return new ResourceVo(in);
        }

        @Override
        public ResourceVo[] newArray(int size) {
            return new ResourceVo[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getNamejc() {
        return namejc;
    }

    public void setNamejc(String namejc) {
        this.namejc = namejc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getRecommended() {
        return recommended;
    }

    public void setRecommended(int recommended) {
        this.recommended = recommended;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSubtitlesUrl() {
        return subtitlesUrl;
    }

    public void setSubtitlesUrl(String subtitlesUrl) {
        this.subtitlesUrl = subtitlesUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getPvcount() {
        return pvcount;
    }

    public void setPvcount(int pvcount) {
        this.pvcount = pvcount;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getBroadcastPace() {
        return broadcastPace;
    }

    public void setBroadcastPace(String broadcastPace) {
        this.broadcastPace = broadcastPace;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public int getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(int isFavorites) {
        this.isFavorites = isFavorites;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public static Creator<ResourceVo> getCREATOR() {
        return CREATOR;
    }
}
