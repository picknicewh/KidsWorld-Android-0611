package net.hunme.baselibrary.util;

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

    /**
     * @param loginName 登录账号
     */
    public void setLoginName(String loginName){
        editor.putString("loginName",loginName);
        editor.commit();
    }
    public String getLoginName(){
        return  spf.getString("loginName","");
    }

    /**
     * @param userName  用户名字
     */
    public void setUserName(String userName){
        editor.putString("userName",userName);
        editor.commit();
    }
    public String getUserName(){
        return  spf.getString("userName","");
    }

    /**
     * @param holdImgUrl 用户头像url
     */
    public void setHoldImgUrl(String holdImgUrl){
        editor.putString("holdImgUrl",holdImgUrl);
        editor.commit();
    }
    public String getHoldImgUrl(){
        return  spf.getString("holdImgUrl","");
    }

    /**
     * @param className 班级名称
     */
    public void setClassName(String className){
        editor.putString("className",className);
        editor.commit();
    }
    public String getClassName(){
        return  spf.getString("className","");
    }

    /**
     * @param schoolName 学校名称
     */
    public void setSchoolName(String schoolName){
        editor.putString("schoolName",schoolName);
        editor.commit();
    }
    public String getSchoolName(){
        return  spf.getString("schoolName","");
    }

    /**
     * @param ryId 融云通讯ID
     */
    public void setRyId(String ryId){
        editor.putString("ryId",ryId);
        editor.commit();
    }
    public String getRyId(){
        return  spf.getString("ryId","");
    }

    /**
     * @param tsId 用户ID 唯一标识
     */
    public void setTsId(String tsId){
        editor.putString("tsId",tsId);
        editor.commit();
    }
    public String getTsId(){
        return  spf.getString("tsId","");
    }

    /**
     * 用户身份
     * @param type 1 学生 0 老师  2 班主任
     */
    public void setType(String type){
        editor.putString("type",type);
        editor.commit();
    }
    public String getType(){
        return spf.getString("type","");
    }

    /**
     * @param sex 用户性别
     */
    public void setSex(String sex){
        editor.putString("sex",sex);
        editor.commit();
    }
    public String getSex(){
        return  spf.getString("sex","未知");
    }

    /**
     * @param userSign  用户签名
     */
    public void setUserSign(String userSign){
        editor.putString("userSign",userSign);
        editor.commit();
    }
    public String getUserSign(){
        return spf.getString("userSign","还没有想好啊");
    }

    /**
     *  用户登录信息缓存
     * @param jsonCache 用户信息（包含多重身份）
     */
    public void setUserMessagejsonCache(String jsonCache){
        editor.putString("jsonCache",jsonCache);
        editor.commit();
    }
    public String getUserMessageJsonCache(){
        return spf.getString("jsonCache","");
    }

}
