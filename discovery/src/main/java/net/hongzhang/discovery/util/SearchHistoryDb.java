package net.hongzhang.discovery.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： wanghua
 * 时间： 2017/3/13
 * 名称:搜索历史
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchHistoryDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "search.db"; //数据库名称
    private static final int version = 323; //数据库版本
    public static final String TABLENAME = "search_key";//表名
    public static final String TABLENAME1 = "search_key_music";//表名
    public static final String TABLENAME2 = "search_key_video";//表名
    public static final String TABLENAME3 = "search_key_consult";//表名

    public SearchHistoryDb(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLENAME +
                "(uid integer primary key autoincrement," +
                "key varchar(50) not null)";
        String sql1 = "create table " + TABLENAME1 +
                "(uid integer primary key autoincrement," +
                "key varchar(50) not null)";
        String sql2 = "create table " + TABLENAME2 +
                "(uid integer primary key autoincrement," +
                "key varchar(50) not null)";
        String sql3 = "create table " + TABLENAME3 +
                "(uid integer primary key autoincrement," +
                "key varchar(50) not null)";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists " + TABLENAME;
        sqLiteDatabase.execSQL(sql);
        String sql1 = "drop table if exists " + TABLENAME1;
        sqLiteDatabase.execSQL(sql1);
        String sql2 = "drop table if exists " + TABLENAME2;
        sqLiteDatabase.execSQL(sql2);
        String sql3 = "drop table if exists " + TABLENAME3;
        sqLiteDatabase.execSQL(sql3);
        this.onCreate(sqLiteDatabase);
    }
}
