package main.jpushlibrary.JPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.hongzhang.baselibrary.database.StatusInfoDb;
import net.hongzhang.baselibrary.database.StatusInfoDbHelper;
import net.hongzhang.baselibrary.database.SystemInfomDb;
import net.hongzhang.baselibrary.database.SystemInfomDbHelp;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.UserMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import main.jpushlibrary.R;

/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 极光推送广播接收
 * 版本说明：代码规范整改
 * 附加注释：当推送一个信息时，发送广播，在xml中有注册广播，根据不同的action做相应的操作
 * 主要接口：无
 *
 *
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "wanghua";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
         Log.i(TAG, "[MyJPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (intent.getAction().equals(JPushInterface.ACTION_REGISTRATION_ID)) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyJPushReceiver] 接收Registration Id : " + regId);
            //  sendRegistrationId(regId,context);
        } else if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            Log.i(TAG, "[MyJPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            String message = bundle.getString(JPushInterface.EXTRA_EXTRA);
          //  接收到推送下来的自定义消息: {"messagetype":"1","schoolid":"","classid":"298f1648653840fdaa6c396830025af5"}
            try {
                JSONObject jsonObject = new JSONObject(message);
                String messagetype = (String) jsonObject.get("messagetype");
                if (messagetype.equals("1")){//动态
                    String targetId = (String) jsonObject.get("targetId");
                    Intent myintent = new Intent(BroadcastConstant.STATUSDOS);
                    myintent.putExtra("targetId",targetId);
                    context.sendBroadcast(myintent);
                }else if (messagetype.equals("2")){//通知
                    schoolDosShow(BroadcastConstant.SCHOOLINFODOS,jsonObject,context);
                } else if (messagetype.equals("3")) {//系统通知
                    String mycontent = (String) jsonObject.get("content");
                    String create_time = (String) jsonObject.get("create_time");
                    String mytitle = (String) jsonObject.get("title");
                    String type = (String) jsonObject.get("type");
                    SystemInfomDb systemInfomDb = new SystemInfomDb(context);
                    SQLiteDatabase db = systemInfomDb.getWritableDatabase();
                    Intent myintent = new Intent(BroadcastConstant.SYSYDOS);
                    myintent.putExtra("isVisible",true);
                    switch (type){
                        case "0":
                            SystemInfomDbHelp.getinstance().insert(db,mytitle,mycontent,create_time,1);
                            context.sendBroadcast(myintent);
                            break;
                        case "1":
                            receivingNotification(context,mycontent,mytitle);
                            context.sendBroadcast(myintent);
                            SystemInfomDbHelp.getinstance().insert(db,mytitle,mycontent,create_time,1);
                            break;
                        case "2":
                            receivingNotification(context,mycontent,mytitle);
                            break;
                    }

                }else if (messagetype.equals("4")){//请假
                    schoolDosShow(BroadcastConstant.LEAVEASEKDOS,jsonObject,context);
                }else if (messagetype.equals("5")){//喂药
                    schoolDosShow(BroadcastConstant.MEDICINEDOS,jsonObject,context);
                }else if (messagetype.equals("6")){//动态评论
                    SQLiteDatabase db = new StatusInfoDb(context).getWritableDatabase();
                    StatusInfoDbHelper helper = StatusInfoDbHelper.getInstance();
                    String tsId = (String) jsonObject.get("tsId");
                    String createTime = (String) jsonObject.get("createTime");
                    String imgUrl = (String) jsonObject.get("imgUrl");
                    //添加到数据中
                    helper.insert(db,createTime,tsId,imgUrl,0);//按时间顺序插入到数据库中，越早推送的数据，存放在下面，先进后出
                    int count =helper.getNoReadcount(db, UserMessage.getInstance(context).getTsId());
                    if (count>0 ){
                            Intent myintent = new Intent(BroadcastConstant.COMMENTINFO);
                            myintent.putExtra("count",count);
                            myintent.putExtra("imageUrl",helper.getLatestUrl(db,UserMessage.getInstance(context).getTsId()));
                            context.sendBroadcast(myintent);
                    }
                    //删除超过20条时记录
                    helper.deleteOverTime(db,UserMessage.getInstance(context).getTsId());
                    Log.i("AAAAA","count:"+helper.getcount(db,UserMessage.getInstance(context).getTsId()));
                    Log.i("SSSSSS","推送下来的tsId："+tsId);
                    Log.i("SSSSSS","点赞的tsId："+tsId);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            Log.i(TAG, "[MyJPushReceiver] 接收到推送下来的通知");
        } else if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[MyJPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i(TAG, "[MyJPushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.i(TAG, "[MyJPushReceiver] Unhandled intent - " + intent.getAction());
        }
    }
    //29f45bed96f6463da1ff0da2986e061f
    //29f45bed96f6463da1ff0da2986e061f
     /**
     *  处理返回过来的数据，并发送通知
     */
    public  void receivingNotification(Context context,String message,String title){
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.user.activity.SystemInfoActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(componetName);
         PendingIntent default_pendingIntent =
                PendingIntent.getActivity(context, 1, intent, 0);
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // 使用notification
        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_main);
        //设置跳转的内容
        builder.setContentIntent(default_pendingIntent);
        if (title.equals("")){
            builder.setContentTitle(getApplicationName(context));
        }else {
            builder.setContentTitle(title);
        }
        Notification notification = builder.build();
        notification.flags  = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        notification.defaults = Notification.DEFAULT_SOUND;//设置通知的方式
        manager.notify(0,notification);
    }

    /**
     * 获取包名
     */
    public String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }
    /**
     *学校红点显示
     * @param  action
     * @param jsonObject
     * @param context
     */
    private void schoolDosShow(String action,JSONObject jsonObject,Context context) throws JSONException {
        //发送学校的通知,请假,喂药红点点广播
        String tsId = (String) jsonObject.get("tsId");
        Intent intent = new Intent(action);
        intent.putExtra("isVisible",true);
        intent.putExtra("tsId",tsId);
        context.sendBroadcast(intent);

    }

 // 打印所有的 intent extra 数据*/
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();

    }

}
