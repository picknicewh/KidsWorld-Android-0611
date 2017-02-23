package net.hongzhang.login.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.login.LoginActivity;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/20
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UserAction {
    /**
     *  前往登录
     * @param activity 当前Activity
     * @param context 上下文对象
     */
    public static void isGoLogin(Activity activity,Context context){
        if (G.isEmteny(UserMessage.getInstance(context).getLoginName())){
            activity.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
    }

    /**
     * 保存用户信息
     * @param context 上下文对象
     * @param userName 用户名
     * @param holdImgUrl 用户头像url
     * @param className 班级名字
     * @param schoolName 学校名字
     * @param ryId 融云通讯ID
     * @param tsId 用户ID 用户唯一标识
     * @param
     */
    public static void saveUserMessage(Context context, String userName, String holdImgUrl,
                                       String className, String schoolName,
                                       String ryId, String tsId,String type,String sex,String signature,String account_id){
        UserMessage um =UserMessage.getInstance(context);
        um.setUserName(userName);
        um.setHoldImgUrl(holdImgUrl);
        um.setClassName(className);
        um.setSchoolName(schoolName);
        um.setRyId(ryId);
        um.setTsId(tsId);
        um.setType(type);
        um.setSex(sex);
        um.setUserSign(signature);
        um.setAccount_id(account_id);
    }

    /**
     * 保存用户登录名和密码
     * @param context
     * @param loginName
     * @param passWord
     */
    public static void saveLoginMessage(Context context,String loginName,String passWord){
        UserMessage um =UserMessage.getInstance(context);
        um.setLoginName(loginName);
        um.setPassword(passWord);
    }

    /**
     * 前往主页面
     * @param activity
     */
    public static void goMainActivity(Activity activity){
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.bbhow.MainActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(componetName);
        activity.startActivity(intent);
    }
}
