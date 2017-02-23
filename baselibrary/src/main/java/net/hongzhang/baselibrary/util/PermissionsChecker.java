package net.hongzhang.baselibrary.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import net.hongzhang.baselibrary.activity.PermissionsActivity;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/22
 * 描    述：检查权限的工具类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PermissionsChecker {
    private final Activity activity;
    private static PermissionsChecker checker;
    //权限请求回调码
    public static final int  REQUEST_CODE = 10100;
    public PermissionsChecker(Activity activity) {
        this.activity = activity;
    }

    public static PermissionsChecker getInstance(Activity activity){
        if(checker==null){
            checker=new PermissionsChecker(activity);
        }
        return checker;
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    //获取权限
    public void getPerMissions(String... permissions){
        if(lacksPermissions(permissions)){
            PermissionsActivity.startActivityForResult(activity, REQUEST_CODE, permissions);
        }
    }
}
