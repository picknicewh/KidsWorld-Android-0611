package net.hongzhang.baselibrary.contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

/**
 * 作者： wh
 * 时间： 2016/8/16
 * 名称：获取所有群组信息，并保存到数据库
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GetGroupData implements OkHttpListener {
    private Context context;
    private SQLiteDatabase database;
    private GroupsDbHelper groupsDbHelper;
    public GetGroupData(Context context){
        this.context = context;
        GroupDb groupDb = new GroupDb(context);
        database  = groupDb.getWritableDatabase();
        groupsDbHelper = GroupsDbHelper.getinstance();
        if (groupsDbHelper.isEmpty(database)){
            groupsDbHelper.delete(database);
        }
    }
    /**
     * 获取所有班级信息
     * @param tsid
     *  获取列表类型 4= 所有联系人 1 =所有群信息 2 =所有家长 3 =所有教师
     */
    public    void getGroupList(String tsid){
        if(G.isEmteny(tsid)){
            //用户没登录或者退出账号 打开App无需去请求
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", tsid);
        //1=群，2=老师，3=家长
        params.put("type",1);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
            List<GroupInfoVo> groupJsonList =data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
                if (RongIM.getInstance()!=null){
                    for (int i = 0 ;i<groupJsonList.size();i++){
                        GroupInfoVo groupJson = groupJsonList.get(i);
                        final String classId = groupJson.getClassId();
                        final String groupName = groupJson.getGroupName();
                        groupsDbHelper.insert(database,groupName,classId,0,0);
                         RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                             @Override
                             public Group getGroupInfo(String s) {
                                 Group group = new   Group(classId,groupName,null);
                                 return group;
                             }
                         },true);
                        if (classId!=null && groupName!=null ){
                            RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName,null));
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
