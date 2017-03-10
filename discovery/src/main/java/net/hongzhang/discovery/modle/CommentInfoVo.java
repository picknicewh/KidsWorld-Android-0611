package net.hongzhang.discovery.modle;

/**
 * 作者： wanghua
 * 时间： 2017/3/9
 * 名称：评论类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CommentInfoVo {

    /**
     * content : 这个视频真好看
     * date : 1天前
     * flag : null
     * create_time : 1488855784000
     * ts_id : 879d4ab35402431d9a9eff32ce6efa97
     * tsName : 龙龙
     * tsType : 1
     * status : null
     * comment_id : f9d01523d0f841c5a38f660cebabd01c
     * tsImgurl : http://eduslb.openhunme.com/KidsWorldAdmin/headimg/head.png
     * infostatus : 0
     * createDate : 2017-03-07 11:03:04
     * resource_id : b2ff68ebca7c453ea73a73c2caf4809f
     */

    private String content;
    private String date;
    private int flag;
    private long create_time;
    private String ts_id;
    private String tsName;
    private int tsType;
    private int status;
    private String comment_id;
    private String tsImgurl;
    private int infostatus;
    private String createDate;
    private String resource_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getTsImgurl() {
        return tsImgurl;
    }

    public void setTsImgurl(String tsImgurl) {
        this.tsImgurl = tsImgurl;
    }

    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }
}
