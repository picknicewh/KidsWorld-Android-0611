package net.hongzhang.baselibrary.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class PackageUtils {
	/**
	 * 获取软件版本号，对应AndroidManifest.xml下android:versionCode
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo("net.hongzhang.bbhow", 0).versionCode;
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
			versionName = context.getPackageManager().getPackageInfo("net.hongzhang.bbhow", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	/**
    * check the app is installed
    */
	private boolean isAppInstalled(Context context,String packagename)
	{
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		}catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			//System.out.println("没有安装");
			return false;
		}else{
			//System.out.println("已经安装");
			return true;
		}
	}
	/**
	 * 打开应用程序
	 * @param  context
	 * @param  packageName
	 * @param  resultCode
	 */
	private void openApplication(Activity context,String packageName,String mainActivity,int resultCode){
		Intent i = new Intent();
		ComponentName cn = new ComponentName(packageName, packageName+"."+mainActivity);
		i.setComponent(cn);
		context.startActivityForResult(i, resultCode);
	}
	/**
	 * 未安装，跳转至market下载该程序
	 * @param context
	 */
	private void gotoRemark(Activity context,String url){
		Uri uri = Uri.parse(url);//id为包名
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}
}
