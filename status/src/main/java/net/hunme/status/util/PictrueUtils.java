package net.hunme.status.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.util.G;

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
    private int MAXIMAGESIZE=0;
    private int IMAGESIZE=0;

    public void setPictrueLoad(Context context, List<String> imageUrl, RelativeLayout rlParams){
        this.context=context;
        this.imageUrl=imageUrl;
        MAXIMAGESIZE=G.dp2px(context,180);
        IMAGESIZE=G.dp2px(context,90);
        if (rlParams!=null){
            rlParams.removeAllViews();
        }
        if(imageUrl.size()==1){
            //单张图片
            ImageView imageView=new ImageView(context);
            ImageCache.imageLoader(imageUrl.get(0),imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            RelativeLayout.LayoutParams pl=new RelativeLayout.LayoutParams(MAXIMAGESIZE,MAXIMAGESIZE);
            imageView.setTag(0);
            imageView.setOnClickListener(this);
            rlParams.addView(imageView,pl);
        }else if(imageUrl.size()==3||imageUrl.size()==6){

            //3张或者6张图片
            for (int i=0;i<imageUrl.size();i++){
                ImageView imageView=new ImageView(context);
                imageView.setId(i+100);
                imageView.setTag(i);
                ImageCache.imageLoader(imageUrl.get(i),imageView);
                imageView.setOnClickListener(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                RelativeLayout.LayoutParams pl;
                if(i==0){
                    pl=new RelativeLayout.LayoutParams(MAXIMAGESIZE,MAXIMAGESIZE);
                    pl.setMargins(5,5,5,5);
                }else if(i<=2){
                    pl=new RelativeLayout.LayoutParams(IMAGESIZE,IMAGESIZE);
                    if(i==2){
                        pl.addRule(RelativeLayout.BELOW,i+100-1);
                        pl.addRule(RelativeLayout.ALIGN_LEFT,i+100-1);
                        pl.addRule(RelativeLayout.ALIGN_BOTTOM,i+100-2);
                        pl.setMargins(0,5,0,0);
                    }else if(i==1){
                        pl.addRule(RelativeLayout.RIGHT_OF,i+100-1);
                        pl.addRule(RelativeLayout.ALIGN_TOP,i+100-1);
                    }
                }else {
                    pl=new RelativeLayout.LayoutParams(IMAGESIZE,IMAGESIZE);
                    if(i==3){
                        pl.addRule(RelativeLayout.BELOW,i+100-3);
                        pl.addRule(RelativeLayout.ALIGN_LEFT,i+100-3);
                        pl.setMargins(0,0,0,5);
                    }else{
                        pl.setMargins(5,0,2,0);
                        pl.addRule(RelativeLayout.RIGHT_OF,i+100-1);
                        pl.addRule(RelativeLayout.ALIGN_TOP,i+100-1);
                    }
                }
                rlParams.addView(imageView,pl);
            }
        }else if(imageUrl.size()==4){

            //4张图片
            for (int i=0;i<imageUrl.size();i++){
                ImageView imageView=new ImageView(context);
                ImageCache.imageLoader(imageUrl.get(i),imageView);
                imageView.setId(i+100);
                imageView.setOnClickListener(this);
                imageView.setTag(i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                RelativeLayout.LayoutParams pl=new RelativeLayout.LayoutParams(IMAGESIZE,IMAGESIZE);
                if(i%2==0){
                    pl.setMargins(5,5,5,5);
                    pl.addRule(RelativeLayout.BELOW,i+100-1);
                }else{
                    pl.setMargins(0,0,5,0);
                    pl.addRule(RelativeLayout.RIGHT_OF,i+100-1);
                    pl.addRule(RelativeLayout.ALIGN_TOP,i+100-1);
                }
                rlParams.addView(imageView,pl);
            }
        }else {

            //去除上面的情况
            for (int i=0;i<imageUrl.size();i++){
                ImageView imageView=new ImageView(context);
                ImageCache.imageLoader(imageUrl.get(i),imageView);
                imageView.setId(i+100);
                imageView.setOnClickListener(this);
                imageView.setTag(i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                RelativeLayout.LayoutParams pl=new RelativeLayout.LayoutParams(IMAGESIZE,IMAGESIZE);
                if(i%3==0){
                    pl.setMargins(5,5,5,5);
                    pl.addRule(RelativeLayout.BELOW,i+100-1);
                }else{
                    pl.setMargins(0,0,5,0);
                    pl.addRule(RelativeLayout.RIGHT_OF,i+100-1);
                    pl.addRule(RelativeLayout.ALIGN_TOP,i+100-1);
                }

                rlParams.addView(imageView,pl);
            }

        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,getimagepath(imageUrl));
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, (int)view.getTag());
        intent.putExtra("source","net");
        context.startActivity(intent);
    }
    /**
     * 把缩略图转换成为原图
     */
    private ArrayList<String> getimagepath(List<String> itemList){
        ArrayList<String> imagepath = new ArrayList<>();
        for (int i = 0 ; i<itemList.size();i++){
            String pathurl = itemList.get(i);
            int index = itemList.get(i).lastIndexOf("/");
            String path = pathurl.substring(0,index-2)+pathurl.substring(index,pathurl.length());
            imagepath.add(path);
        }
        return imagepath;

    }
}
