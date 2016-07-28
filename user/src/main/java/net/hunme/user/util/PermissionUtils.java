package net.hunme.user.util;

import android.app.Activity;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.util.PermissionsChecker;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/28
 * 描    述：权限管理类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PermissionUtils {
    public static final int  REQUEST_CODE = 0;
    // 权限检测器
    private static PermissionsChecker mPermissionsChecker;

    public static void getPermission(Activity context,String[] PERMISSIONS ){
        mPermissionsChecker = new PermissionsChecker(context);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(context, REQUEST_CODE, PERMISSIONS);
            return;
        }
    }
}
