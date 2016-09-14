package net.hunme.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.baselibrary.util.MyAlertDialog;
import net.hunme.message.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/9/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MainLongClickDialog implements View.OnClickListener{
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
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    /**
     * 是否顶置
     */
    private boolean isTop;
    public MainLongClickDialog(Activity context, String targetId,String targetName,Conversation.ConversationType type){
        this.context  = context;
        this.targetId = targetId;
         this.targetName = targetName;
        this.type = type;
        initView();
    }
    public void  initView(){
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_main, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context,1);
        tv_name = (TextView)contentView.findViewById(R.id.tv_mainname);
        tv_top = (TextView)contentView.findViewById(R.id.tv_maintop);
        tv_remove = (TextView)contentView.findViewById(R.id.tv_mainremove);
        tv_top.setOnClickListener(this);
        tv_remove.setOnClickListener(this);
        tv_name.setText(targetName);
    }
   public void initData(){
       spf=context.getSharedPreferences("group", Context.MODE_PRIVATE);
       editor=spf.edit();
       isTop = spf.getBoolean("isTop",false);
       if (isTop){
           tv_top.setText("取消顶置");
       }else {
           tv_top.setText("顶置改会话");
       }
   }

    @Override
    public void onClick(View view) {
        int viewId= view.getId();
        if (viewId==R.id.tv_mainremove){
            removeConversation(targetId);
            alertDialog.dismiss();
        }else if (viewId==R.id.tv_maintop){
            if (isTop){
                setTopConversation(false);
                editor.putBoolean("isTop",false);
            }else {
                setTopConversation(true);
                editor.putBoolean("isTop",true);
            }
            editor.commit();
            alertDialog.dismiss();
        }
    }
    private void removeConversation(String targetId){
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().removeConversation(type,targetId, new RongIMClient.ResultCallback<Boolean>(){
                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (aBoolean){
                        Log.i("TAG","移除成功！");
                    }else {
                        Log.i("TAG","移除失败！");
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("TAG",errorCode.getMessage());
                }
            });
        }
    }
    /**
     * 设置消息顶置
     * @param  isTop 是否顶置
     */
    private void setTopConversation(final boolean isTop) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(type, targetId, isTop, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean issuccess) {
                    if (issuccess){
                        if (isTop){
                            Toast.makeText(context,"顶置该会话！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"取消顶置！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(context,errorCode.getMessage(),Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}
