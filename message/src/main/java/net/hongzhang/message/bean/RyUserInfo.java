package net.hongzhang.message.bean;

/**
 * 作者： wanghua
 * 时间： 2017/6/12
 * 名称：角色信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RyUserInfo {
    /**
     * 角色ID
     */
    private String tsId;
    /**
     * 角色名称
     */
    private String tsName;
    /**
     * 头像
     */
    private String imgUrl;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 真实姓名(家长或老师)
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 融云通讯ID
     */
    private String ryId;
    /**
     * 角色类别
     */
    private int tsType;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
    }

    public int getTsType() {
        return tsType;
    }

    public void setTsType(int tsType) {
        this.tsType = tsType;
    }
}
