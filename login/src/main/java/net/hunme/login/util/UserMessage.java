package net.hunme.login.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/19
 * 描    述：用户信息存储类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UserMessage {
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private static UserMessage userMessage;
    public UserMessage(Context context) {
        spf=context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        editor=spf.edit();
    }

    public static UserMessage getInstance(Context context){
        if(null==userMessage){
            userMessage=new UserMessage(context);
        }
       return userMessage;
    }

    public void setLoginName(String loginName){
        editor.putString("loginName",loginName);
        editor.commit();
    }

    public String getLoginName(){
        return  spf.getString("loginName","");
    }
}
