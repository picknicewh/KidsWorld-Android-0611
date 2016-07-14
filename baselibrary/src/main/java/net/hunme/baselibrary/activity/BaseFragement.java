package net.hunme.baselibrary.activity;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * 作者： Administrator
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class BaseFragement extends android.support.v4.app.Fragment {
    public <T extends View> T $(View layoutView, @IdRes int resId){
        return (T)layoutView.findViewById(resId);
    }
}
