package net.hunme.user.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：获取图片公共类，初始化数据
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class Bimp {
    /**
     * 选择图片最大值
     */
    public static int max = 0;
//    public static ArrayList<ImageItemVo> tempSelectBitmap = new ArrayList<ImageItemVo>();
    /**
     * 选中图片的路径
     */
    public static List<String> tempSelectBitmap = new ArrayList<String>();
    /**
     * 选中的图片
     */
    public static List<Bitmap> bmp = new ArrayList<Bitmap>();

    /**
     * 根据图片路径返回一个bitmap对象 压缩图片
     * @param path 图片路径
     * @return bitmap对象
     * @throws IOException 图片不存在 路径错误异常
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
//        int hight=options.outHeight>options.outWidth?options.outHeight+1000:options.outWidth+1000;
        while (true) {
            if ((options.outWidth >> i <= 500)
                    && (options.outHeight >> i <= 500)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
}
