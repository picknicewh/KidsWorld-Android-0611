package net.hongzhang.status.mode;

/**
 * 作者： Administrator
 * 时间： 2016/10/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DynamidRew  {
    /**
     * 评论ID
     */
    private int rew_id	;
    /**
     *评论人
     */
    private int ts_id	;
    /**
     *	内容
     */
    private String rew_content;
    /**
     *动态id
     */
    private String dynamic_id;
    /**
     *评论时间
     */
    private String create_time;
    /**
     *	针对回复人的id（是回复的时候才有）
     */
    private String 	 rew_ts_id;
    /**
     *1评论 2回复
     */
    private String rew_type;
    /**
     *	评论人姓名
     */
    private String tsName;
    /**
     *评论人头像
     */
    private String tsImgurl	;
    /**
     *回复的人姓名
     */
    private String rewTsName;
    /**
     *回复的人头像
     */
    private String rewTsImgurl	;
    /**
     *处理之后的时间(几天前)
     */
    private  String date	 ;
    /**
     *	处理之后的时间(年月日时分秒)
     */
    private  String createDate	 ;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRewTsImgurl() {
        return rewTsImgurl;
    }

    public void setRewTsImgurl(String rewTsImgurl) {
        this.rewTsImgurl = rewTsImgurl;
    }

    public String getRewTsName() {
        return rewTsName;
    }

    public void setRewTsName(String rewTsName) {
        this.rewTsName = rewTsName;
    }

    public String getTsImgurl() {
        return tsImgurl;
    }

    public void setTsImgurl(String tsImgurl) {
        this.tsImgurl = tsImgurl;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getRew_type() {
        return rew_type;
    }

    public void setRew_type(String rew_type) {
        this.rew_type = rew_type;
    }

    public String getRew_ts_id() {
        return rew_ts_id;
    }

    public void setRew_ts_id(String rew_ts_id) {
        this.rew_ts_id = rew_ts_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getRew_content() {
        return rew_content;
    }

    public void setRew_content(String rew_content) {
        this.rew_content = rew_content;
    }

    public int getTs_id() {
        return ts_id;
    }

    public void setTs_id(int ts_id) {
        this.ts_id = ts_id;
    }

    public int getRew_id() {
        return rew_id;
    }

    public void setRew_id(int rew_id) {
        this.rew_id = rew_id;
    }
}
