package net.hunme.user.mode;

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
     * 图片地址
     */
    String imgUrl;
    /**
     * 图片跳转地址
     */
    private String   jumpAddress;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpAddress() {
        return jumpAddress;
    }

    public void setJumpAddress(String jumpAddress) {
        this.jumpAddress = jumpAddress;
    }
}
