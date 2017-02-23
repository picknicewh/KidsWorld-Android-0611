package net.hongzhang.discovery.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.MotionEvent;
import android.view.WindowManager;

import net.hongzhang.baselibrary.util.G;

/**
 * Created by wanghua on 2017/2/21.
 */
public class MyGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
    private AudioManager mAudioManager;
    /**
     * 当前音量
     */
    private int mVolume;
    /**
     * 最大音量
     */
    private int mMaxVolume;
    /**
     * 屏幕宽度
     */
    private int srceenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * 当前页
     */
    private Activity activity;
    /**
     * 当前亮度
     */
    private float mBrightness;

    public MyGestureListener(Activity activity) {
        this.activity = activity;
        G.initDisplaySize(activity);
        mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        srceenWidth = G.size.W;
        screenHeight = G.size.H;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float olderX = e1.getX();
        float olderY = e1.getY();
        float y = e2.getRawY();
        if (olderX > 0 && olderX <= srceenWidth / 3)
            onBrightnessSlide((olderY - y) / screenHeight);
        if (olderX >= srceenWidth / 3 * 2 && olderX <= srceenWidth)
            onVolumeSlide((olderY - y) / screenHeight);
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = activity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        activity.getWindow().setAttributes(lpa);

    }
    /** 手势结束 */
    public void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
    }

}
