package net.hongzhang.baselibrary.contract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.baselibrary.util.G;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/8/31
 * 名称：联系人信息数据库操作类（增删改查）
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ContractsDbHelper {
    public  static ContractsDbHelper message;
    public  static ContractsDbHelper getinstance(){
        message = new ContractsDbHelper();
        return  message;
    }
    /**
     * 插入数据
     */
    public  void insert(SQLiteDatabase db, String name, String ryId, String tsId,String imgurl,int isTop) {
        String sql = "insert into friend" + "(name,ryId,tsId,imgurl,isTop) values ('"+name+"','" + ryId + "','" + tsId + "','" + imgurl + "'," + isTop + ")";
        db.execSQL(sql);
    }
    /**
     * 查询所有数据数据,获取列表信息
     * @param  db 数据库
     */
    public List<ContractInfoVo> getFriendInformVos(SQLiteDatabase db){
        List<ContractInfoVo> contractInfoVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from friend",null);
        int idindex  = cursor.getColumnIndex("uid");
        int nameIndex = cursor.getColumnIndex("name");
        int ryidIndex= cursor.getColumnIndex("ryId");
        int tsIdIndex= cursor.getColumnIndex("tsId");
        int  imgurlIndex = cursor.getColumnIndex("imgurl");
        int  isTopIndex = cursor.getColumnIndex("isTop");
        while (cursor.moveToNext()){
            ContractInfoVo contractInfoVo = new ContractInfoVo();
            int id = cursor.getInt(idindex);
            String name = cursor.getString(nameIndex);
            String ryid = cursor.getString(ryidIndex);
            String tsId = cursor.getString(tsIdIndex);
            String imgurl = cursor.getString(imgurlIndex);
            int isTop = cursor.getInt(isTopIndex);
            contractInfoVo.setUid(id);
            contractInfoVo.setTsName(name);
            contractInfoVo.setRyId(ryid);
            contractInfoVo.setImg(imgurl);
            contractInfoVo.setTsId(tsId);
            contractInfoVo.setIsTop(isTop);
            contractInfoVos.add(contractInfoVo);

        }

        return contractInfoVos;
    }
    /**
     * 模糊查询所有与friendname相关联系人数据,获取列表信息
     * @param  db 数据库
     *    friendname 联系人姓名
     */
    public  List<ContractInfoVo> getFriendInform(SQLiteDatabase db,String friendname){
        List<ContractInfoVo> contractInfoVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from friend where name like '"+"%"+friendname+"%"+"'",null);
        int idindex  = cursor.getColumnIndex("uid");
        int nameIndex = cursor.getColumnIndex("name");
        int ryidIndex= cursor.getColumnIndex("ryId");
        int tsIdIndex= cursor.getColumnIndex("tsId");
        int  imgurlIndex = cursor.getColumnIndex("imgurl");
        while (cursor.moveToNext()){
            ContractInfoVo contractInfoVo = new ContractInfoVo();
            int id = cursor.getInt(idindex);
            String name = cursor.getString(nameIndex);
            String ryid = cursor.getString(ryidIndex);
            String tsId = cursor.getString(tsIdIndex);
            String imgurl = cursor.getString(imgurlIndex);
            contractInfoVo.setUid(id);
            contractInfoVo.setTsName(name);
            contractInfoVo.setRyId(ryid);
            contractInfoVo.setImg(imgurl);
            contractInfoVo.setTsId(tsId);
            contractInfoVos.add(contractInfoVo);
        }
        return contractInfoVos;
    }
    /**
     * 查询某个群是否顶置
     * @param  db 数据库
     * @param tsId id
     */
    public  boolean getTop(SQLiteDatabase db,String tsId){
        Cursor cursor = db.rawQuery("select isTop from friend where tsId ='"+tsId+"'",null);
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
     * 修改是否顶置
     * @param  db 数据库
     * @param  isTop 是否顶置
     * @param  tsId 群id
     */
    public void updateIsTop(SQLiteDatabase db ,int isTop,String tsId) {
        String sql = "update friend set isTop = "+ isTop + " where tsId ='"+tsId+"'";
        db.execSQL(sql);
    }


/*    *//**
     * 通过Id修改数据
     * @param  db 数据库
     * @param  id 数据id
     * @param  imgurl 头像url
     *//*
    public  void  update(SQLiteDatabase db,int id,String imgurl){
        String sql = "update friend " + " set imgurl=' " + imgurl + "' where uid = " + id;
        db.execSQL(sql);
    }*/
    /**
     * 通过tsId修改数据
     * @param  db 数据库
     * @param  tsId 数据tsId
     * @param  imgurl 头像url
     */
    public  void  updateImageUrl(SQLiteDatabase db,String tsId,String imgurl){
        String sql = "update friend " + " set imgurl='" + imgurl + "' where tsId ='" + tsId+"'";
        db.execSQL(sql);
    }
    /**
     * 判断是否数据是否为空
     * @param  db 数据库
     */
    public   boolean  isEmpty(SQLiteDatabase db){
        Cursor cursor  =db.rawQuery("select * from friend",null);
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
        String sql="delete from friend where uid =" + id;
        db.execSQL(sql);
    }
    /**
     * 删除所有
     * @param db
     */
    public  void delete(SQLiteDatabase db){
        String sql="delete from friend";
        db.execSQL(sql);
        G.log(isEmpty(db)+"---是否删除数据库成功");
    }

}

