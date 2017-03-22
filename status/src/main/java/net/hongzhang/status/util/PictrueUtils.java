package net.hongzhang.status.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.RecyclerImageView;

import java.util.ArrayList;
import java.util.List;

import main.picturesee.util.ImagePagerActivity;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/13
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PictrueUtils implements View.OnClickListener {
    private Context context;
    private List<String> imageUrl;
    private int MAXIMAGESIZE = 0;
    private int IMAGESIZE = 0;
    private int SPACE = 0;
    private final int Base = 90;
    private final int LineBase = 2;

    public void setPictrueLoad(Context context, List<String> imageUrl, final RelativeLayout rlParams) {
        this.context = context;
        this.imageUrl = imageUrl;
        SPACE = G.dp2px(context, LineBase);
        MAXIMAGESIZE = G.dp2px(context, Base * 2 + LineBase);
        IMAGESIZE = G.dp2px(context, Base);
        if (rlParams != null) {
            rlParams.removeAllViews();
        }
        if (imageUrl.size() == 1) {
            //单张图片
            final RecyclerImageView imageView = new RecyclerImageView(context);
            RelativeLayout.LayoutParams pl;
            ImageCache.imageLoader(imageUrl.get(0), imageView);
            imageView.setIndex(0);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setOnClickListener(this);
            Glide.with(context).load(imageUrl.get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    RelativeLayout.LayoutParams pl = getLayoutParams(resource, imageView);
                    if (pl != null) {
                        rlParams.addView(imageView, pl);
                    }
                }
            });
        } else if (imageUrl.size() == 3 || imageUrl.size() == 6) {
            //3张或者6张图片
            for (int i = 0; i < imageUrl.size(); i++) {
                RecyclerImageView imageView = getImageView(i);
                RelativeLayout.LayoutParams pl;
                if (i == 0) {
                    pl = new RelativeLayout.LayoutParams(MAXIMAGESIZE, MAXIMAGESIZE);
                    pl.setMargins(SPACE, SPACE, SPACE, SPACE); //第一张摆放位置
                } else if (i <= 2) {
                    pl = new RelativeLayout.LayoutParams(IMAGESIZE, IMAGESIZE);
                    if (i == 2) {  //第三张
                        pl.addRule(RelativeLayout.BELOW, i + 100 - 1);
                        pl.addRule(RelativeLayout.ALIGN_LEFT, i + 100 - 1);
                        pl.addRule(RelativeLayout.ALIGN_BOTTOM, i + 100 - 2);
                        pl.setMargins(0, SPACE, 0, 0);
                    } else if (i == 1) {//第二张
                        pl.addRule(RelativeLayout.RIGHT_OF, i + 100 - 1);
                        pl.addRule(RelativeLayout.ALIGN_TOP, i + 100 - 1);
                    }
                } else {
                    pl = new RelativeLayout.LayoutParams(IMAGESIZE, IMAGESIZE);
                    if (i == 3) {//第四张
                        pl.addRule(RelativeLayout.BELOW, i + 100 - 3);
                        pl.addRule(RelativeLayout.ALIGN_LEFT, i + 100 - 3);
                        pl.setMargins(0, 0, 0, SPACE);
                    } else {
                        pl.addRule(RelativeLayout.RIGHT_OF, i + 100 - 1);
                        pl.addRule(RelativeLayout.ALIGN_TOP, i + 100 - 1);
                        pl.setMargins(SPACE, 0, 0, 0);
                    }
                }
                rlParams.addView(imageView, pl);
            }
        } else if (imageUrl.size() == 4) {
            //4张图片
            for (int i = 0; i < imageUrl.size(); i++) {
                RecyclerImageView imageView = getImageView(i);
                RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(IMAGESIZE, IMAGESIZE);
                if (i % 2 == 0) {
                    pl.setMargins(SPACE, SPACE, SPACE, SPACE);
                    pl.addRule(RelativeLayout.BELOW, i + 100 - 1);
                } else {
                    pl.setMargins(0, 0, SPACE, 0);
                    pl.addRule(RelativeLayout.RIGHT_OF, i + 100 - 1);
                    pl.addRule(RelativeLayout.ALIGN_TOP, i + 100 - 1);
                }
                rlParams.addView(imageView, pl);
            }
        } else {
            //去除上面的情况
            for (int i = 0; i < imageUrl.size(); i++) {
                RecyclerImageView imageView = getImageView(i);
                RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(IMAGESIZE, IMAGESIZE);
                if (i % 3 == 0) {
                    pl.setMargins(SPACE, SPACE, SPACE, SPACE);
                    pl.addRule(RelativeLayout.BELOW, i + 100 - 1);
                } else {
                    pl.setMargins(0, 0, SPACE, 0);
                    pl.addRule(RelativeLayout.RIGHT_OF, i + 100 - 1);
                    pl.addRule(RelativeLayout.ALIGN_TOP, i + 100 - 1);
                }
                rlParams.addView(imageView, pl);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, getimagepath(imageUrl));
        int index;
        try {
            index = ((RecyclerImageView) view).getIndex();
        } catch (Exception e) {
            index = 0;
        }
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
        intent.putExtra("source", "net");
        context.startActivity(intent);
    }

    /**
     * 实例化 RecyclerImageView
     *
     * @param i 当前ImageView的位置
     * @return RecyclerImageView
     */
    private RecyclerImageView getImageView(int i) {
        RecyclerImageView imageView = new RecyclerImageView(context);
        ImageCache.imageLoader(imageUrl.get(i), imageView);
        imageView.setId(i + 100);
        imageView.setOnClickListener(this);
        imageView.setIndex(i);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    /**
     * 把缩略图转换成为原图
     */
    private ArrayList<String> getimagepath(List<String> itemList) {
        ArrayList<String> imagepath = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
//            String pathurl = itemList.get(i);
//            pathurl = pathurl.replace("/s", "");
//            imagepath.add(pathurl);
            String pathurl = itemList.get(i);
            int index = itemList.get(i).lastIndexOf("/s");
            String path;
            if (index != -1)
                path = pathurl.substring(0, index) + pathurl.substring(index + 2, pathurl.length());
            else
                path = pathurl;
            imagepath.add(path);
        }
        return imagepath;
    }

//    /**
//     * 设置图片布局参数,根据不同长宽的图片不同的布局
//     *
//     * @param url      图片的url
//     * @param iv_image 图片控件
//     */
//    public static RelativeLayout.LayoutParams setParam(Context context, String url, ImageView iv_image) {
//        Bitmap bitmap = ImageCache.getBitmap(url);
//        if (bitmap != null) {
//            int imageWidth = bitmap.getWidth();
//            int imageHeight = bitmap.getHeight();
//            RelativeLayout.LayoutParams lp;
//            int viewWidth = G.dp2px(context, 180);
//            int viewHeight = G.dp2px(context, 180);
//            if (imageHeight > imageWidth) {
//                lp = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
//                iv_image.setScaleType(ImageView.ScaleType.FIT_START);
//            } else if (imageHeight == imageWidth) {
//                lp = new RelativeLayout.LayoutParams(viewWidth, viewWidth);
//                iv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            } else {
//                lp = new RelativeLayout.LayoutParams(viewWidth, viewHeight * 2 / 3);
//                iv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            }
//            return lp;
//        }
//        return null;
//    }

    private RelativeLayout.LayoutParams getLayoutParams(Bitmap bitmap, ImageView imageView) {
        if (bitmap != null) {
            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();
            RelativeLayout.LayoutParams lp;
            int viewWidth = G.dp2px(context, 180);
            int viewHeight = G.dp2px(context, 180);
            if (imageHeight > imageWidth) {
                lp = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
                imageView.setScaleType(ImageView.ScaleType.FIT_START);
            } else if (imageHeight == imageWidth) {
                lp = new RelativeLayout.LayoutParams(viewWidth, viewWidth);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                lp = new RelativeLayout.LayoutParams(viewWidth, viewHeight * 2 / 3);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            return lp;
        }
        return null;
    }
}
