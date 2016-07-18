package net.hunme.user.mode;

import android.graphics.Bitmap;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：我的相册ListView的实体类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PhotoVo {

    private Bitmap photoBitmap;  //图片
    private String photoName;    //相册名字
    private String photoNumber;  //照片数量

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(String photoNumber) {
        this.photoNumber = photoNumber;
    }
}
