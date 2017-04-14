package net.hongzhang.baselibrary.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 作者： wanghua
 * 时间： 2017/4/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class UpdateUtil {
    /**
     * 安装APK文件
     * @param  context 上线文本
     * @param  savePath 保存路径
     */
    public static boolean installApk(Context context,String savePath) {
        File apkFile = new File(savePath,getSaveName(context));
        if (!apkFile.exists()) {
            return false;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        return true;
    }
    public static String getSaveName(Context context){
        String apkName = PackageUtils.getApkName(context);
        String version  = MD5Utils.encode(String.valueOf(PackageUtils.getVersionCode(context)));
        return apkName+version+".apk";
    }
}
