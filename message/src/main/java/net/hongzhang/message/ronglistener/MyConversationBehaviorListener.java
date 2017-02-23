package net.hongzhang.message.ronglistener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import net.hongzhang.message.activity.PersonDetailActivity;
import net.hongzhang.message.widget.MainLongClickDialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：融云聊天页面的监听事件
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyConversationBehaviorListener  implements RongIM.ConversationBehaviorListener{
    private Activity activity;
   public  MyConversationBehaviorListener(Activity activity){
       this.activity  =activity;

   }
    /**
     * 当点击用户头像后执行。
     *
     * @param context           上下文。
     * @param conversationType  会话类型。
     * @param userInfo          被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {

        Intent intent  = new Intent(context,PersonDetailActivity.class);
        intent.putExtra("targetId",userInfo.getUserId());
        context.startActivity(intent);
      /*  Intent intent = new Intent(context, ImagePagerActivity.class);
        ArrayList<String> urls = new ArrayList<>();
        if (userInfo.getPortraitUri() !=null){
            urls.add(userInfo.getPortraitUri().toString());
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
            intent.putExtra("source","message");
            context.startActivity(intent);
        }*/
        return true;
    }
    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

        MainLongClickDialog dialog = new MainLongClickDialog(activity, message,1);
        dialog.initData();
        return true;
    }
    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }
}