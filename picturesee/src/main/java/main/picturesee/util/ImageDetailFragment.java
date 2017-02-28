package main.picturesee.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
            ImageCache.imageLoader(mImageUrl, mImageView, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                @Override
                public void onLoadingFailed(String imageUri, View view, final FailReason failReason) {
                    Glide.with(getActivity()).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource != null) {
                                mImageView.setImageBitmap(resource);
                                Log.i("TGA","=================================xxx======================");
                            } else {
                                String message;
                                switch (failReason.getType()) {
                                    case IO_ERROR:
                                        message = "下载错误";
                                        break;
                                    case DECODING_ERROR:
                                        message = "图片无法显示";
                                        break;
                                    case NETWORK_DENIED:
                                        message = "网络有问题，无法下载";
                                        break;
                                    case OUT_OF_MEMORY:
                                        message = "图片太大无法显示";
                                        break;
                                    case UNKNOWN:
                                        message = "未知的错误";
                                        break;
                                    default:
                                        message = "未知的错误";
                                        break;

                                }
                                if (!G.isEmteny(message) && getActivity() != null) {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    mAttacher.update();
                }
            });
        } else if (source.equals("local")) {
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
