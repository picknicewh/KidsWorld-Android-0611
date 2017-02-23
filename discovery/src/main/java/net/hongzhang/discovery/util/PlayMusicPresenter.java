package net.hongzhang.discovery.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.MainPlayActivity;
import net.hongzhang.discovery.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/11/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PlayMusicPresenter implements PlayMusicContract.Presenter, OkHttpListener {
    /**
     * 播放页面View
     */
    private PlayMusicContract.View view;
    /**
     * 文本
     */
    private Context context;
    /**
     * 当前专辑播放的歌曲位置
     */
    public static int position = 0;
    /**
     * 用户角色id
     */
    private String tsId;
    /**
     * 当前音乐是否播放
     */
    public static boolean isPlaying = false;
    /**
     * 音乐接受服务广播
     */
    private MainPlayActivity.MyMusicReceiver myMusicReceiver;
    /**
     * 资源id
     */
    public static List<ResourceVo> resourceVos;
    /**
     * 通知管理类
     */
    public static NotificationManager manager;
    /**
     * 跳转服务的事件
     */
    private Intent intent;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 专辑id
     */
    private String themeId;
    /**
     * 用户信息类
     */
    private UserMessage userMessage;

    public PlayMusicPresenter(PlayMusicContract.View view, Context context, int position, MainPlayActivity.MyMusicReceiver myMusicReceiver, String themeId, String resourceId) {
        this.context = context;
        this.view = view;
        this.position = position;
        this.resourceId = resourceId;
        this.myMusicReceiver = myMusicReceiver;
        this.themeId = themeId;
        userMessage = UserMessage.getInstance(context);
        tsId = userMessage.getTsId();
        registerReceiver();
      //  getUserPaly();
        getSongList(tsId, themeId);
    }

    /**
     * 保存播放记录列表
     *
     * @param tsId          角色id
     * @param resourceId    资源id
     * @param broadcastPace 当前播放音乐的秒数
     * @param type          播放音乐开始/结束 type = 1 播放开始 type=2播放结束
     */
    @Override
    public void savePlayTheRecord(String tsId, String resourceId, int broadcastPace, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("resourceid", resourceId);
        if (type == 2) {
            map.put("broadcastPace", broadcastPace);
        }
        map.put("type", type);
        Type mType = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SAVEPLAYRECORDING, map, this);
    }

    /**
     * 根据专辑id获取专辑中的资源列表
     *
     * @param tsId    角色id
     * @param themeId 专辑id
     */
    @Override
    public void getSongList(String tsId, String themeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", 999);
        map.put("pageNumber", 1);
        map.put("albumId", themeId);
        map.put("account_id", UserMessage.getInstance(context).getAccount_id());
        Type mType = new TypeToken<Result<List<ResourceVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETTHENELIST, map, this);
        view.showLoadingDialog();
    }

    /**
     * 根据资源id获取资源详情
     *
     * @param tsId       角色id
     * @param resourceId 资源id
     */
    @Override
    public void getResourceDetail(String tsId, String resourceId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("resourceid", resourceId);
        Type mType = new TypeToken<Result<ResourceVo>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETSOURCEDETAIL, map, this);
    }

    /**
     * 根据资源用户的计费id获取是否已经付费
     *
     * @param tsId      角色id
     * @param accountId 计费id
     */
    @Override
    public void getIsUserPay(String tsId, String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("tsId", tsId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.GETISPLAY, params, this);
    }

    /**
     * 初始化音乐播放服务数据
     */
    @Override
    public void setup() {
        ResourceVo resourceVo = resourceVos.get(position);
        showNotification(isPlaying, resourceVo);
        intent = new Intent();
        intent.setAction(Constants.MUSIC_SERVICE);
        intent.setPackage(context.getPackageName());
        intent.putExtra("position", position);
        intent.putParcelableArrayListExtra("musicInfoVos", (ArrayList<? extends Parcelable>) resourceVos);
        context.startService(intent);
    }

    /**
     * 通过发送广播的形式播放音乐
     */
    @Override
    public void play() {
        isPlaying = true;
        view.setIsPlay(isPlaying);
        showNotification(isPlaying, resourceVos.get(position));
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PLAY);
        context.sendBroadcast(intent);
    }

    /**
     * 通过发送广播的形式暂停音乐
     */
    @Override
    public void pause() {
        isPlaying = false;
        view.setIsPlay(isPlaying);
        showNotification(isPlaying, resourceVos.get(position));
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PAUSE);
        context.sendBroadcast(intent);
    }

    /**
     * 通过发送广播的形式切换下一首歌曲
     */
    @Override
    public void nextSong() {
        isPlaying = true;
        view.setIsPlay(isPlaying);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_NEXT);
        context.sendBroadcast(intent);

    }

    /**
     * 通过发送广播的形式切换上一首音乐
     */
    @Override
    public void preSong() {
        isPlaying = true;
        view.setIsPlay(isPlaying);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PRV);
        context.sendBroadcast(intent);
    }

    /**
     * 通过发送广播的形式改变播放页面的播放进度条
     *
     * @param progress 播放进度
     */
    @Override
    public void changeSeeBar(int progress) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PROCRESSBAR);
        intent.putExtra("progress", progress);
        context.sendBroadcast(intent);
    }

    /**
     * 停止服务
     */
    public void stopService() {
        if (intent != null) {
            context.stopService(intent);
        }
    }

    /**
     * 设置当前播放歌曲的位置
     *
     * @param position 播放位置
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * 发送广播，设置锁屏的内容
     *
     * @param resourceVo 当前播放资源的内容
     * @param isPlaying  是否正在部分
     */
    @Override
    public void sendLockText(ResourceVo resourceVo, boolean isPlaying) {
        Intent intent = new Intent(Constants.ACTION_CHANGETEXT);
        intent.putExtra("resourceVo", resourceVo);
        intent.putExtra("isPlaying", isPlaying);
        context.sendBroadcast(intent);
    }

    /**
     * 注册所有的广播
     */
    @Override
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MUSIC_NEXT);
        filter.addAction(Constants.MUSIC_CURRENT);
        filter.addAction(Constants.MUSIC_UPDATE);
        filter.addAction(Constants.MUSIC_ANIAMATION);
        filter.addAction(Constants.ACTION_NOTIFICATION);
        context.registerReceiver(myMusicReceiver, filter);
        IntentFilter mScreenFilter = new IntentFilter();
        mScreenFilter.addAction(Constants.MUSIC_UPDATE);
        context.registerReceiver(PoaitionChangeReceiver, mScreenFilter);
    }

    /**
     * 注销所有的广播
     */
    @Override
    public void unRisterReceiver() {
        if (myMusicReceiver != null) {
            context.unregisterReceiver(myMusicReceiver);
        }
        if (PoaitionChangeReceiver != null) {
            context.unregisterReceiver(PoaitionChangeReceiver);
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.USER_GETTHENELIST)) {
            if (date != null) {
                Result<ArrayList<ResourceVo>> data = (Result<ArrayList<ResourceVo>>) date;
                ArrayList<ResourceVo> resourceVos = data.getData();
                if (resourceVos.size() > 0 && resourceVos != null) {
                    this.resourceVos = resourceVos;
                    view.setSongList(resourceVos);
                    if (resourceId != null) {
                        for (int i = 0; i < resourceVos.size(); i++) {
                            ResourceVo resourceVo = resourceVos.get(i);
                            if (String.valueOf(resourceVo.getResourceId()).equals(resourceId)) {
                                position = i;
                            }
                        }
                    } else {
                        position = 0;
                    }
                    view.setPosition(position);
                    setup();

                }
            }
        } else if (uri.equals(Apiurl.SAVEPLAYRECORDING)) {
            Result<String> data = (Result<String>) date;
            String result = data.getData();
            Log.i("SSS", result);

        } else if (uri.equals(Apiurl.GETISPLAY)) {
            if (date != null) {
                int isUserPay = 1;
                Result<String> result = (Result<String>) date;
                String code = result.getCode();
                if (code.equals("0")) {
                    isUserPay = 1;
                } else if (code.equals("4")) {
                    isUserPay = 1;
                    //  Toast.makeText(context,"到期时间（7天内）",Toast.LENGTH_SHORT).show();
                } else if (code.equals("5")) {
                    Toast.makeText(context, "已过期", Toast.LENGTH_SHORT).show();
                    isUserPay = 0;
                } else if (code.equals("6")) {
                    //   Toast.makeText(context,"校方已退订（老师角色）",Toast.LENGTH_SHORT).show();
                    isUserPay = 0;
                } else if (code.equals("7")) {
                    isUserPay = 0;
                    Toast.makeText(context, "账号冻结，不可访问，直接打回登录页面", Toast.LENGTH_SHORT).show();
                } else if (code.equals("8")) {
                    isUserPay = 0;
                    Toast.makeText(context, "信息查询不全，无法判断", Toast.LENGTH_SHORT).show();
                }
                Log.i("sssss",isUserPay+"=----------------------------");
                userMessage.setIsUserPay(isUserPay);
            }
        }
    }
    /**
     * 设置今天日期
     */
    private void getUserPaly() {
        UserMessage userMessage = UserMessage.getInstance(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        //设置今天是否第一次打开之后设置为
        if (!userMessage.getTodayDate().equals(today)) {
            getIsUserPay(userMessage.getTsId(), userMessage.getAccount_id());
            userMessage.setTodayDate(today);
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据资源信息创建通知栏
     *
     * @param isPlaying  是的播放
     * @param resourceVo 资源信息类
     */
    @Override
    public void showNotification(boolean isPlaying, ResourceVo resourceVo) {
        Bitmap bitmap = ImageCache.getBitmap(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()));
        MusicNotification musicNotification = new MusicNotification(context, resourceVo, isPlaying, bitmap);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_notification_app_logo)
                .setWhen(System.currentTimeMillis())
                .setCustomContentView(musicNotification.getSmallContentView())
                .setCustomBigContentView(musicNotification.getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notification);
    }

    /**
     * 监听屏幕的广播，发送信息初始化的
     * 给锁屏屏幕变亮的广播
     */
    private BroadcastReceiver PoaitionChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.MUSIC_UPDATE)) {
                position = intent.getIntExtra("position", position);
                sendLockText(resourceVos.get(position), isPlaying);
                showNotification(isPlaying, resourceVos.get(position));
            }
        }
    };

    /**
     * 清除通知栏
     */
    @Override
    public void cleanNotification() {
        if (manager != null) {
            manager.cancel(1);
        }
    }
}
