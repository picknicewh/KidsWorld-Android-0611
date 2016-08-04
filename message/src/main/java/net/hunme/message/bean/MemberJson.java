package net.hunme.message.bean;

import java.io.Serializable;

/**
 * 作者： Administrator
 * 时间： 2016/7/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MemberJson implements Serializable{
    /**
     * 用户id
     */
    private String tsId;
    /**
     *用户名称
     */
    private String tsName;
    /**
     *头像地址
     */
    private String img;
    /**
     *融云通讯ID
     */
    private String ryId;

    public String getRyId() {
        return ryId;
    }

    public void setRyId(String ryId) {
        this.ryId = ryId;
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

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }


}
