package net.hunme.message.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.message.activity.ClassActivity;
import net.hunme.message.activity.InitContractData;
import net.hunme.message.activity.InitGroupData;
import net.hunme.message.activity.ParentActivity;
import net.hunme.message.ronglistener.MyConversationBehaviorListener;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：通讯首页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MessageFragement extends BaseFragement implements View.OnClickListener{
    /**
     * 搜索
     */
  //  private ImageView iv_search;
    /**
     * 班级
     */
    private ImageView iv_class;
    /**
     * 老师
     */
    private ImageView iv_teacher;
    /**
     * 家长
     */
    private ImageView iv_parent;
    /**
     * 用户信息
     */
    private  UserMessage userMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        init(view);
        // 设置点击头像监听事件
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
    //    RongIM.setConnectionStatusListener(new MyConnectionStatusListener(getActivity()));
        return view;
    }
    /**
     * 初始化view
     * @param  v
     */
   private  void init(View v){
     //  iv_search = $(v,R.id.iv_search);
        iv_class = $(v,R.id.iv_class);
        iv_teacher = $(v,R.id.iv_teacher);
        iv_parent = $(v,R.id.iv_parent);
        userMessage = UserMessage.getInstance(getActivity());
       // getGroupList(userMessage.getTsId(),1);
        //初始化联系人所有的头像和名字
         InitContractData data = new InitContractData(getActivity());
         data.getContractList(userMessage.getTsId());
        //初始化群主的头像和名字
         InitGroupData groupData = new InitGroupData(getActivity());
        groupData.getGroupList(userMessage.getTsId());
        initframent();
        iv_parent.setOnClickListener(this);
        iv_teacher.setOnClickListener(this);
        iv_class.setOnClickListener(this);
     //   iv_search.setOnClickListener(this);

   }
    /**
     *获取聊天列表
     */
    public void  initframent(){
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
                .build();
        fragment.setUri(uri);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId()==R.id.iv_class){
            intent.setClass(getActivity(), ClassActivity.class);
            startActivity(intent);
        }else if (v.getId()==R.id.iv_teacher){
            intent.setClass(getActivity(), ParentActivity.class);
            intent.putExtra("title","教师");
            startActivity(intent);
        }else if (v.getId()==R.id.iv_parent){
            intent.setClass(getActivity(), ParentActivity.class);
            intent.putExtra("title","家长");
            startActivity(intent);
        }/*else if (v.getId()==R.id.iv_search){
            intent.setClass(getActivity(), SearchActivity.class);
            startActivity(intent);
        }*/
    }
   /* *//**
     * 获取所有班级信息
     * @param tsid
     * @param  mtype 获取列表类型 0 所有联系人 1 所有群信息 2 所有家长 3 所有教师
     *//*
    private  void getGroupList(String tsid,int mtype){
        if(G.isEmteny(tsid)){
            //用户没登录或者退出账号 打开App无需去请求
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", tsid);
        //1=群，2=老师，3=家长
        params.put("type",mtype);
        Type type =new TypeToken<Result<List<GroupJson>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this,2,"GETGTOUP");
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
                                            if (s.equals(classId)){
                                                Group group = new Group(classId,groupName, Uri.parse(""));
                                                RongIM.getInstance().refreshGroupInfoCache(group);
                                                return group;
                                }
                                return null;
                            }
                        },true);

                    }
                 }
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Log.i("TAG",error);
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }*/
}
