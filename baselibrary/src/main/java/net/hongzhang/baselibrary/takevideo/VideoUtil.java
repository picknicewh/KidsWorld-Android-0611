package net.hongzhang.baselibrary.takevideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;

import net.hongzhang.baselibrary.util.FileUtils;
import net.hongzhang.baselibrary.util.G;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * 作者： wanghua
 * 时间： 2017/4/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class VideoUtil {
    /**
     * 是不是视频，且是视频的第一帧
     *
     * @param videoPath 视频路径
     * @param context   上下文本
     */
    public static String getVideoFirstFrame(String videoPath, Context context) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        byte[] data = G.Bitmap2Bytes(bitmap);
        String path = FileUtils.getUploadVideoFile(context);
        FileUtils.savePhoto(path, data, false);
        media.release();
        return path;
    }

    public static Bitmap getNetVideoFirstFrame(String path) {
        Bitmap bitmap = null;
        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
        try {
            fmmr.setDataSource(path);
            bitmap = fmmr.getFrameAtTime();
            if (bitmap != null) {
                Bitmap b2 = fmmr
                        .getFrameAtTime(
                                4000000,
                                FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                if (b2 != null) {
                    bitmap = b2;
                }
                if (bitmap.getWidth() > 640) {// 如果图片宽度规格超过640px,则进行压缩
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                            640, 480,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                }

            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } finally {
            fmmr.release();
        }
        return bitmap;
    }
    public static String  getFirstFrame(String videoPath){
        int index = videoPath.lastIndexOf(".");
        if (index==-1){
            return "";
        }
        String imagePath = videoPath.substring(0,index)+".png";
        return imagePath;
    }
}
