package net.hunme.message.ronglistener;

import android.content.Context;
import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 作者： wh
 * 时间： 2016/7/11
 * 名称：融云接收消息监听事件
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener{
    private static   String MESSAGERECEIVER = "mian.mycircle.messagereceiver";
    public Context context;
    public MyReceiveMessageListener(Context context){
        this.context = context;
    }
    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        MessageContent messageContent = message.getContent();
        //开发者根据自己需求自行处理
        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.i("TAG1", "onReceived-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.i("TAG1", "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.i("TAG1", "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.i("TAG1", "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.i("TAG", "onReceived-其他消息，自己来判断处理");
        }
        return false;
    }


}
