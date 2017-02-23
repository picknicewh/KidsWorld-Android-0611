package net.hongzhang.baselibrary.contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：保存联系人信息数据库
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class ContractsDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "friend.db"; //数据库名称
    private static final int version = 303; //数据库版本
    private static final String TABLENAME = "friend";//表名
    public ContractsDb(Context context) {
        super(context, DB_NAME, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table " +TABLENAME+
                "(uid integer primary key autoincrement," +
                "name varchar(50) not null,"+
                "ryId varchar(50) not null,"+
                "tsId varchar(50) not null,"+
                "imgurl varchar(50) not null," +
                "isTop integer not null)";
        sqLiteDatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists " + TABLENAME;
        sqLiteDatabase.execSQL(sql);
        this.onCreate(sqLiteDatabase);
    }
}
