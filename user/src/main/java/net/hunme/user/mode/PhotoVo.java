package net.hunme.user.mode;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：我的相册实体类
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PhotoVo {
    private String flickrName;    //相册名称
    private String flickrSize;   // 相册相片张数
    private String imgUrl;       //相册相片张数
    private String flickrId;     //相册ID

    public String getFlickrName() {
        return flickrName;
    }

    public void setFlickrName(String flickrName) {
        this.flickrName = flickrName;
    }

    public String getFlickrSize() {
        return flickrSize;
    }

    public void setFlickrSize(String flickrSize) {
        this.flickrSize = flickrSize;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFlickrId() {
        return flickrId;
    }

    public void setFlickrId(String flickrId) {
        this.flickrId = flickrId;
    }
}
