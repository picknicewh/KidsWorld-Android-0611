package net.hunme.kidsworld.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import net.hunme.baselibrary.util.G;
import net.hunme.login.LoginActivity;
import net.hunme.login.util.UserMessage;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/19
 * 描    述：用户在主页的动作
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UserAction {
    /**
     * 用户是否需要去登录
     */
    public static void userLogin(Activity activity,Context context){
        if(G.isEmteny(UserMessage.getInstance(context).getLoginName())){
            activity.startActivity(new Intent(context, LoginActivity.class));
        }
    }
}
