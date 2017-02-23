package net.hongzhang.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.hongzhang.baselibrary.contract.ContractsDb;
import net.hongzhang.baselibrary.contract.ContractsDbHelper;
import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.message.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 作者： wh
 * 时间： 2016/9/14
 * 名称：消息列表长按，消息页面长按消息显示对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MainLongClickDialog implements View.OnClickListener {
    private Activity context;
    /**
     * 当前操作id
     */
    private String targetId;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    /**
     * 名字
     */
    private TextView tv_name;
    private String targetName;
    /**
     * 置顶
     */
    private TextView tv_top;
    /**
     * 移除会话
     */
    private TextView tv_remove;
    /**
     * 会话类型
     */
    private Conversation.ConversationType type;
    /**
     * 标记 flag 0消息列表长按某个会话，1消息页面长按消息
     */
    private int flag = 0;
    /**
     * 群组的数据库
     */
    private GroupsDbHelper groupsDbHelper;
    /**
     * 联系人数据库
     */
    private ContractsDbHelper contractsDbHelper;
    private SQLiteDatabase db;
    /**
     * 是否顶置
     */
    private boolean isTop;
    /**
     * 消息
     */
    private Message message;

    //长按消息列表长按某个会话
    public MainLongClickDialog(Activity context, String targetId, String targetName, Conversation.ConversationType type, int flag) {
        this.context = context;
        this.targetId = targetId;
        this.targetName = targetName;
        this.type = type;
        this.flag = flag;
        initView();
    }

    //消息页面长按消息
    public MainLongClickDialog(Activity context, Message message, int flag) {
        this.context = context;
        this.flag = flag;
        this.message = message;
        initView();
    }

    public void initView() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_main, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context, 1);
        tv_name = (TextView) contentView.findViewById(R.id.tv_mainname);
        tv_top = (TextView) contentView.findViewById(R.id.tv_maintop);
        tv_remove = (TextView) contentView.findViewById(R.id.tv_mainremove);
        tv_top.setOnClickListener(this);
        tv_remove.setOnClickListener(this);
    }

    /**
     * 设置对话框的内容
     */
    public void initData() {
        switch (flag) {
            case 0:
                groupsDbHelper = GroupsDbHelper.getinstance();
                contractsDbHelper = ContractsDbHelper.getinstance();
                if (type == Conversation.ConversationType.GROUP) {
                    db = new GroupDb(context).getWritableDatabase();
                    isTop = groupsDbHelper.getTop(db, targetId);
                } else if (type == Conversation.ConversationType.PRIVATE) {
                    db = new ContractsDb(context).getWritableDatabase();
                    isTop = contractsDbHelper.getTop(db, targetId);
                }
                if (isTop) {
                    tv_top.setText("取消置顶");
                } else {
                    tv_top.setText("置顶该会话");
                }
                tv_remove.setText("从会话列表中移除");
                tv_name.setText(targetName);
                break;
            case 1:
                tv_top.setText("复制消息");
                tv_remove.setText("删除消息");
                try {
                    tv_name.setText(message.getContent().getUserInfo().getName());
                }catch (Exception e){
                    e.printStackTrace();
                }
                MessageContent messageContent = message.getContent();
                if (messageContent instanceof TextMessage) { //文本消息
                        tv_top.setVisibility(View.VISIBLE);
                } else if (messageContent instanceof ImageMessage) {//图片消息
                        tv_top.setVisibility(View.GONE);
                } else if (messageContent instanceof VoiceMessage) {//语音消息
                        tv_top.setVisibility(View.GONE);
                } else if (messageContent instanceof RichContentMessage) {//图文消息
                        tv_top.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_mainremove) {
            if (flag == 0) {
                removeConversation(targetId);
            } else if (flag == 1) {
                removeMessage();
            }
        } else if (viewId == R.id.tv_maintop) {
            switch (flag) {
                case 0:
                    if (isTop) {
                        setTopConversation(false);
                        if (type == Conversation.ConversationType.GROUP) {
                            groupsDbHelper.updateIsTop(db, 0, targetId);
                        } else if (type == Conversation.ConversationType.PRIVATE) {
                            contractsDbHelper.updateIsTop(db, 0, targetId);
                        }

                    } else {
                        setTopConversation(true);
                        if (type == Conversation.ConversationType.GROUP) {
                            groupsDbHelper.updateIsTop(db, 1, targetId);
                        } else if (type == Conversation.ConversationType.PRIVATE) {
                            contractsDbHelper.updateIsTop(db, 1, targetId);
                        }
                    }
                    break;
                case 1:
                    MessageContent messageContent = message.getContent();
                    if (messageContent instanceof TextMessage) { //文本消息
                        tv_top.setVisibility(View.VISIBLE);
                        TextMessage textMessage = (TextMessage) messageContent;
                        copy(textMessage.getContent());
                    } else if (messageContent instanceof RichContentMessage) {//图文消息
                        tv_top.setVisibility(View.VISIBLE);
                        RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                        copy(richContentMessage.getContent());
                    }
                    break;
            }
        }
        alertDialog.dismiss();
    }

    /**
     * 移除会话
     *
     * @param targetId 会话id
     */
    private void removeConversation(final String targetId) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().removeConversation(type, targetId, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (aBoolean) {
                        if (type == Conversation.ConversationType.GROUP) {
                            groupsDbHelper.updateIsTop(db, 0, targetId);
                        } else if (type == Conversation.ConversationType.PRIVATE) {
                            contractsDbHelper.updateIsTop(db, 0, targetId);
                        }
                        Log.i("TAG", "移除成功！");
                    } else {
                        Log.i("TAG", "移除失败！");
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("TAG", errorCode.getMessage());
                }
            });
        }
    }

    /**
     * 设置消息顶置
     *
     * @param isTop 是否顶置
     */
    private void setTopConversation(final boolean isTop) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(type, targetId, isTop, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean issuccess) {
                    if (issuccess) {
                        if (isTop) {
                            Toast.makeText(context, "置顶该会话！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "取消置顶！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(context, errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    /**
     * 复制
     *
     * @param copyText 复制内容
     */
    private void copy(String copyText) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(copyText.trim());
    }

    /**
     * 删除消息
     */
    private void removeMessage() {
        int[] messageids = new int[1];
        messageids[0] = message.getMessageId();
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().deleteMessages(messageids, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean issuccess) {
                    if (issuccess) {
                        Log.i("TAG", "移除成功！");
                    } else {
                        Log.i("TAG", "移除失败！");
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("TAG", errorCode.getMessage());
                }
            });
        }
    }
}
