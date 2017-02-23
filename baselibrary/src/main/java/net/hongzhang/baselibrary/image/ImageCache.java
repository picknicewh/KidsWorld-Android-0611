package net.hongzhang.baselibrary.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.hongzhang.baselibrary.R;

/**
 * ================================================
 * 作    者： ZLL
 * 时    间： 2016/7/8
 * 描    述：图片工具类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class ImageCache {
    private static DisplayImageOptions options;
    private static ImageLoader imageLoader;
    /**
     *  实例化设置获取图片的时候一些参数
     * @return
     */
    private static ImageLoader ImageCache() {
        if(imageLoader==null){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_img_error) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.ic_img_error) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.ic_img_error) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 启用EXIF和JPEG图像格式
                    .imageScaleType(ImageScaleType.EXACTLY)
                    // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                    .bitmapConfig(Bitmap.Config.RGB_565).build(); // 构建完成
            imageLoader =ImageLoader.getInstance();

        }
        return imageLoader;
    }

    /**
     *  显示普通图片
     * @param imageUri 图片地址
     * @param imageView
     */
    public static void imageLoader( String imageUri,  ImageView imageView){
        ImageCache().displayImage(getNewUrl(imageUri), imageView, options);

    }

    /**
     *  显示普通图片
     * @param imageUri 图片地址
     * @param imageView
     */
    public static void imageLoaderPlay( String imageUri,  ImageView imageView){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_album) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_album) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_album) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .imageScaleType(ImageScaleType.EXACTLY)
                // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                .bitmapConfig(Bitmap.Config.RGB_565).build(); // 构建完成
        imageView.setTag(imageUri);
        ImageCache().displayImage(imageUri, imageView, options);
    }
    /**
     *  获取图片下载进度监听
     * @param uri
     * @param imageView
     * @param listener 图片下载进度监听
     */
    public static void imageLoader(String uri, ImageView imageView, SimpleImageLoadingListener listener){
        ImageCache().displayImage(getNewUrl(uri), imageView, listener);
    }
    /**
     *  获取一个uri的bitmap对象
     * @param uri
     * @return
     */
    public static Bitmap getBitmap(String uri){
        return ImageCache().loadImageSync(getNewUrl(uri));
    }
    private static String  getNewUrl(String url){
        if (url!=null){
            if (url.contains("\\")){
                url = url.replace("\\","/");
            }
        }
        return url;
    }
}
