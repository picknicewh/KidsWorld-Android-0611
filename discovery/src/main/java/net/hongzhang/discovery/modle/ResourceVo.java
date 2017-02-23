package net.hongzhang.discovery.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： wh
 * 时间： 2016/11/30
 * 名称：资源
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceVo implements Parcelable {
    /**
     * 资源ID
     */
    private String resourceId;
    /**
     * 资源
     */
    private String resourceName;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 资源地址
     */
    private String resourceUrl;
    /**
     * 字幕地址
     */
    private String subtitlesUrl;
    /**
     * 排序号
     */
    private int ordernumber;
    /**
     * 简介
     */
    private String description;
    /**
     * 内容（资讯：文本内容，音频：文本歌词）
     */
    private String content;
    /**
     * 资源编码（视频，音频）
     */
    private String code;
    /**
     * 来源（目前不用）
     */
    private int source;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 长度（时分秒形式）
     */
    private String length;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 总点击数
     */
    private int pvcount;
    /**
     * 类型（1：视频，2：音频，3：资讯）
     */
    private int type;
    /**
     * 状态（1：显示，2：隐藏）
     */
    private int status;
    /**
     * 是否推荐（1：推荐，2：不推荐）
     */
    private int recommended;
    /**
     * 审核状态（1：已通过，2：未通过）
     */
    private int audit;
    /**
     * 学段（1：小班，2：中班，3：大班）
     */
    private int grade;
    /**
     * 是否收费（1：收费，2：免费）
     */
    private int pay;
    /**
     * 第几集（1：第一集，2：第二集，.....）
     */
    private int sets;
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
     * 搜索次数
     */
    private String searchCount;
    /**
     * 播放时长
     */
    private int broadcastPace;
    /**
     * 专辑名称
     */
    private String albumName;
    /**
     * 专辑id
     */
    private String albumId;
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


    protected ResourceVo(Parcel in) {
        resourceId = in.readString();
        resourceName = in.readString();
        imageUrl = in.readString();
        resourceUrl = in.readString();
        subtitlesUrl = in.readString();
        ordernumber = in.readInt();
        description = in.readString();
        content = in.readString();
        code = in.readString();
        source = in.readInt();
        commentCount = in.readInt();
        length = in.readString();
        fileSize = in.readString();
        pvcount = in.readInt();
        type = in.readInt();
        status = in.readInt();
        recommended = in.readInt();
        audit = in.readInt();
        grade = in.readInt();
        pay = in.readInt();
        sets = in.readInt();
        updateName = in.readString();
        updateTime = in.readString();
        createName = in.readString();
        createTime = in.readString();
        searchCount = in.readString();
        broadcastPace = in.readInt();
        albumName = in.readString();
        albumId = in.readString();
        isPraise = in.readInt();
        praiseNumber = in.readInt();
        isFavorites = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resourceId);
        dest.writeString(resourceName);
        dest.writeString(imageUrl);
        dest.writeString(resourceUrl);
        dest.writeString(subtitlesUrl);
        dest.writeInt(ordernumber);
        dest.writeString(description);
        dest.writeString(content);
        dest.writeString(code);
        dest.writeInt(source);
        dest.writeInt(commentCount);
        dest.writeString(length);
        dest.writeString(fileSize);
        dest.writeInt(pvcount);
        dest.writeInt(type);
        dest.writeInt(status);
        dest.writeInt(recommended);
        dest.writeInt(audit);
        dest.writeInt(grade);
        dest.writeInt(pay);
        dest.writeInt(sets);
        dest.writeString(updateName);
        dest.writeString(updateTime);
        dest.writeString(createName);
        dest.writeString(createTime);
        dest.writeString(searchCount);
        dest.writeInt(broadcastPace);
        dest.writeString(albumName);
        dest.writeString(albumId);
        dest.writeInt(isPraise);
        dest.writeInt(praiseNumber);
        dest.writeInt(isFavorites);
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubtitlesUrl() {
        return subtitlesUrl;
    }

    public void setSubtitlesUrl(String subtitlesUrl) {
        this.subtitlesUrl = subtitlesUrl;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRecommended() {
        return recommended;
    }

    public void setRecommended(int recommended) {
        this.recommended = recommended;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getBroadcastPace() {
        return broadcastPace;
    }

    public void setBroadcastPace(int broadcastPace) {
        this.broadcastPace = broadcastPace;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(int isFavorites) {
        this.isFavorites = isFavorites;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }
}
