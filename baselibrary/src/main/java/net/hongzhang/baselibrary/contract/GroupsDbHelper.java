package net.hongzhang.baselibrary.contract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.hongzhang.baselibrary.mode.GroupInforVo;
import net.hongzhang.baselibrary.util.G;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：群组信息数据库操作类（增删改查）
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupsDbHelper {
    public  static GroupsDbHelper message;
    public  static GroupsDbHelper getinstance(){
        message = new GroupsDbHelper();
        return  message;
    }
    /**
     * 插入数据
     * @param  db 数据库
     * @param  groupName 群名称
     * @param  classId 群id
     */
    public  void insert(SQLiteDatabase db, String groupName, String classId,int isTop,int isStatus) {
        String sql = "insert into mgroup" + "(groupName,classId,isTop,isStatus) values ('"+groupName+"','" + classId + "',"+isTop+","+isStatus+")";
        db.execSQL(sql);
    }
    /**
     * 查询所有数据数据,获取列表信息
     * @param  db 数据库
     */
    public List<GroupInforVo> getGroupInformVos(SQLiteDatabase db){
        List<GroupInforVo> groupInforVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from mgroup",null);
        int idindex  = cursor.getColumnIndex("uid");
        int nameIndex = cursor.getColumnIndex("groupName");
        int classIdIndex= cursor.getColumnIndex("classId");
        while (cursor.moveToNext()){
            GroupInforVo groupInforVo = new GroupInforVo();
            int id = cursor.getInt(idindex);
            String groupName = cursor.getString(nameIndex);
            String classId = cursor.getString(classIdIndex);
            groupInforVo.setUid(id);
            groupInforVo.setGroupName(groupName);
            groupInforVo.setClassId(classId);
            groupInforVos.add(groupInforVo);
        }
        return groupInforVos;
    }
    /**
     * 获取所以群中名称
     *  @param  db 数据库
     */
    public List<String> getGroupNames(SQLiteDatabase db){
        List<String> groupNames = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from mgroup",null);

        int nameIndex = cursor.getColumnIndex("groupName");

        while (cursor.moveToNext()){
            String groupName = cursor.getString(nameIndex);
            groupNames.add(groupName);

        }
        return groupNames;
    }
    /**
     * 查询某个群是否顶置
     * @param  db 数据库
     * @param classId 群组id
     */
   public  boolean getTop(SQLiteDatabase db,String classId){
       Cursor cursor = db.rawQuery("select isTop from mgroup where classId ='"+classId+"'",null);
       int topIndex  = cursor.getColumnIndex("isTop");
       while (cursor.moveToNext()){
           int istop = cursor.getInt(topIndex);
           if (istop==1){
               return true;
           }else {
               return false;
           }
       }
      return false;
   }
    /**
     * 查询某个群是否设置免打扰
     * @param  db 数据库
     * @param  classId 群组id
     */
    public  boolean getStatus(SQLiteDatabase db,String classId){
        Cursor cursor = db.rawQuery("select isStatus from mgroup where classId ='"+classId+"'",null);
        int statusIndex  = cursor.getColumnIndex("isStatus");
        while (cursor.moveToNext()){
            int isstatus = cursor.getInt(statusIndex);
            if (isstatus==1){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
    /**
     * 判断是否数据是否为空
     * @param  db 数据库
     */
    public   boolean  isEmpty(SQLiteDatabase db){
        Cursor cursor  =db.rawQuery("select * from mgroup",null);
        int i = 0;
        while (cursor.moveToNext()){
            return false;
        }
        return true;

    }
    /**
     *删除通过id号删除单个系统消息
     * @param  db 数据库
     * @param classId 群id号
     */
    public void  deleteByClassId(SQLiteDatabase db,String classId){
        String sql="delete from mgroup where classId ='"+classId+"'";
        db.execSQL(sql);
    }
    /**
     * 修改群名称
     * @param  db 数据库
     * @param  groupName 群名称
     * @param  classId 群id
     */
    public void updateGroupName(SQLiteDatabase db ,String groupName,String classId) {
        String sql = "update mgroup set groupName = '"+ groupName + "'where classId ='"+classId+"'";
        db.execSQL(sql);
    }
    /**
     * 修改是否设置免打扰
     * @param  db 数据库
     * @param  isStatus 是否设置免打扰
     * @param  classId 群id
     */
    public void updateIsStatus(SQLiteDatabase db ,int  isStatus ,String classId) {
        String sql = "update mgroup set isStatus = "+ isStatus + " where classId ='"+classId+"'";
        db.execSQL(sql);
    }

    /**
     * 修改是否顶置
     * @param  db 数据库
     * @param  isTop 是否顶置
     * @param  classId 群id
     */
    public void updateIsTop(SQLiteDatabase db ,int isTop,String classId) {
        String sql = "update mgroup set isTop = "+ isTop + " where classId ='"+classId+"'";
        db.execSQL(sql);
    }
    /**
     * 删除所有
     * @param db
     */
    public  void delete(SQLiteDatabase db){
        String sql="delete from mgroup";
        db.execSQL(sql);
        G.log(isEmpty(db)+"---是否删除数据库成功");
    }

}

