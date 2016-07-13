package net.hunme.kidsworld.util;

import android.app.Application;

import net.hunme.baselibrary.BaseLibrary;

/**
 * Created by DELL on 2016/1/19.
 */
public class HunmeApplication extends Application {
    private static HunmeApplication instance;
    public static HunmeApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseLibrary.initializer(this);
    }

}
