package net.hongzhang.baselibrary.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.hongzhang.baselibrary.mode.StatusInfoVo;
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
public class StatusInfoDbHelper {
    private static final String dbname= "statusInfo";
    public  static StatusInfoDbHelper  message;
    public  static StatusInfoDbHelper getInstance(){
        message = new StatusInfoDbHelper();
        return  message;
    }
    /**
     * 插入数据
     */
    public  void insert(SQLiteDatabase db, String createTime, String tsId, String imgUrl,int isRead) {
        String sql = "insert into statusInfo " + "(createTime,tsId,imgUrl,isRead) values ('"+createTime+"','" + tsId + "','" + imgUrl + "'," + isRead + ")";
        db.execSQL(sql);
    }
    /**
     * 查询所有数据数据,获取列表信息
     * @param  db 数据库
     */
    public List<StatusInfoVo> getStatusInformVos(SQLiteDatabase db,String tsId){
        List<StatusInfoVo> statusInfoVos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from statusInfo where tsId = '" + tsId +"' order by createTime desc ",null);
        int createTimeIndex = cursor.getColumnIndex("createTime");
        int imgUrlIndex= cursor.getColumnIndex("imgUrl");
        int tsIdIndex= cursor.getColumnIndex("tsId");
        int isReadIndex =cursor.getColumnIndex("isRead");
        int uidIndex = cursor.getColumnIndex("uid");
        while (cursor.moveToNext()){
            StatusInfoVo statusInfoVo = new StatusInfoVo();
            String createTime = cursor.getString(createTimeIndex);
            String imgUrl = cursor.getString(imgUrlIndex);
            String mtsId = cursor.getString(tsIdIndex);
            int uid = cursor.getInt(uidIndex);
            int isRead = cursor.getInt(isReadIndex);
            statusInfoVo.setCreateTime(createTime);
            statusInfoVo.setImgUrl(imgUrl);
            statusInfoVo.setTsId(mtsId);
            statusInfoVo.setUid(uid);
            statusInfoVo.setIsRead(isRead);
            statusInfoVos.add(statusInfoVo);
        }
        return statusInfoVos;
    }
    /**
     * 获取没有阅读的消息数量
     * @param  db 数据库
     */
    public int getNoReadcount(SQLiteDatabase db,String tsId){
        int count = 0;
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db, tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            for (int i = 0; i < statusInfoVos.size(); i++) {
                StatusInfoVo statusInfoVo = statusInfoVos.get(i);
                if (statusInfoVo.getIsRead() == 0) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * 获取没有阅读的消息的时间
     * @param  db 数据库
     */
    public ArrayList<String>  getNoreadTime(SQLiteDatabase db,String tsId){
        ArrayList<String> timeList = new ArrayList<>();
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db,tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            for (int i = 0; i < statusInfoVos.size(); i++) {
                StatusInfoVo statusInfoVo = statusInfoVos.get(i);
                if (statusInfoVo.getIsRead() == 0) {
                    timeList.add(statusInfoVo.getCreateTime());
                }else {
                    timeList.add(statusInfoVo.getCreateTime());
                    return timeList;
                }
            }
        }
        return timeList;
    }
    /**
     * 更新所有阅读的消息状态
     * @param  db 数据库
     */
    public void updateAllread(SQLiteDatabase db,String tsId){
            List<StatusInfoVo> statusInfoVos= getStatusInformVos(db,tsId);
            if (statusInfoVos.size()>0 && statusInfoVos!=null) {
                for (int i = 0; i < statusInfoVos.size(); i++) {
                    StatusInfoVo statusInfoVo = statusInfoVos.get(i);
                    if (statusInfoVo.getIsRead() == 0) {
                        //修改所有的未读新消息为已读
                        update(db,statusInfoVo.getUid(),1);
                    }
                }
            }
    }
    /**
     * 获取没有阅读的消息数量
     * @param  db 数据库
     */
    public int getcount(SQLiteDatabase db,String tsId){
        int count = 0;
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db,tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            count = statusInfoVos.size();
           return count;
        }
        return count;
    }
    /**
     * 最近一次没有阅读的消息头像
     * @param  db 数据库
     */
    public List<String> getTsIds(SQLiteDatabase db,String tsId){
        List<String> tsIds = new ArrayList<>();
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db, tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            for (int i = 0; i < statusInfoVos.size(); i++) {
                StatusInfoVo statusInfoVo = statusInfoVos.get(i);
                tsIds.add(statusInfoVo.getTsId());
            }
        }
        return tsIds;
    }
    /**
     * 最近一次没有阅读的消息头像
     * @param  db 数据库
     */
    public String getLatestUrl(SQLiteDatabase db,String tsId){
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db, tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            for (int i = 0; i < statusInfoVos.size(); i++) {
                StatusInfoVo statusInfoVo = statusInfoVos.get(i);
                if (statusInfoVo.getIsRead()==0&&i==0) {
                    return statusInfoVo.getImgUrl();
                }
            }
        }
        return null;
    }
    /**
     * 修改数据
     * @param  db 数据库
     * @param  id 数据id
     * @param  isRead 是否阅读
     */
    public  void  update(SQLiteDatabase db,int id,int isRead){
        String sql = "update statusInfo " + " set isRead =" + isRead + " where uid = " + id;
        db.execSQL(sql);
    }
    /**
     * 判断是否数据是否为空
     * @param  db 数据库
     */
    public   boolean  isEmpty(SQLiteDatabase db){
        Cursor cursor  =db.rawQuery("select * from statusInfo",null);
        while (cursor.moveToNext()){
            return false;
        }
        return true;
    }

    /**
     *  删除超过20条时记录
     * @param  db 数据库
     */
    public  void deleteOverTime(SQLiteDatabase db,String tsId){
        List<StatusInfoVo> statusInfoVos= getStatusInformVos(db,tsId);
        if (statusInfoVos.size()>0 && statusInfoVos!=null) {
            for (int i = 0; i < statusInfoVos.size(); i++) {
                if(i>19 && statusInfoVos.get(i).getIsRead()==1){
                    deleteByTsId(db,statusInfoVos.get(i).getUid());
                }
            }
        }
    }
    /**
     *删除通过id号删除单个系统消息
     * @param  db 数据库
     * @param id 唯一用户id号
     */
    public void  deleteByTsId(SQLiteDatabase db,int id){
        String sql="delete from statusInfo where uid =" + id;
        db.execSQL(sql);
    }
    /**
     * 删除所有
     * @param db
     */
    public  void delete(SQLiteDatabase db){
        String sql="delete from statusInfo";
        db.execSQL(sql);
        G.log(isEmpty(db)+"---是否删除数据库成功");
    }

}

