package net.hongzhang.discovery.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * 作者：Restring
 * 时间：2017/3/15
 * 描述：
 * 版本：
 */

public class BannerImageLoaderUtil extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
