package net.hunme.baselibrary.contract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.hunme.baselibrary.mode.FriendInforVo;
import net.hunme.baselibrary.util.G;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/8/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class FriendsDbHelper {
    public  static FriendsDbHelper message;
    public  static FriendsDbHelper getinstance(){
        message = new FriendsDbHelper();
        return  message;
    }
    /**
     * 插入数据
     */
    public  void insert(SQLiteDatabase db, String name, String ryId, String imgurl) {
        String sql = "insert into friend" + "(name,ryId,imgurl) values ('"+name+"','" + ryId + "','" + imgurl + "')";
        db.execSQL(sql);
    }
    /**
     * 查询所有数据数据,获取列表信息
     */
    public List<FriendInforVo> getFriendInformVos(SQLiteDatabase db){
        List<FriendInforVo> friendInforVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from friend",null);
        int idindex  = cursor.getColumnIndex("uid");
        int nameIndex = cursor.getColumnIndex("name");
        int ryidIndex= cursor.getColumnIndex("ryId");
        int  imgurlIndex = cursor.getColumnIndex("imgurl");
        while (cursor.moveToNext()){
            FriendInforVo friendInforVo = new FriendInforVo();
            int id = cursor.getInt(idindex);
            String name = cursor.getString(nameIndex);
            String ryid = cursor.getString(ryidIndex);
            String imgurl = cursor.getString(imgurlIndex);
            friendInforVo.setId(id);
            friendInforVo.setName(name);
            friendInforVo.setRyid(ryid);
            friendInforVo.setImgurl(imgurl);
            friendInforVos.add(friendInforVo);
        }
        return friendInforVos;
    }

    /**
     * 修改数据
     * @param  db 数据库
     * @param  id 数据id
     * @param  imgurl 头像url
     */
    public  void  update(SQLiteDatabase db,int id,String imgurl){
        String sql = "update friend " + " set imgurl=' " + imgurl + "' where uid = " + id;
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

