package net.hongzhang.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.message.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/9/8
 * 名称：群组操作对话框
 * 版本说明：3.0.2 三个构造方法，不同的方法分别对应，情况群的聊天记录，解散群，以及添加或删除群的操作。
 * 附加注释：三个构造方法共同传值flag,通过flag的不同，代表不同的操作 0 清空聊天记录 1解散群 2移除群成员，4退出群，3添加新成员
 * 主要接口：1.查看不在群内成员 2.查看群成员 3.解散群
 */
public class OperationGroupDialog implements View.OnClickListener, OkHttpListener {
    /**
     * 清空聊天记录
     */
    public static final int FLAG_CLEAN = 0;
    /**
     * 解散群
     */
    public static final int FLAG_DISSOLVE = 1;
    /**
     * 移除群成员
     */
    public static final int FLAG_REMOVE_MEMBER = 2;
    /**
     * 退出群
     */
    public static final int FLAG_REMOVE = 4;
    /**
     * 添加
     */
    public static final int FLAG_ADD = 3;

    private Activity context;
    /**
     * 确定
     */
    private Button bt_conform;
    /**
     * 取消
     */
    private Button bt_cancel;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    /**
     * 当前退出群的id
     */
    private String targetId;
    /**
     * 信息
     */
    private TextView tv_message;
    /**
     * 群id
     */
    private String targetGroupId;
    /**
     * 群名称
     */
    private String targetGroupName;
    /**
     * 操作标记 0 清空聊天记录 1解散群 2 退出群，或者是添加新成员
     */
    private int flag;

    //清空聊天记录
    public OperationGroupDialog(Activity context, String targetId, int flag) {
        this.context = context;
        this.targetId = targetId;
        this.flag = flag;

    }

    //解散群
    public OperationGroupDialog(Activity context, String targetId, String targetGroupId, int flag) {
        this.context = context;
        this.targetId = targetId;
        this.targetGroupId = targetGroupId;
        this.flag = flag;
    }

    //退出群，或者是添加新成员
    public OperationGroupDialog(Activity context, String targetId, String targetGroupId, String targetGroupName, int flag) {
        this.context = context;
        this.targetId = targetId;
        this.targetGroupId = targetGroupId;
        this.targetGroupName = targetGroupName;
        this.flag = flag;
    }

    /**
     * 初始化view
     */
    public void initView() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exitgroup, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context, 1);
        tv_message = (TextView) contentView.findViewById(R.id.tv_message);
        bt_conform = (Button) contentView.findViewById(R.id.bt_egconform);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_egcancel);
        bt_cancel.setOnClickListener(this);
        bt_conform.setOnClickListener(this);
        setTitle(flag);
    }

    /**
     * 设置标题
     *
     * @param flag 标记
     */
    private void setTitle(int flag) {
        switch (flag) {
            case FLAG_CLEAN:
                tv_message.setText("确认清除缓存数据和清除图片？");
                break;
            case FLAG_DISSOLVE:
                tv_message.setText(" 删除并退出后，将不再接收改群信息");
                break;
            case FLAG_REMOVE_MEMBER:
                tv_message.setText("确认删除成员？");
                break;
            case FLAG_REMOVE:
                tv_message.setText("确认删除成员？");
                break;
            case FLAG_ADD:
                tv_message.setText("确认添加成员？");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_egcancel) {
            alertDialog.dismiss();
        } else if (view.getId() == R.id.bt_egconform) {
            switch (flag) {
                case FLAG_CLEAN:
                    tv_message.setText("确认清除缓存数据和清除图片？");
                    cleanMessage(targetId);
                    break;
                case FLAG_DISSOLVE:
                    tv_message.setText("删除并退出后，将不再接收改群信息!");
                    dissolveGroup(targetId, targetGroupId);
                    break;
                case FLAG_REMOVE_MEMBER:
                    tv_message.setText("确认删除成员？");
                    removeMember(targetId, targetGroupId);
                    break;
                case FLAG_REMOVE:
                    tv_message.setText("删除并退出后，将不再接收改群信息!");
                    removeMember(targetId, targetGroupId);
                    removeConversation(targetGroupId);
                    break;
                case FLAG_ADD:
                    tv_message.setText("确认添加成员？");
                    addMember(targetId, targetGroupId, targetGroupName);
                    break;
            }
            alertDialog.dismiss();
        }
    }

    /**
     * 移除群成员
     *
     * @param targetIds     移除的群成员id
     * @param targetGroupId 群id
     */
    private void removeMember(String targetIds, String targetGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsIds", targetIds);
        params.put("groupChatAdmin", UserMessage.getInstance(context).getTsId());
        params.put("groupChatId", targetGroupId);
        //  removeConversation(targetGroupId);
        //Log.i("EEEEEEEE","tsIds:"+targetIds+"groupChatAdmin:"+UserMessage.getInstance(context).getTsId()+"groupChatId:"+targetGroupId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_EXIT_MEMBER, params, this);
    }

    /**
     * 添加群成员
     *
     * @param targetIds       添加的群成员id
     * @param targetGroupId   群id
     * @param targetGroupName 群名称
     */
    private void addMember(String targetIds, String targetGroupId, String targetGroupName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsIds", targetIds);
        params.put("groupName", targetGroupName);
        params.put("groupChatId", targetGroupId);
        //Log.i("EEEEEEEE","tsIds:"+targetIds+"groupName:"+targetGroupName+"groupChatId:"+targetGroupId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_ADD_MEMBER, params, this);
    }

    /**
     * 解散群
     *
     * @param tsId          当前的角色名
     * @param targetGroupId 群id
     */
    private void dissolveGroup(String tsId, String targetGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("groupChatId", targetGroupId);
        //  Log.i("EEEEEEEE","tsId:"+tsId+"groupChatId:"+targetGroupId);
        removeConversation(targetGroupId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_DISSORE_GROUP, params, this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.contains(Apiurl.MESSAGE_EXIT_MEMBER)) {
            result(date);
            if (flag == FLAG_REMOVE) {
                context.finish();
            } else if (flag == FLAG_REMOVE_MEMBER) {
                context.finish();
            }
            GroupsDbHelper dbHelper = new GroupsDbHelper();
            GroupDb groupDb = new GroupDb(context);
            dbHelper.deleteByClassId(groupDb.getWritableDatabase(), targetGroupId);
        } else if (uri.contains(Apiurl.MESSAGE_ADD_MEMBER)) {
            result(date);
            context.finish();
        } else if (uri.contains(Apiurl.MESSAGE_DISSORE_GROUP)) {
            result(date);
            context.finish();

        }
    }

    /**
     * 返回结果
     *
     * @param date 结果值
     */
    private void result(Object date) {
        Result<String> data = (Result<String>) date;
        if (data != null) {
            String result = data.getData();
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
        alertDialog.dismiss();
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    /**
     * 移除会话
     *
     * @param targetId 会话的id
     */
    private void removeConversation(String targetId) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (aBoolean) {
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

    private void cleanMessage(String targetId) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, targetId,
                    new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            if (aBoolean) {
                                Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(context, errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
