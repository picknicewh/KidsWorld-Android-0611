package net.hongzhang.user.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：图片缓存类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class BitmapCache {
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩图片到指定位置(默认JPG格式)
     *
     * @param bitmap       需要压缩的图片
     * @param compressPath 生成文件路径(例如: /storage/imageCache/1.jpg)
     * @param quality      图片质量，0~100
     * @return if true,保存成功
     */
    public static boolean compressBiamp(Bitmap bitmap, String compressPath, int quality) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(compressPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);// (0-100)压缩文件
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private static List<File> files;

    public static List<File> getFileList(List<String>pathList){
        String path=Environment.getExternalStorageDirectory().toString() + "/ChatFile/";
        files=new ArrayList<>();
        for (String s:pathList){
            String compressPath = path+ new Date().getTime()+".jpg";
            Bitmap bitmap=BitmapFactory.decodeFile(s);
           boolean isCache= compressBiamp(bitmap,compressPath,70);
            if(isCache){
                files.add(new File(compressPath));
            }
        }
     return  files;
    }
}
