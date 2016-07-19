package net.hunme.user.mode;

import android.graphics.Bitmap;

import net.hunme.user.util.Bimp;

import java.io.IOException;
import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：选中图片对象的实体类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class ImageItemVo implements Serializable{
    public String imageId; //图片id
    public String thumbnailPath; //略缩图的路径
    public String imagePath; //图片地址
    private Bitmap bitmap; //图片
    public boolean isSelected = false;  //是否被选中

    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getThumbnailPath() {
        return thumbnailPath;
    }
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public Bitmap getBitmap() {
        if(bitmap == null){
            try {
                bitmap = Bimp.revitionImageSize(imagePath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
