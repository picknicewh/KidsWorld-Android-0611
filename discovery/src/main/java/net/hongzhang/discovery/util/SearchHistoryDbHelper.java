package net.hongzhang.discovery.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.modle.SearchKeyVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：搜索历史记录数据库操作类（增删改查）
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchHistoryDbHelper {
    public static SearchHistoryDbHelper message;

    public static SearchHistoryDbHelper getinstance() {
        message = new SearchHistoryDbHelper();
        return message;
    }

    /**
     * 插入数据
     *
     * @param tableName 表名
     */
    public void insert(SQLiteDatabase db, String key, String tableName) {
        if (getcount(db,tableName)>5){
            deleteByKey(db,getLastOne(db,tableName),tableName);
        }
        if (!G.isEmteny(key)) {
            Cursor cursor = db.rawQuery("select * from " + tableName + " order by uid desc", null);
            int keyIndex = cursor.getColumnIndex("key");
            while (cursor.moveToNext()) {
                String mKey = cursor.getString(keyIndex);
                if (mKey.equals(key)) {
                    deleteByKey(db, key, tableName);
                }
            }
            String sql = "insert into " + tableName + " " + "(key) values ('" + key + "')";
            db.execSQL(sql);
        }
    }


    /**
     * 查询所有key
     *
     * @param db        数据库
     * @param tableName 表名
     */
    public List<SearchKeyVo> getKeyList(SQLiteDatabase db, String tableName) {
        List<SearchKeyVo> searchKeyVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + tableName + " order by uid desc", null);
        int idIndex = cursor.getColumnIndex("uid");
        int keyIndex = cursor.getColumnIndex("key");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            String key = cursor.getString(keyIndex);
            SearchKeyVo searchKeyVo = new SearchKeyVo();
            searchKeyVo.setKey(key);
            searchKeyVo.setUid(id);
            searchKeyVos.add(searchKeyVo);
        }
        cursor.close();
        return searchKeyVos;
    }

    public String getKey(SQLiteDatabase db, int uid, String tableName) {
        Cursor cursor = db.rawQuery("select key from " + tableName + "  where uid = " + uid, null);
        int keyIndex = cursor.getColumnIndex("key");
        while (cursor.moveToNext()) {
            String key = cursor.getString(keyIndex);
            return key;
        }
        return null;
    }

    /**
     * 判断是否数据是否为空
     *
     * @param db 数据库
     */
    public boolean isEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select * from " + tableName + " ", null);
        while (cursor.moveToNext()) {
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * 删除数据组key = key的字段
     *
     * @param db  数据库
     * @param key 关键字
     */

    public void deleteByKey(SQLiteDatabase db, String key, String tableName) {
        String sql = "delete from " + tableName + "  where key ='" + key + "'";
        db.execSQL(sql);
    }
    /**
     * 获取条数
     * @param db  数据库
     * @param tableName
     */
    public int getcount(SQLiteDatabase db,String tableName){
        Cursor cursor = db.rawQuery("select * from " + tableName + " ", null);
        int i = 0;
        while (cursor.moveToNext()){
            i++;
        }
        cursor.close();
        return i;
    }
     public String getLastOne(SQLiteDatabase database,String tableName) {
         List<SearchKeyVo> searchKeyVoList = getKeyList(database,tableName);
         return searchKeyVoList.get(searchKeyVoList.size()-1).getKey();
     }
    /**
     * 删除所有
     *
     * @param db
     */
    public void delete(SQLiteDatabase db, String tableName) {
        String sql = "delete from " + tableName + " ";
        db.execSQL(sql);
        G.log(isEmpty(db, tableName) + "---是否删除数据库成功");
    }

}

