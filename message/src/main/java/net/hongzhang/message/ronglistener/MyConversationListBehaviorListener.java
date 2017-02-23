package net.hongzhang.message.ronglistener;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import net.hongzhang.message.widget.MainLongClickDialog;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;

/**
 * 作者： Administrator
 * 时间： 2016/9/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {
    private Activity activity;
    public MyConversationListBehaviorListener(Activity activity){
        this.activity = activity;
    }
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return true;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        MainLongClickDialog dialog = new MainLongClickDialog(activity,uiConversation.getConversationTargetId(),uiConversation.getUIConversationTitle(),uiConversation.getConversationType(),0);
        dialog.initData();
        return true;
    }
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }
}
