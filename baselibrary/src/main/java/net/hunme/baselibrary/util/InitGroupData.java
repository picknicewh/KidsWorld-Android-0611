package net.hunme.baselibrary.util;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.GroupJson;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

/**
 * 作者： Administrator
 * 时间： 2016/8/16
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class InitGroupData implements OkHttpListener {
    private Context context;
    public InitGroupData(Context context){
        this.context = context;
    }
    /**
     * 获取所有班级信息
     * @param tsid
     *  获取列表类型 0 所有联系人 1 所有群信息 2 所有家长 3 所有教师
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
        Type type =new TypeToken<Result<List<GroupJson>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupJson>> data = (Result<List<GroupJson>>) date;
        if (data!=null){
            List<GroupJson> groupJsonList =data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
                if (RongIM.getInstance()!=null){
                    for (int i = 0 ;i<groupJsonList.size();i++){
                        GroupJson groupJson = groupJsonList.get(i);
                        final String classId = groupJson.getClassId();
                        final String groupName = groupJson.getGroupName();
                        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                            @Override
                            public Group getGroupInfo(String s) {
                                Group group = new Group(classId,groupName, Uri.parse(""));
                                return group;

                            }
                        },true);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName, Uri.parse("")));
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
