package net.hunme.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.hunme.baselibrary.R;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

/**
 * @Title: ${file_name}
 * @Description: 带视觉差的滑动返回
 * Created by bushijie33@gmail.com on 2015/7/5.
 */
public class SwipeBackFragment extends Fragment implements SlidingPaneLayout.PanelSlideListener {

    private final static String TAG = SwipeBackFragment.class.getSimpleName();
    private final static String WINDOWBITMAP = "screenshots.jpg";
    private File mFileTemp;
    private SlidingPaneLayout slidingPaneLayout;
    private FrameLayout frameLayout;
    private ImageView behindImageView;
    private ImageView shadowImageView;
    private int defaultTranslationX = 100;
    private int shadowWidth = 20;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //通过反射来改变SlidingPanelayout的值
        try {
            slidingPaneLayout = new SlidingPaneLayout(getActivity());
            Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            f_overHang.setAccessible(true);
            f_overHang.set(slidingPaneLayout, 0);
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(R.color.transparent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        mFileTemp = new File(getActivity().getCacheDir(), WINDOWBITMAP);
        defaultTranslationX = dip2px(defaultTranslationX);
        shadowWidth = dip2px(shadowWidth);
        //behindframeLayout
        FrameLayout behindframeLayout = new FrameLayout(getActivity());
        behindImageView = new ImageView(getActivity());
        behindImageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        behindframeLayout.addView(behindImageView, 0);

        //containerLayout
        LinearLayout containerLayout = new LinearLayout(getActivity());
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        containerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        containerLayout.setLayoutParams(new ViewGroup.LayoutParams(getActivity().getWindowManager().getDefaultDisplay().getWidth() + shadowWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        //you view container
        frameLayout = new FrameLayout(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //add shadow
        shadowImageView = new ImageView(getActivity());
        shadowImageView.setBackgroundResource(R.mipmap.shadow);
        shadowImageView.setLayoutParams(new LinearLayout.LayoutParams(shadowWidth, LinearLayout.LayoutParams.MATCH_PARENT));
        containerLayout.addView(shadowImageView);
        containerLayout.addView(frameLayout);
        containerLayout.setTranslationX(-shadowWidth);
        //添加两个view
        slidingPaneLayout.addView(behindframeLayout, 0);
        slidingPaneLayout.addView(containerLayout, 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frameLayout.removeAllViews();
        frameLayout.addView(slidingPaneLayout, container.getLayoutParams());
        return slidingPaneLayout;

    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        Log.e(TAG, "onPanelSlide ：" + v);
        //duang duang duang 你可以在这里加入很多特效
        behindImageView.setTranslationX(v * defaultTranslationX - defaultTranslationX);
        if (v<0.8){
            v = 1;
        }else {
            v = 1.5f-v;
        }
        shadowImageView.setAlpha(v);
    }

    /**
     * 取得视觉差背景图
     *
     * @return
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(mFileTemp.getAbsolutePath());
    }

    /**
     * 启动视觉差返回Activity
     *
     * @param activity
     * @param intent
     */
    public void startParallaxSwipeBackActivty(Activity activity, Intent intent) {
        startParallaxSwipeBackActivty(activity, intent, false);
    }

    /**
     * startParallaxSwipeBackActivty
     *
     * @param activity
     * @param intent
     * @param isFullScreen
     */
    public void startParallaxSwipeBackActivty(Activity activity, Intent intent, boolean isFullScreen) {
        screenshots(activity, isFullScreen);
        startActivity(intent);
       // this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * this screeshots form
     *
     * @param activity
     * @param isFullScreen
     */
    public void screenshots(Activity activity, boolean isFullScreen) {
        try {
            //View是你需要截图的View
            View decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            Bitmap b1 = decorView.getDrawingCache();
            // 获取状态栏高度 /
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            Log.e(TAG, "statusBarHeight:" + statusBarHeight);
            // 获取屏幕长和高 Get screen width and height
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            int height = activity.getWindowManager().getDefaultDisplay().getHeight();
            // 去掉标题栏 Remove the statusBar Height
            Bitmap bitmap;
            if (isFullScreen) {
                bitmap = Bitmap.createBitmap(b1, 0, 0, width, height);
            } else {
                bitmap = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
            }
            decorView.destroyDrawingCache();
            FileOutputStream out = new FileOutputStream(mFileTemp);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
