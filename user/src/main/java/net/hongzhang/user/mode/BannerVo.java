package net.hongzhang.user.mode;

/**
 * 作者： wh
 * 时间： 2016/12/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class BannerVo {
    /**
     * 图片ID
     */
    private String banner_id;
    /**
     *图片地址
     */
    private String banner_url;
    /**
     *图片跳转地址
     */
    private String  banner_jump;
    /**
     *状态1=显示，2=不显示
     */
    private int status;
    /**
     *创建时间
     */
    private String create_time ;
    /**
     * 1=原生，2=内部web页面，3=内嵌web页面，4=外部web页面
     */
    private int  type;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBanner_jump() {
        return banner_jump;
    }

    public void setBanner_jump(String banner_jump) {
        this.banner_jump = banner_jump;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
