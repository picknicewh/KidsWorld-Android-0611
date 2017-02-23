package net.hongzhang.school.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.hongzhang.baselibrary.util.G;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：保存日期多选日历的日期选中状态
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DateDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "date.db"; //数据库名称
    private static final String TABLENAME = "mdate";//表名
    private static DateDb dateDb;

    public DateDb(Context context) {
        super(context, DB_NAME, null, 304);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLENAME +
                "(uid integer ," +
                "sign integer," +
                "date varchar(100)," +
                "state integer not null)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists " + TABLENAME;
        sqLiteDatabase.execSQL(sql);
        this.onCreate(sqLiteDatabase);
    }

    /**
     * 插入数据
     */
    public void insert(SQLiteDatabase db, int uid, int sign, String date, int state) {
        String sql = "insert into " + TABLENAME + "(uid,sign,date,state) values (" + uid + " ," + sign + ",'" + date + "'," + state + ")";
        db.execSQL(sql);
    }

    /**
     * 修改选中状态
     *
     * @param db 数据库
     */
    public void updateState(SQLiteDatabase db, int uid, int state) {
        String sql = "update " + TABLENAME + " set state = " + state + " where uid = " + uid + "";
        db.execSQL(sql);
    }

    /**
     * 修改标记
     *
     * @param db 数据库
     */
    public void updateSign(SQLiteDatabase db, int uid, int sign) {
        String sql = "update " + TABLENAME + " set sign = " + sign + " where uid = " + uid + "";
        db.execSQL(sql);
    }

    /**
     * 查询是否点击标记
     *
     * @param db  数据库
     * @param uid id
     */
    public int getSign(SQLiteDatabase db, int uid) {
        Cursor cursor =null;
        try {
            cursor = db.rawQuery("select sign from " + TABLENAME + " where uid =" + uid + "", null);
            int signIndex = cursor.getColumnIndex("sign");
            while (cursor.moveToNext()) {
                int sign = cursor.getInt(signIndex);
                if (cursor != null && sign != -1) {
                    cursor.close();
                }
                return sign;

            }
            return -1;
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }

    }
    /**
     * 查询所有选中日期
     *
     * @param db  数据库
     * @param state 状态
     */
    public String getDate(SQLiteDatabase db, int state) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select date from " + TABLENAME + " where state =" + state + "", null);
            int dateIndex = cursor.getColumnIndex("date");
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                String date = cursor.getString(dateIndex);
                buffer.append(date).append(";");
            }
            if (buffer.length()>0){
                buffer.deleteCharAt(buffer.length()-1);
            }
            return buffer.toString();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }

    }

    /**
     * 查询某个日期状态
     *
     * @param db  数据库
     * @param uid id
     */
    public int getState(SQLiteDatabase db, int uid) {
        Cursor cursor  =null;
        try {
            cursor = db.rawQuery("select state from " + TABLENAME + " where uid =" + uid + "", null);
            int stateIndex = cursor.getColumnIndex("state");
            while (cursor.moveToNext()) {
                int state = cursor.getInt(stateIndex);
                if (state!=-1&&cursor!=null){
                    cursor.close();
                    return state;
                }
            }
            return -1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
    }


    /**
     * 判断是否数据是否为空
     *
     * @param db 数据库
     */
    public boolean isEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from " + TABLENAME, null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            if (i != -1 && cursor != null) {
                cursor.close();
                return false;
            }
        }
        return true;
    }

    /**
     * 查询选中的日期个数
     *
     * @param db 数据库
     */
    public int getSelectedSize(SQLiteDatabase db, int state) {
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where state =" + state + "", null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
        }
        Log.i("ssffsss",i+"================");
        return i;
    }

    /**
     * 删除所有
     *
     * @param db
     */
    public void delete(SQLiteDatabase db) {
        String sql = "delete from  " + TABLENAME;
        db.execSQL(sql);
        G.log(isEmpty(db) + "---是否删除数据库成功");
    }

}
