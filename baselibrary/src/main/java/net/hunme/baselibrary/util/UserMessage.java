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
    private Context context;
    public UserMessage(Context context) {
        spf=context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        editor=spf.edit();
        this.context=context;
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
     * @param password 用户密码
     */
    public void setPassword(String password){
        editor.putString("password",password);
        editor.commit();
    }
    public String getPassword(){
        return spf.getString("password","");
    }
    
    /**
     * @param userName  用户名字
     */
    public void setUserName(String userName){
        editor.putString("userName",userName);
        editor.commit();
    }
    public String getUserName(){
        return  spf.getString("userName","大幂幂");
    }

    /**
     * @param holdImgUrl 用户头像url
     */
    public void setHoldImgUrl(String holdImgUrl){
        editor.putString("holdImgUrl",holdImgUrl);
        editor.commit();
    }
    public String getHoldImgUrl(){
        return  spf.getString("holdImgUrl","http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");
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
        return  spf.getString("ryId","V5tYQjjmYQGGUT5RP9YyZ0bso9ndFkPYvochz2Gw7s692q5Oy6+dsfcJT13ag45+j9HeWAqVtz/T0ApFSaea8Q==");
    }

    /**
     * @param tsId 用户ID 唯一标识
     */
    public void setTsId(String tsId){
        editor.putString("tsId",tsId);
        editor.commit();
    }
    public String getTsId(){
        return  spf.getString("tsId","1001");
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

    public void clean(){
        cleanPublishDb();
        editor.clear().commit();
    }

    /**
     * 清空系统消息数据库
     */
    public void cleanPublishDb(){
        PublishDbHelp.delete(new PublishDb(context).getReadableDatabase());
    }

    /**
     * 通知最后一条记录的时间
     * @param dateTime
     */
    public void setPublishDateTime(String dateTime){
        editor.putString("dateTime",dateTime);
        editor.commit();
    }
    public String getPublishDateTime(){
        return  spf.getString("dateTime","2016-07-01 00:00:00");
    }

}
