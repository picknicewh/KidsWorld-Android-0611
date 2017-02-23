package net.hongzhang.school.bean;

/**
 * 作者： Administrator
 * 时间： 2016/7/25
 * 名称：签到人员名单
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TsJson {
    /**
     * 角色名
     */
    private String ts_name;
    /**
     * 角色ID
     */
    private String ts_id;
    /**
     * 班级名
     */
    private String schoolName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 账户名
     */
    private String accountName;
    /**
     * 图片地址URL
     */
    private String img;
    /**
     * 1=学生，2=老师
     */
    private Integer tsType;
    /**
    * 融云通讯ID
    */
    private String ryId;

    public String getTs_name() {
        return ts_name;
    }

    public void setTs_name(String ts_name) {
        this.ts_name = ts_name;
    }

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
    }

    public Integer getTsType() {
        return tsType;
    }

    public void setTsType(Integer tsType) {
        this.tsType = tsType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
    }
}
