package net.hunme.baselibrary.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ================================================
 * 作    者： ZLL
 * 时    间： 2016/7/8
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class ImageCache {
    private static DisplayImageOptions options;
    private static ImageLoader imageLoader;
    private static ImageLoader ImageCache() {
        if(imageLoader==null){
            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.ic_ad_default) // 设置图片下载期间显示的图片
//                    .showImageForEmptyUri(R.drawable.ic_ad_default) // 设置图片Uri为空或是错误的时候显示的图片
//                    .showImageOnFail(R.drawable.ic_ad_default) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                    // .bitmapConfig(Bitmap.Config.RGB_565);//
                    .bitmapConfig(Bitmap.Config.RGB_565).build(); // 构建完成
            imageLoader =ImageLoader.getInstance();
        }
        return imageLoader;
    }

    public static void imageLoader(String uri, ImageView imageView){
        ImageCache().displayImage(uri, imageView, options);
    }
}
