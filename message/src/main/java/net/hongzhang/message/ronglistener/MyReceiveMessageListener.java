package net.hongzhang.message.ronglistener;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.message.bean.IMessage;

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
        //新创建一个群组，从新获取所有
        if (message.getContent().getUserInfo()==null){
            Gson g = new Gson();
            IMessage m = g.fromJson(g.toJson(message),IMessage.class);

            if (m!=null){
                Conversation.ConversationType type = m.getConversationType();
                String content = m.getContent().getMessage();
                regetData(type);
                String userId =m.getTargetId();
                if (content.equals("该群已被群主解散")){
                    removeConveration(Conversation.ConversationType.GROUP,userId);
                }else {
                    String extra = m.getContent().getExtra();
                    if (extra.contains(UserMessage.getInstance(activity).getTsId())){
                        removeConveration(Conversation.ConversationType.GROUP,userId);
                    }
                }
            }
            return true;
        }else {

            return false;
        }

    }
    /**
     *重新获取所有群信息
     *
     * @param type 会话类型
     */
    private void regetData( Conversation.ConversationType type ){
        if (type.equals(Conversation.ConversationType.GROUP)){
            getClassinfor();
        }
    }
    private void removeConveration(Conversation.ConversationType type ,String targetId){
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().removeConversation(type,targetId, new RongIMClient.ResultCallback<Boolean>(){
                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (aBoolean){
                        Log.i("TAG","移除成功！");
                    }else {
                        Log.i("TAG","移除失败！");
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("TAG",errorCode.getMessage());
                }
            });
        }
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
                if (helper.isEmpty(groupDb.getWritableDatabase())){
                    helper.delete(groupDb.getWritableDatabase());
                }
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
