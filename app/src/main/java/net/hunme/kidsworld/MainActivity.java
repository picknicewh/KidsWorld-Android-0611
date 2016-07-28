package net.hunme.kidsworld;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.util.UserMessage;
import net.hunme.discovery.DiscoveryFragement;
import net.hunme.kidsworld.util.HunmeApplication;
import net.hunme.login.util.UserAction;
import net.hunme.message.fragment.MessageFragement;
import net.hunme.school.SchoolFragement;
import net.hunme.status.StatusFragement;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import main.jpushlibrary.JPush.JPushBaseActivity;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：app--首页
 * 版本说明：
 * 附加注释：继承了极光推送页面，初始化极光的设置,其中ShwoMessageReceiver为通讯未读消息个数接收广播，当未读消息不为0时，显示小圆点，否则不显示
 * 主要接口：
 */
public class MainActivity extends JPushBaseActivity {
    private static final String SHOWDOS = "net.hunme.message.showdos";
    /**
     * 通讯圆点
     */
    @Bind(R.id.tv_dos_message)
    TextView tvMeaasgeDos;
    /**
     * 动态圆点
     */
    @Bind(R.id.tv_dos_status)
    TextView tvStatusDos;
    private FragmentManager fragmentManager;
    /**
     * 动态
     */
    private StatusFragement statusFragement;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.iv_status)
    ImageView ivStatus;
    /**
     * 学校
     */
    private SchoolFragement schoolFragement;

    @Bind(R.id.tv_school)
    TextView tvSchool;
    @Bind(R.id.iv_school)
    ImageView ivSchool;
    /**
     * 乐园
     */
    private DiscoveryFragement discoveryFragement;
    @Bind(R.id.tv_discovery)
    TextView tvDiscovery;
    @Bind(R.id.iv_discovery)
    ImageView ivDiscovery;
    /**
     * 通讯
     */
    private MessageFragement messageFragement;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.iv_message)
    ImageView ivMessage;

    /**
     * 用户信息
     */
    private UserMessage userMessage;
    /**
     * 保存的用户名
     */
    private String username;
    /**
     * 用户的userId
     */
    private String userId;
    /**
     * 用户头像地址
     */
    private String portrait;
    /**
     * 标记位
     */
    private int flag = 0;

    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserAction.isGoLogin(MainActivity.this,this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initJPushConfiguration();
        connect(userMessage.getRyId());
        setNoreadMessage();

    }

    /**
     * 初始化数据
     */
    private void init() {
        statusFragement = new StatusFragement();
        schoolFragement = new SchoolFragement();
        discoveryFragement = new DiscoveryFragement();
        messageFragement = new MessageFragement();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
      /*
        transaction.replace(R.id.content, statusFragement);
        transaction.commit();*/
        userMessage  = new UserMessage(this);
        userId = userMessage.getTsId();
        username  = userMessage.getUserName();
        portrait = userMessage.getHoldImgUrl();
        fragmentList = new ArrayList<>();
        fragmentList.add(statusFragement);
        fragmentList.add(schoolFragement);
        fragmentList.add(discoveryFragement);
        fragmentList.add(messageFragement);
        for (int i = 0 ; i <fragmentList.size() ;i++){
            transaction.add(R.id.content, fragmentList.get(i));
        }
        showview(flag,transaction);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNoreadMessage();
    }

    /**
     * 通过点击事件改变tab
     */
    @OnClick({R.id.ll_status, R.id.ll_school, R.id.ll_discovery, R.id.ll_message})
    public void onClick(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.ll_status:
                tvDiscovery.setTextColor(getResources().getColor(R.color.default_grey));
                tvMessage.setTextColor(getResources().getColor(R.color.default_grey));
                tvSchool.setTextColor(getResources().getColor(R.color.default_grey));
                tvStatus.setTextColor(getResources().getColor(R.color.main_green));
                ivSchool.setImageResource(R.mipmap.school);
                ivDiscovery.setImageResource(R.mipmap.discovery);
                ivMessage.setImageResource(R.mipmap.message);
                ivStatus.setImageResource(R.mipmap.status_p);
                flag=0;
                break;
            case R.id.ll_school:
                tvDiscovery.setTextColor(getResources().getColor(R.color.default_grey));
                tvMessage.setTextColor(getResources().getColor(R.color.default_grey));
                tvSchool.setTextColor(getResources().getColor(R.color.main_green));
                tvStatus.setTextColor(getResources().getColor(R.color.default_grey));
                ivSchool.setImageResource(R.mipmap.school_p);
                ivDiscovery.setImageResource(R.mipmap.discovery);
                ivMessage.setImageResource(R.mipmap.message);
                ivStatus.setImageResource(R.mipmap.status);
                flag=1;
                break;
            case R.id.ll_discovery:
                tvDiscovery.setTextColor(getResources().getColor(R.color.main_green));
                tvMessage.setTextColor(getResources().getColor(R.color.default_grey));
                tvSchool.setTextColor(getResources().getColor(R.color.default_grey));
                tvStatus.setTextColor(getResources().getColor(R.color.default_grey));
                ivSchool.setImageResource(R.mipmap.school);
                ivDiscovery.setImageResource(R.mipmap.discovery_p);
                ivMessage.setImageResource(R.mipmap.message);
                ivStatus.setImageResource(R.mipmap.status);
                flag=2;
                break;
            case R.id.ll_message:
                tvDiscovery.setTextColor(getResources().getColor(R.color.default_grey));
                tvMessage.setTextColor(getResources().getColor(R.color.main_green));
                tvSchool.setTextColor(getResources().getColor(R.color.default_grey));
                tvStatus.setTextColor(getResources().getColor(R.color.default_grey));
                ivSchool.setImageResource(R.mipmap.school);
                ivDiscovery.setImageResource(R.mipmap.discovery);
                ivMessage.setImageResource(R.mipmap.message_p);
                ivStatus.setImageResource(R.mipmap.status);
                flag=3;
                break;
          }
              showview(flag,transaction);

    }
    /**
     显示隐藏界面
     */
    private void showview(int flag,FragmentTransaction transaction){
        for (int i = 0; i<fragmentList.size();i++){
            if (flag==i){
                transaction.show(fragmentList.get(flag));
            }else {

                transaction.hide(fragmentList.get(i));
            }
        }
        transaction.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(HunmeApplication.getCurProcessName(this))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {Log.d("LoginActivity", "--onTokenIncorrect");}
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, username, Uri.parse(portrait)));
                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {Log.d("LoginActivity", "--onError" + errorCode);}
            });
        }
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
    /**
     * 发送广播通知改变主界面的圆点的显示状态
     */
    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count > 0) {
                tvMeaasgeDos.setVisibility(View.VISIBLE);
            } else {
                tvMeaasgeDos.setVisibility(View.GONE);
            }
        }
    };
}



