package net.hunme.message.ronglistener;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.contract.GroupDb;
import net.hunme.baselibrary.contract.GroupsDbHelper;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.bean.GroupInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;

/**
 * 作者： wh
 * 时间： 2016/9/14
 * 名称：接收消息监听
 * 版本说明：
 * 附加注释：
 * 主要接口：获取群消息
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener, OkHttpListener {
    private Activity activity;
    private GroupDb groupDb;
    private GroupsDbHelper helper;
    public  MyReceiveMessageListener(Activity activity){
       this.activity =activity;
        groupDb = new GroupDb(activity);
        helper = new GroupsDbHelper();
    }
    /**
     * 收到消息的处理。
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        Conversation.ConversationType type = message.getConversationType();
        String name =message.getContent().getUserInfo().getName();
        boolean isCreate=false;
        List<String> nameList = helper.getGroupNames(groupDb.getWritableDatabase());
        for (int i = 0;i<nameList.size();i++){
            String myname = nameList.get(i);
            if (!myname.equals(name)){
                isCreate =true;
            }else {
                isCreate =false;
            }
        }
        if (type.equals(Conversation.ConversationType.GROUP)&&isCreate){
            getClassinfor();
        }
        return true;
    }
    /**
     * 获取所有班级信息
     */
    private  void getClassinfor(){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(activity).getTsId());
        //1=群，2=老师，3=家长
        params.put("type",1);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this,2,"contract_class");
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
            List<GroupInfoVo> groupJsonList =data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
               for (int i = 0 ; i <groupJsonList.size();i++){
                   GroupInfoVo groupJson = groupJsonList.get(i);
                   final String classId = groupJson.getClassId();
                   final String groupName = groupJson.getGroupName();
                   helper.insert(groupDb.getWritableDatabase(),groupName,classId,0,0);
                   if (RongIM.getInstance()!=null){
                       RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                           @Override
                           public Group getGroupInfo(String s) {
                               if (s.equals(classId)){
                                   Group group = new Group(classId,groupName, Uri.parse(""));
                                   return group;
                               }
                               return null;
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
        Toast.makeText(activity,error,Toast.LENGTH_SHORT).show();
    }
}
