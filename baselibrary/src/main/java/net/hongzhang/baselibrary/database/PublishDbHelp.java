package net.hongzhang.baselibrary.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：通知数据库帮助类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PublishDbHelp {
    /**
     * 插入数据
     */
    public static  void insert(SQLiteDatabase db, String values){
        String sql="insert into publish (messageid) values ('"+values+"')";
        db.execSQL(sql);
    }

    /**
     * 查询数据
     */
    public static boolean select(SQLiteDatabase db, String values){
        Cursor cursor = db.rawQuery("select * from publish where messageid = "+"'"+values+"'",null);
        while (cursor.moveToNext()){
            return true;
        }
        return false;
      /*
        String sql="select * from user where usertype = "+"'"+values+"'";
        Cursor localCursor= db.rawQuery(sql,null);
        db.close();
        if (localCursor.getColumnCount()==0){
            Log.i("TAG",localCursor.getColumnCount()+"");
            return true;
        }
        return false;*/



    }

    /**
     * 删除所有
     * @param db
     */
    public static void delete(SQLiteDatabase db){
        String sql="delete from publish";
        db.execSQL(sql);

        String sqls = "DELETE FROM sqlite_sequence";
        db.execSQL(sqls);

    }
}
