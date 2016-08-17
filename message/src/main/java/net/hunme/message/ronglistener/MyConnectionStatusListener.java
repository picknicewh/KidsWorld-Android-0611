package net.hunme.message.ronglistener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.LoginActivity;
import net.hunme.message.R;
import net.hunme.user.util.MyAlertDialog;

import io.rong.imlib.RongIMClient;

/**
 * 作者： Administrator
 * 时间： 2016/8/17
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
    private Activity context;
     public  MyConnectionStatusListener(Activity context){
      this.context = context;
     }
    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus){
            case CONNECTED://连接成功。
                break;
            case DISCONNECTED://断开连接。
                //如果网络连接时，连接融云
                UserMessage userMessage = UserMessage.getInstance(context);
                if (G.isNetworkConnected(context)) {
                    BaseLibrary.connect(userMessage.getRyId(), context, userMessage.getUserName(), userMessage.getHoldImgUrl());
                }
                break;
            case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                  showAlertDialog();
                break;
        }
    }

    /**
     * 退出提示框
     */
    private void showAlertDialog() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View coupons_view = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
                final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, context);
                Button btn_conform = (Button) coupons_view.findViewById(R.id.btn_conform);
                TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_title);
                btn_conform.setText("确认退出");
                pop_title.setText("您的帐号已在其他设备上登录，如果不是您的操作，请尽快重新登录修改密码.");
                btn_conform.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        //退出账号
                        UserMessage.getInstance(context).clean();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        context.finish();
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }
}