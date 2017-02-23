package net.hongzhang.baselibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.hongzhang.baselibrary.util.PackageUtils;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：系统消息数据库
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SystemInfomDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "systeminfo.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private static final String TABLENAME = "user";//表名
    public SystemInfomDb(Context context) {
        super(context, DB_NAME, null, PackageUtils.getVersionCode(context));
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table " +TABLENAME+
                " (uid integer primary key autoincrement," +
                  "title varchar(50) not null,"+
                  "content varchar(50) not null,"+
                  "time varchar(50) not null,"+
                  "flag integer)";
        db.execSQL(sql);

    }

    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists " + TABLENAME;
        db.execSQL(sql);
        this.onCreate(db);
    }
}
