package net.hunme.baselibrary.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

/**
 * ================================================
 * 作    者： ZLL
 * 时    间： 2016/7/8
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class G {
    /**
     * 调试信息
     */
    public static boolean DEBUG = true;
    /**
     * toast提示
     */
    private static Toast toast;
    /**
     * 尺寸
     */
    public static final class size {
        /**
         * 屏幕宽
         */
        public static int W = 480;
        /**
         * 屏幕高
         */
        public static int H = 800;
    }

    /**
     * 初始化屏幕尺寸
     */
    public static void initDisplaySize(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        G.size.W = mDisplayMetrics.widthPixels;
        G.size.H = mDisplayMetrics.heightPixels;
    }

    /**
     * 提示
     *
     * @param msg 提示信息
     */
    public static void showToast(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, size.H/4);
        toast.show();

    }

    /**
     * 记录调试信息
     *
     * @param msg 调试信息
     */
    public static void log(Object msg) {
        if (DEBUG) {
            Log.i("TAG",String.valueOf(msg));
        }
    }


    /**
     *  根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *  根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmteny(String values){
        if(null==values||"".equals(values)||"null".equals(values)){
            return true;
        }
        return  false;
    }
    /**
     * 判断网络是否连接
     *
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 删除缓存
     */
    public static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }
    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Context context){
        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //WebView 缓存文件
        File appCacheDir = context.getDir("cache", Context.MODE_PRIVATE);
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            context.deleteFile(appCacheDir.getAbsolutePath());
        }
    }

    public static class KisTyep{
        public static  boolean isChooseId=false;  //是否选中身份 用切换用户后刷新数据

        public static boolean isReleaseSuccess=false;  //是否成功发布动态 用户刷新数据

        public static  boolean isUpadteHold=false;   //是否修改用户头像
    }
}
