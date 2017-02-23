package net.hongzhang.message.bean;

/**
 * 作者： wh
 * 时间： 2016/9/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupMemberVo {
    /**
     *角色ID
     */
    private  String ts_id;
    /**
     *角色名称
     */
    private  String ts_name;
    /**
     *头像URL
     */
    private  String imgUrl;
    /**

     *融云通讯ID
     */
    private  String ry_id;
    /**
     *0=老师，1=学生，2=班主任
     */
    private  int role_type;

    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
    }

    public String getTs_name() {
        return ts_name;
    }

    public void setTs_name(String ts_name) {
        this.ts_name = ts_name;
    }

    public String getRy_id() {
        return ry_id;
    }

    public void setRy_id(String ry_id) {
        this.ry_id = ry_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getRole_type() {
        return role_type;
    }

    public void setRole_type(int role_type) {
        this.role_type = role_type;
    }
}
