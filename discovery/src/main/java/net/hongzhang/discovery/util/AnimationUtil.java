package net.hongzhang.discovery.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 作者： Administrator
 * 时间： 2016/12/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class AnimationUtil {
    private  ObjectAnimator mRotateAnimator;
    private  long mLastAnimationValue=0;
     public  AnimationUtil(View view){
         init(view);
     }
    private  void init(View view) {
        mRotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        mRotateAnimator.setDuration(14400);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }
    public void startRotateAnimation() {
        mRotateAnimator.cancel();
        mRotateAnimator.start();
    }
    public void cancelRotateAnimation() {
        mLastAnimationValue = 0;
        mRotateAnimator.cancel();
    }

    public long getmLastAnimationValue() {
        return mLastAnimationValue;
    }

    public void pauseRotateAnimation() {
        mLastAnimationValue = mRotateAnimator.getCurrentPlayTime();
        mRotateAnimator.cancel();
    }

    public void resumeRotateAnimation() {
        mRotateAnimator.start();
        mRotateAnimator.setCurrentPlayTime(mLastAnimationValue);
    }
}
