package net.hunme.message.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.hunme.baselibrary.activity.BaseFragement;
import net.hunme.message.MessageApplication;
import net.hunme.message.R;
import net.hunme.message.activity.ParentActivity;
import net.hunme.message.ronglistener.MyConversationBehaviorListener;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/7/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MessageFragement extends BaseFragement implements View.OnClickListener{

    private ImageView iv_search;
    /**
     * 班级
     */
    private LinearLayout ll_class;
    /**
     * 老师
     */
    private LinearLayout ll_teacher;
    /**
     * 家长
     */
    private LinearLayout ll_parent;
    /**
     * 保存的用户名
     */
    private String username;
    /**
     * userId
     */
    private String userId;

    private String portrait;

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
        ll_class = $(v,R.id.ll_class);
        ll_teacher = $(v,R.id.ll_teacher);
        ll_parent = $(v,R.id.ll_parent);
        initframent();
        ll_parent.setOnClickListener(this);
        ll_teacher.setOnClickListener(this);
        ll_class.setOnClickListener(this);

   }
    private void  initframent(){
        connect("V5tYQjjmYQGGUT5RP9YyZ0bso9ndFkPYvochz2Gw7s692q5Oy6+dsfcJT13ag45+j9HeWAqVtz/T0ApFSaea8Q==");
        Log.i("TAFGG","cfdfefdfedfdf");
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
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getActivity().getApplicationInfo().packageName.equals(MessageApplication.getCurProcessName(getActivity()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    if (RongIM.getInstance() != null) {
                      /*  Log.i("wang","userid:"+userid);
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, username, Uri.parse(portrait)));
                        RongIM.getInstance().setMessageAttachedUserInfo(true);*/
                    }
                }
                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
    /**
     * 未读消息监听
     */
   private void setNoreadMessage(){
       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, Conversation.ConversationType.PRIVATE);
           }
       }, 500);
   }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId()==R.id.ll_class){

        }else if (v.getId()==R.id.ll_teacher){
            intent.setClass(getActivity(), ParentActivity.class);
            intent.putExtra("title","教师");
            startActivity(intent);
        }else if (v.getId()==R.id.ll_parent){
            intent.setClass(getActivity(), ParentActivity.class);
            intent.putExtra("title","家长");
            startActivity(intent);
        }else if (v.getId()==R.id.iv_search){

        }

    }
    /**
     * 监听处理
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
