package main.picturesee.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import main.picturesee.R;
import main.picturesee.photoview.PhotoViewAttacher;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment implements View.OnLongClickListener {
    /**
     * 图片链接地址
     */
    private String mImageUrl;
    /**
     * 显示图片
     */
    private ImageView mImageView;
    /**
     * 加载图片进度条
     */
    private ProgressBar progressBar;

    private PhotoViewAttacher mAttacher;
    /**
     * 来自本地图片或者是网络图片，处理图片的方式不同
     */
    private String source;

    public static ImageDetailFragment newInstance(String imageUrl, String source) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putString("source", source);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        source = getArguments() != null ? getArguments().getString("source") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        G.initDisplaySize(getActivity());
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnLongClickListener(this);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (source.equals("net") || source.equals("message")) {
            G.log("-------------------------"+mImageUrl);
          //  GlideUtils.loadImageView1(getActivity(),mImageUrl,mImageView);
            Glide.with(getActivity()).load(mImageUrl).asBitmap().thumbnail(0.1f).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mImageView.setImageBitmap(resource);
                    progressBar.setVisibility(View.GONE);
                    if (resource==null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Bitmap bitmap = ImageCache.getBitmap(mImageUrl);
                                if (bitmap!=null){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mImageView.setImageBitmap(bitmap);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
            });


        } else if (source.equals("local")) {
            progressBar.setVisibility(View.GONE);
            Bitmap bitmap = getLoacalBitmap(mImageUrl);
            mImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onLongClick(final View view) {
        if (!source.equals("local")) {
            Glide.with(getActivity()).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (bitmap != null) {
                        SavePicturePopWindow picturePopWindow = new SavePicturePopWindow(getActivity(), bitmap);
                        picturePopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    } else {
                        Toast.makeText(getActivity(), "图片有误！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return true;
    }
}
