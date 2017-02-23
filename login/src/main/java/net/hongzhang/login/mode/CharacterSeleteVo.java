package net.hongzhang.login.mode;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/20
 * 描    述：用户信息实体类
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CharacterSeleteVo implements Serializable{
    private String tsId;   //角色ID
    private String img;    //用户头像URL
    private String name;   //用户名称
    private String schoolName;  //学校名称
    private String className;   //班级名称 当type=0时，此字段为空
    private String type;       //1=学生，0=老师
    private String ryId;      // 融云通讯ID（token）
    private Integer sex;     //性别 1=男 0=女
    private String signature; // 个性签名
   private String  account_id;//账号id

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
