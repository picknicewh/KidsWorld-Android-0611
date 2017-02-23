package net.hongzhang.user.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： Administrator
 * 时间： 2016/11/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceVo implements Parcelable{

    private int id;
    private String name;
    private String length;
    private int type;
    private String content;
    private String source;
    private String description;
    private int status;
    private String imgUrl;
    private int grade;
    private String resourceUrl;
    private int ordernumber;
    private int isrecommend;
    private String subtitlesurl;
    private int commentcount;
    private String attentionid;
    private String code;
    private int pvcount;
    private String lyrics;
    private int virtualpv;
    private String themeid;
    private String createTime;
    private int pv;
    private String typeId;
    private String uploaduser;
    private String uploaddate;
    private int infostatus;
    private String  praiseid;
    private String filesize;

    public ResourceVo(Parcel parcel){
        id = parcel.readInt();
        name = parcel.readString();
        length = parcel.readString();
        type = parcel.readInt();
        content = parcel.readString();
        source = parcel.readString();
        description = parcel.readString();
        status = parcel.readInt();
        imgUrl = parcel.readString();
        grade = parcel.readInt();
        resourceUrl = parcel.readString();
        ordernumber = parcel.readInt();
        isrecommend = parcel.readInt();
        subtitlesurl = parcel.readString();
        attentionid = parcel.readString();
        code = parcel.readString();
        pvcount = parcel.readInt();
        lyrics = parcel.readString();
        commentcount = parcel.readInt();
        virtualpv = parcel.readInt();
        themeid = parcel.readString();
        createTime = parcel.readString();
        pv = parcel.readInt();
        typeId = parcel.readString();
        uploaduser = parcel.readString();
        uploaddate = parcel.readString();
        infostatus = parcel.readInt();
        praiseid = parcel.readString();
        filesize = parcel.readString();
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
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getSubtitlesurl() {
        return subtitlesurl;
    }

    public void setSubtitlesurl(String subtitlesurl) {
        this.subtitlesurl = subtitlesurl;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public String getAttentionid() {
        return attentionid;
    }

    public void setAttentionid(String attentionid) {
        this.attentionid = attentionid;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPvcount() {
        return pvcount;
    }

    public void setPvcount(int pvcount) {
        this.pvcount = pvcount;
    }

    public String getThemeid() {
        return themeid;
    }

    public void setThemeid(String themeid) {
        this.themeid = themeid;
    }

    public int getVirtualpv() {
        return virtualpv;
    }

    public void setVirtualpv(int virtualpv) {
        this.virtualpv = virtualpv;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUploaduser() {
        return uploaduser;
    }

    public void setUploaduser(String uploaduser) {
        this.uploaduser = uploaduser;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public String getPraiseid() {
        return praiseid;
    }

    public void setPraiseid(String praiseid) {
        this.praiseid = praiseid;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(length);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeString(source);
        dest.writeString(description);
        dest.writeInt(status);
        dest.writeString(imgUrl);
        dest.writeInt(grade);
        dest.writeString(resourceUrl);
        dest.writeInt(ordernumber);
        dest.writeInt(isrecommend);
        dest.writeString(subtitlesurl);
        dest.writeString(attentionid);
        dest.writeString(code);
        dest.writeInt(pvcount);
        dest.writeString(lyrics);
        dest.writeInt(commentcount);
        dest.writeInt(virtualpv);
        dest.writeString(themeid);
        dest.writeString(createTime);
        dest.writeInt(pv);
        dest.writeString(typeId);
        dest.writeString(uploaduser);
        dest.writeString(uploaddate);
        dest.writeInt(infostatus);
        dest.writeString(praiseid);
        dest.writeString(filesize);
    }

}
