package net.hunme.message.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.hunme.baselibrary.base.BaseFragement;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.message.activity.ClassActivity;
import net.hunme.message.activity.ParentActivity;
import net.hunme.message.activity.SearchActivity;
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
    private ImageView iv_search;
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
     * 保存的用户名
     */
    private String username;
    /**
     * userId
     */
    private String userId;

    private String portrait;
    /**
     * 用户信息
     */
    private  UserMessage userMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        init(view);
        setNoreadMessage();
        // 设置点击头像监听事件
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        return view;
    }
   private  void init(View v){
       iv_search = $(v,R.id.iv_search);
        iv_class = $(v,R.id.iv_class);
        iv_teacher = $(v,R.id.iv_teacher);
        iv_parent = $(v,R.id.iv_parent);
        initframent();
        iv_parent.setOnClickListener(this);
        iv_teacher.setOnClickListener(this);
        iv_class.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        userMessage = new UserMessage(getActivity());
        username = userMessage.getUserName();
        userId = userMessage.getTsId();
        portrait = userMessage.getHoldImgUrl();
   }

    /**
     *获取聊天列表
     */
    private void  initframent(){
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }


    /**
     * 未读消息监听
     */
   private void setNoreadMessage(){
       Handler handler = new Handler();
       final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
               Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
               Conversation.ConversationType.PUBLIC_SERVICE};
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener,conversationTypes);
           }
       }, 500);
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
        }else if (v.getId()==R.id.iv_search){
            intent.setClass(getActivity(), SearchActivity.class);
            startActivity(intent);
        }
    }
    /**
     * 发送广播通知改变主界面的圆点的显示状态
     */
    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
                Intent intent = new Intent();
                intent.setAction("net.hunme.message.showdos");
                intent.putExtra("count",count);
               if (getActivity()!=null){
                  getActivity().sendBroadcast(intent);
               }
        }
    };


}
