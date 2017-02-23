package net.hongzhang.baselibrary.mode;

import java.io.Serializable;

/**
 * 作者： wh
 * 时间： 2016/7/25
 * 名称：联系人信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ContractInfoVo implements Serializable{


    /**
     * 保存在本地数据库的自增长id
     */
    private int uid;
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
    /**
     * 是否置顶
     */
    private int isTop;

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
