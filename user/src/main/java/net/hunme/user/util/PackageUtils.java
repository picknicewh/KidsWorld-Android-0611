package net.hunme.user.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {
	/**
	 * 获取软件版本号，对应AndroidManifest.xml下android:versionCode
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo("net.hunme.kidsworld", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版本号，对应AndroidManifest.xml下android:versionName
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo("net.hunme.kidsworld", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

}
