package net.hunme.kidsworld;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.NoScrollViewPager;
import net.hunme.discovery.DiscoveryFragement;
import net.hunme.kidsworld.util.ConnectionChangeReceiver;
import net.hunme.kidsworld.util.HunmeApplication;
import net.hunme.kidsworld.util.MyViewPagerAdapter;
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
    @Bind(R.id.vp_main)
    NoScrollViewPager viewPager;
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
    /**
     * 有无网络请求
     */
    ConnectionChangeReceiver myReceiver;

    /**
     *
     */
     public  static  boolean isconnect;
    /**
     *
     */
    public  static int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  UserAction.isGoLogin(MainActivity.this,this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewpager();
        //初始激光推送配置信息
        initJPushConfiguration();
        //如果网络连接时，连接融云
        if (G.isNetworkConnected(this)){
            connect(userMessage.getRyId());
            setNoreadMessage();
        }
        registerReceiver();
        initCount();

    }
    /**
     * 初始化viewpager
     */
   private void  initViewpager(){
       userMessage = new UserMessage(this);
       userId = userMessage.getTsId();
       username = userMessage.getUserName();
       portrait = userMessage.getHoldImgUrl();
       statusFragement = new StatusFragement();
       schoolFragement = new SchoolFragement();
       discoveryFragement = new DiscoveryFragement();
       messageFragement = new MessageFragement();
       fragmentManager = getSupportFragmentManager();
       fragmentList = new ArrayList<>();
       fragmentList.add(statusFragement);
       fragmentList.add(schoolFragement);
       fragmentList.add(discoveryFragement);
       fragmentList.add(messageFragement);
       MyViewPagerAdapter adapter = new MyViewPagerAdapter(fragmentManager,fragmentList);
       viewPager.setAdapter(adapter);
       viewPager .setOffscreenPageLimit(3);
       viewPager.setPagingEnabled(false);
   }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG","=================onResume===================");
        if (G.isNetworkConnected(this)){
            initViewpager();
        }
        setNoreadMessage();
    }
    /**
     * 通过点击事件改变tab
     */
    @OnClick({R.id.ll_status, R.id.ll_school, R.id.ll_discovery, R.id.ll_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_status:flag=0;break;
            case R.id.ll_school:flag=1;break;
            case R.id.ll_discovery:flag=2;break;
            case R.id.ll_message:flag=3;break;
        }
        if (isconnect && count%2==0){
            initViewpager();
            connect(userMessage.getRyId());
            count=1;
        }
        viewPager.setCurrentItem(flag,false);
        setBaseBar(flag);
    }
    /**
     * 注册广播
     */
    private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myReceiver);
        Log.i("TAG","=================onDestroy===================");
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

    /**
     *  设置底部颜色状态
     * @param chooseType
     */
    private void setBaseBar(int chooseType){
        tvDiscovery.setTextColor(getResources().getColor(R.color.default_grey));
        tvMessage.setTextColor(getResources().getColor(R.color.default_grey));
        tvSchool.setTextColor(getResources().getColor(R.color.default_grey));
        tvStatus.setTextColor(getResources().getColor(R.color.default_grey));
        ivSchool.setImageResource(R.mipmap.school);
        ivDiscovery.setImageResource(R.mipmap.discovery);
        ivMessage.setImageResource(R.mipmap.message);
        ivStatus.setImageResource(R.mipmap.status);
        switch (chooseType){
            case 0:
                tvStatus.setTextColor(getResources().getColor(R.color.main_green));
                ivStatus.setImageResource(R.mipmap.status_p);
                break;
            case 1:
                tvSchool.setTextColor(getResources().getColor(R.color.main_green));
                ivSchool.setImageResource(R.mipmap.school_p);
                break;
            case 2:
                tvDiscovery.setTextColor(getResources().getColor(R.color.main_green));
                ivDiscovery.setImageResource(R.mipmap.discovery_p);
                break;
            case 3:
                tvMessage.setTextColor(getResources().getColor(R.color.main_green));
                ivMessage.setImageResource(R.mipmap.message_p);
                break;
        }
    }
    /**
     * 如果count的值为0时表示一进来就是断网的，此时点击tab重新连接加载viewpager
     * 如果count的值是为1时表示有网络状态，一进来就已经加载刷新数据了，不需要重新加载
     * 如果count的值为2时表示断了一次网络再次连接网络，此时要重新加载数据
     * 初始化是否有网络的值，如果进应用程序时时断网的设置此时的count值为-1(因为如果断网的话监听到断网，此时
     * count会加1)
     */
    private void  initCount(){
        if (G.isNetworkConnected(this)){
           count=1;
        }else {
            count=-1;
        }
    }
}



