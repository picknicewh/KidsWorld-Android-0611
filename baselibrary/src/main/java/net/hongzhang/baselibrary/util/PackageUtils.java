package net.hongzhang.baselibrary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import net.hongzhang.baselibrary.R;

import java.util.List;

public class PackageUtils {
	/**
	 * 获取当前应用程序的包名
	 * @param context 上下文对象
	 * @return 返回包名
	 */
	public static String getAppProcessName(Context context) {
		//当前应用pid
		int pid = android.os.Process.myPid();
		//任务管理类
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//遍历所有应用
		List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : infos) {
			if (info.pid == pid)//得到当前应用
				return info.processName;//返回包名
		}
		return "";
	}
	/**
	 * 获取程序 图标
	 * @param context
	 * @param packname 应用包名
	 * @return
	 */
	public static Drawable getAppIcon(Context context, String packname){
		try {
			//包管理操作管理类
			PackageManager pm = context.getPackageManager();
			//获取到应用信息
			ApplicationInfo info = pm.getApplicationInfo(packname, 0);
			return info.loadIcon(pm);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取程序的名字
	 * @param context
	 * @param packname
	 * @return
	 */
	public static  String getAppName(Context context,String packname){
		//包管理操作管理类
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(packname, 0);
			return info.loadLabel(pm).toString();
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return packname;
	}
	/*
     * 获取程序的权限
     */
	public String[] getAllPermissions(Context context,String packname){
		try {
			//包管理操作管理类
			PackageManager pm = context.getPackageManager();
			PackageInfo packinfo =    pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
			//获取到所有的权限
			return packinfo.requestedPermissions;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();

		}
		return null;
	}
	/**
	 * 获取程序的签名
	 * @param context
	 * @param packname
	 * @return
	 */
	public static String getAppSignature(Context context,String packname){
		try {
			//包管理操作管理类
			PackageManager pm = context.getPackageManager();
			PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
			//获取当前应用签名
			return packinfo.signatures[0].toCharsString();

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();

		}
		return packname;
	}
	/**
	 * 获取当前展示 的Activity名称
	 * @return
	 */
	private static String getCurrentActivityName(Context context){
		ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}
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
	public static String getApkName(Context context){
		String appName = context.getResources().getString(R.string.app_name);
		return appName;
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
