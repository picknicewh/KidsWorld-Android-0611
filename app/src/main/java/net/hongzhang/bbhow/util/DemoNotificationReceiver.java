package net.hongzhang.bbhow.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.rong.push.RongPushClient;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 作者： Administrator
 * 时间： 2016/8/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DemoNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        RongPushClient.ConversationType type = message.getConversationType();
        String targetName = message.getTargetUserName();
        String  targetId = message.getTargetId();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri.Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
        builder.appendPath("conversation").appendPath(type.getName())
                .appendQueryParameter("targetId", targetId)
                .appendQueryParameter("title", targetName);
        Uri uri = builder.build();
        intent.setData(uri);
        context.startActivity(intent);
        return true;
    }
}
