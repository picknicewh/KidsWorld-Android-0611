package net.hongzhang.baselibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.hongzhang.baselibrary.contract.GetContractData;
import net.hongzhang.baselibrary.contract.GetGroupData;
import net.hongzhang.baselibrary.contract.InitAllContractData;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

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
public class BaseLibrary {
    /**
     * 整体activity同时销毁的
     */
    private static List<Activity> activitys = null;
    /**
     * 部分activity同时销毁的
     */
    private static List<Activity> activityPartList= null;
    /**
     * 部分activity同时销毁的
     */
    public static List<Activity> activityLockList= null;
    private static Application instance;
    public static void initializer(Application application){
        OkHttpUtils.init(application);
        initImageLoader(application);
   //     RongPushClient.registerMiPush(application, " 2882303761517505108", "5551750520108");
        ///RongIM.init(application);
        activitys=new ArrayList<>();
        activityLockList = new ArrayList<>();
        instance=application;
    }
    public static Application getInstance() {
        return instance;
    }
    // 添加Activity到容器中
    public static void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);

            }
        }else{
            activitys.add(activity);
        }
    }
    public static  void addLockActivity(Activity activity){
        if (activityLockList!=null){
            activityLockList.add(activity);
        }

    }
    private  static  int count=0;
    public static  void removeLockActivity(){
        if (activityLockList != null && activityLockList.size() > 0) {
            for (Activity activity : activityLockList) {
                activity.finish();
            }
        }
    }
    public static  void removeLocFirstkActivity(){
        if (activityLockList != null && activityLockList.size() > 0) {
            activityLockList.remove(0);
        }
    }
    // 遍历所有Activity并finish
    public static void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    //图片缓存
    private static void initImageLoader(Context context) {
        // 缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ChatFile");//设置内存卡的路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) // 设置当前线程优先级
                .denyCacheImageMultipleSizesInMemory() // 缓存显示不同 大小的同一张图片
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                // int i = 50 * 1024 * 1024;
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // 连接超时5s
                // 下载时间30s
                .writeDebugLogs() // Remove for release app
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    /**
     * 建立与融云服务器的连接
     * @param token 连接融云的参数
     * @param  username 用户名
     * @param  portrait 用户头像地址
     */
    public static void connect(String token, final Activity activity, final String username, final String portrait) {
        initContract(activity);
        if (activity.getApplicationInfo().packageName.equals(BaseLibrary.getCurProcessName(activity))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                  Log.i("sss","==========onTokenIncorrect============");
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.i("ssss","==========onSuccess============");
                    G.log("融云连接成功----");
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, username, Uri.parse(portrait)));
                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                    }
                  // initContract(activity);
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("ssss","==========errorCode============"+errorCode.getMessage());
                    G.log("融云连接失败----");}
            });
        }
    }
    /**
     * 初始化联系人
     * @param context
     */
    public  static void initContract( Context context){
        UserMessage userMessage= (UserMessage.getInstance(context));
        GetContractData getContractData = new GetContractData(context);
        getContractData.getContractList(userMessage.getTsId());
        GetGroupData getGroupData = new GetGroupData(context);
        getGroupData.getGroupList(userMessage.getTsId());
        //初始化所有联系人的数据
        InitAllContractData data = new InitAllContractData(context);
        data.init();
    }
}
