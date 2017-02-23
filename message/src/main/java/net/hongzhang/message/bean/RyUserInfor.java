package net.hongzhang.message.bean;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RyUserInfor {
    /**

     * ts_name	String
     ts_id	String
     schoolName	String
     className	String
     list	List<AccountInfo>
     img	String
     tsType	Integer
     ryId	String

     *
     */
    private String tsName;
    /**
     *角色ID
     */
    private String ts_id;
    /**
     *学校名
     */
    private String schoolName;
    /**
     *班级名
     */
    private String className;
    /**
     *家长信息
     */
    private List<AccountInfo> account_info;
    /**
     *图片地址URL
     */
    private String img;
    /**
     *t1=学生，2=老师
     */
    private int tsType;
    /**
     *融云通讯ID
     */
    private String ryId;

    public void setTsType(int tsType) {
        this.tsType = tsType;
    }

    public List<AccountInfo> getAccount_info() {
        return account_info;
    }

    public void setAccount_info(List<AccountInfo> account_info) {
        this.account_info = account_info;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getTsType() {
        return tsType;
    }

    public void setTsType(Integer tsType) {
        this.tsType = tsType;
    }

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
    }

   public class AccountInfo{
       /**
        * 手机号
        */
       private String phone;
       /**
        * 	账户名
        */
       private String name;

       public String getPhone() {
           return phone;
       }

       public void setPhone(String phone) {
           this.phone = phone;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }
   }
}
