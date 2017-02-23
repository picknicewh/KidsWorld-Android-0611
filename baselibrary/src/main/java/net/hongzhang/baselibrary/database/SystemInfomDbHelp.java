package net.hongzhang.baselibrary.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.hongzhang.baselibrary.mode.SystemInformVo;
import net.hongzhang.baselibrary.util.G;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：系统消息数据库帮助类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SystemInfomDbHelp {
    public  static SystemInfomDbHelp systemInfoDbHelp;

    public  static SystemInfomDbHelp getinstance(){
        systemInfoDbHelp = new SystemInfomDbHelp();
        return  systemInfoDbHelp;
    }
    /**
     * 插入数据
     */
    public  void insert(SQLiteDatabase db, String title, String content, String time, int flag) {
        String sql = "insert into user" + "(title,content,time,flag) values ('"+title+"','" + content + "','" + time + "'," + flag + ")";
        db.execSQL(sql);
    }
    /**
     * 查询所有数据数据,获取列表信息
     */
    public  List<SystemInformVo> getSystemInformVo(SQLiteDatabase db){
        List<SystemInformVo> informVoList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from user",null);
        int idindex  = cursor.getColumnIndex("uid");
        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex= cursor.getColumnIndex("content");
        int  timeIndex = cursor.getColumnIndex("time");
        int flagIndex  = cursor.getColumnIndex("flag");
        while (cursor.moveToNext()){
            SystemInformVo systemInformVo = new SystemInformVo();
            int id = cursor.getInt(idindex);
            String title = cursor.getString(titleIndex);
            String content = cursor.getString(contentIndex);
            String time = cursor.getString(timeIndex);
            int flag = cursor.getInt(flagIndex);
            systemInformVo.setId(id);
            systemInformVo.setTitle(title);
            systemInformVo.setTime(time);
            systemInformVo.setFlag(flag);
            systemInformVo.setContent(content);
            informVoList.add(systemInformVo);
            G.log(title+"---------rrr");
        }
        return informVoList;
    }

    /**
     * 通过内容查询单个数据是否阅读
     * @param  db 数据库
     * @param  content 内容
     */
    public  int queryFlag(SQLiteDatabase db,String content){
        int flag = 0;
        Cursor cursor  =db.rawQuery("select f1ag from user where content ='" +content+"'",null);
        while (cursor.moveToNext()){
            flag = cursor.getInt(cursor.getColumnIndex("flag"));
        }
        return flag;
    }
    /**
     * 修改数据
     * @param  db 数据库
     * @param  id 数据id
     * @param  flag 标记位
     */
    public  void  update(SQLiteDatabase db,int id,int flag){
        String sql = "update user " + " set flag= " + flag + " where uid = " + id;
        db.execSQL(sql);
    }
    /**
     * 判断是否数据是否为空
     * @param  db 数据库
     */
    public   boolean  isEmpty(SQLiteDatabase db){
        Cursor cursor  =db.rawQuery("select * from user",null);
        int i = 0;
        while (cursor.moveToNext()){
            return false;
        }
        return true;

    }
    /**
     *删除通过id号删除单个系统消息
     * @param  db 数据库
     * @param id 唯一用户id号
     */
    public void  deleteById(SQLiteDatabase db,int id){
        String sql="delete from user where uid =" + id;
        db.execSQL(sql);
    }
    /**
     * 删除所有
     * @param db
     */
    public  void delete(SQLiteDatabase db){
        String sql="delete from user";
        db.execSQL(sql);
        Log.i("cscscs",isEmpty(db)+"");
        G.log(isEmpty(db)+"---是否删除数据库成功");
    }
}
