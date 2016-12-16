package net.hunme.discovery.util;

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

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.discovery.MainPlayActivity;
import net.hunme.discovery.R;

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
public class PlayMusicPresenter  implements  PlayMusicContract.Presenter, OkHttpListener {
    private PlayMusicContract.View view;
    private Context context;
    public static int position=0;
    private String tsId;
    public static boolean  isPlaying=false;
    private MainPlayActivity.MyMusicReceiver myMusicReceiver;
    public    static  List<ResourceVo>  resourceVos;
    public  static     NotificationManager manager;
    private      Intent intent;
    private String resourceId;
    private String  themeId;
    private UserMessage userMessage;
    public  PlayMusicPresenter(PlayMusicContract.View view , Context context, int position, MainPlayActivity.MyMusicReceiver myMusicReceiver, String themeId, String resourceId){
        this.context  =context;
        this.view = view;
        this.position = position;
        this.resourceId = resourceId;
        this.myMusicReceiver = myMusicReceiver;
        this.themeId = themeId;
        userMessage = UserMessage.getInstance(context);
         tsId = userMessage.getTsId();
        registerReceiver();
      //  tsId = "c14dceea93e244b6be7ceed3d65bf037";
    //   getSongList(tsId,String.valueOf(17));
        getSongList(tsId,themeId);
        getUserPaly();
    }
    @Override
    public void savePlayTheRecord(String tsId, String resourceId,int broadcastPace,int type) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("resourceid",resourceId);
        if (type==2){
            map.put("broadcastPace",broadcastPace);
        }
        map.put("type",type);
        Type mType=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.SAVEPLAYRECORDING,map,this);
    }
    @Override
    public void getSongList(String tsId, String  themeId) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("pageSize",999);
        map.put("pageNumber",1);
        map.put("albumId",themeId);
        map.put("account_id",UserMessage.getInstance(context).getAccount_id());
        Type mType=new TypeToken<Result<List<ResourceVo>>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETTHENELIST,map,this);
        view.showLoadingDialog();
    }
    @Override
    public void getResourceDetail(String tsId, String resourceId) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("resourceid",resourceId);
        Type mType=new TypeToken<Result<ResourceVo>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETSOURCEDETAIL,map,this);
    }
    @Override
    public void getIsUserPay(String tsId,String accountId){
        Map<String,Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("tsId",tsId);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.GETISPLAY,params,this);
    }
    @Override
    public void setup() {
        ResourceVo resourceVo = resourceVos.get(position);
        showNotification(isPlaying,resourceVo);
        intent = new Intent();
        intent.setAction(Constants.MUSIC_SERVICE);
        intent.setPackage(context.getPackageName());
        intent.putExtra("position",position);
        intent.putParcelableArrayListExtra("musicInfoVos", (ArrayList<? extends Parcelable>) resourceVos);
        context.startService(intent);
    }
    @Override
    public void play() {
        isPlaying = true;
         view.setIsPlay(isPlaying);
        showNotification(isPlaying,resourceVos.get(position));
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PLAY);
        context.sendBroadcast(intent);
    }
    @Override
    public void pause() {
        isPlaying = false;
        view.setIsPlay(isPlaying);
        showNotification(isPlaying,resourceVos.get(position));
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PAUSE);
        context.sendBroadcast(intent);
    }
    @Override
    public  void nextSong() {
        isPlaying = true;
        view.setIsPlay(isPlaying);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_NEXT);
        context.sendBroadcast(intent);

    }
    @Override
    public  void preSong() {
        isPlaying = true;
        view.setIsPlay(isPlaying);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PRV);
        context.sendBroadcast(intent);
    }
    @Override
    public  void changeSeeBar(int progress) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PROCRESSBAR);
        intent.putExtra("progress",progress);
        context.sendBroadcast(intent);
    }
    public  void stopService(){
        if (intent!=null){
            context.stopService(intent);
        }
    }
    public  void setPosition(int position){
        this.position =  position;
    }
    /**
     * 发送广播，设置锁屏的内容
     * @param  resourceVo 当前播放资源的内容
     *@param  isPlaying 是否正在部分
     */
    @Override
    public void sendLockText(ResourceVo resourceVo, boolean isPlaying) {
        Intent intent  = new Intent(Constants.ACTION_CHANGETEXT);
        intent.putExtra("resourceVo",resourceVo);
        intent.putExtra("isPlaying",isPlaying);
        context.sendBroadcast(intent);
    }
    @Override
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MUSIC_NEXT);
        filter.addAction(Constants.MUSIC_CURRENT);
        filter.addAction(Constants.MUSIC_UPDATE);
        filter.addAction(Constants.MUSIC_ANIAMATION);
        filter.addAction(Constants.ACTION_NOTIFICATION);
        context.registerReceiver(myMusicReceiver,filter);
        IntentFilter mScreenFilter = new IntentFilter();
        mScreenFilter.addAction(Constants.MUSIC_UPDATE);
        context.registerReceiver(PoaitionChangeReceiver, mScreenFilter);
    }
    @Override
    public void unRisterReceiver() {
        if (myMusicReceiver!=null){
            context.unregisterReceiver(myMusicReceiver);
        }
        if (PoaitionChangeReceiver!=null){
            context.unregisterReceiver(PoaitionChangeReceiver);
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
         if (uri.equals(Apiurl.USER_GETTHENELIST)){
            if (date!=null){
                Result<ArrayList<ResourceVo>> data  = (Result<ArrayList<ResourceVo>>) date;
                ArrayList<ResourceVo>  resourceVos = data.getData();
                if (resourceVos.size()>0&&resourceVos!=null){
                    this.resourceVos = resourceVos;
                    view.setSongList(resourceVos);
                    if (resourceId!=null){
                        for (int i = 0 ;i<resourceVos.size();i++){
                            ResourceVo resourceVo = resourceVos.get(i);
                            if (String.valueOf(resourceVo.getResourceId()).equals(resourceId)){
                                position =i;
                            }
                        }
                    }else {
                        position=0;
                    }
                    view.setPosition(position);
                    setup();
                }
            }
        }else if (uri.equals(Apiurl.SAVEPLAYRECORDING)){
                Result<String> data = (Result<String>) date;
                String result  =data.getData();
                Log.i("SSS",result);

         }else if (uri.equals(Apiurl.GETISPLAY)){
             if (date!=null){
                 int isUserPay=1;
                 Result<String> result = (Result<String>) date;
                 String code =result.getCode();
                     if (code.equals("0")){
                         isUserPay=1;
                     }else if (code.equals("4")){
                         isUserPay=1;
                         Toast.makeText(context,"到期时间（7天内）",Toast.LENGTH_SHORT).show();
                     }else if (code.equals("5")){
                         Toast.makeText(context,"已过期",Toast.LENGTH_SHORT).show();
                         isUserPay=0;
                     }else if (code.equals("6")){
                         Toast.makeText(context,"校方已退订（老师角色）",Toast.LENGTH_SHORT).show();
                         isUserPay=0;
                     }else if (code.equals("7")){
                         isUserPay=0;
                         Toast.makeText(context,"账号冻结，不可访问，直接打回登录页面",Toast.LENGTH_SHORT).show();
                     }else if (code.equals("8")){
                         isUserPay=0;
                         Toast.makeText(context,"信息查询不全，无法判断",Toast.LENGTH_SHORT).show();
                     }
                  userMessage.setIsUserPay(isUserPay);
             }
         }
    }
    private void getUserPaly(){
      UserMessage   userMessage = UserMessage.getInstance(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        //设置今天是否第一次打开之后设置为
        if (!userMessage.getTodayDate().equals(today)){
            getIsUserPay(userMessage.getTsId(),userMessage.getAccount_id());
            userMessage.setTodayDate(today);
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
   @Override
    public void showNotification(boolean isPlaying ,ResourceVo resourceVo) {
        Bitmap  bitmap = ImageCache.getBitmap(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()));
        MusicNotification  musicNotification = new MusicNotification(context,resourceVo,isPlaying,bitmap);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_notification_app_logo)
                .setWhen(System.currentTimeMillis())
                .setCustomContentView(musicNotification.getSmallContentView())
                .setCustomBigContentView(musicNotification.getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();
       notification.flags=Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notification);
    }
    /**
     * 屏幕变亮的广播
     */
    private BroadcastReceiver PoaitionChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             if (intent.getAction().equals(Constants.MUSIC_UPDATE)){
                position =  intent.getIntExtra("position",position);
                sendLockText(resourceVos.get(position),isPlaying);
               showNotification(isPlaying,resourceVos.get(position));
            }
        }
    };
    @Override
    public void cleanNotification() {
        if (manager!=null){
            manager.cancel(1);
        }
    }
}
