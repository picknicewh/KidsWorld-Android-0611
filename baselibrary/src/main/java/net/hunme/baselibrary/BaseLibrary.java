package net.hunme.baselibrary;

import android.app.Application;
import android.content.Context;

import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

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
public class BaseLibrary {

    public static void initializer(Application application){
        OkHttpUtils.init(application);
        initImageLoader(application);
    }

    //图片缓存
    private static void initImageLoader(Context context) {
        // 缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ChatFile");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                // int i = 50 * 1024 * 1024;
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // 连接超时5s
                // 下载时间30s
                .writeDebugLogs() // Remove for release app
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
}
