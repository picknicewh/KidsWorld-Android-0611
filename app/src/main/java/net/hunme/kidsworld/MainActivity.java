package net.hunme.kidsworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.cordova.HMDroidGap;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MyConnectionStatusListener;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.NoScrollViewPager;
import net.hunme.discovery.DiscoveryFragement;
import net.hunme.kidsworld.util.MyViewPagerAdapter;
import net.hunme.login.UserChooseActivity;
import net.hunme.login.util.UserAction;
import net.hunme.message.fragment.MessageFragement;
import net.hunme.school.SchoolFragement;
import net.hunme.status.StatusFragement;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
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
    /**
     * 底部tab
     */
    @Bind(R.id.ll_tab)
    LinearLayout llTab;

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
     * 标记位
     */
    private int flag = 0;

    private List<Fragment> fragmentList;

    /**
     * 判断网络是否连接
     */
    public static boolean isconnect;
    public static int count;
    /**
     * 接收动态小红点的广播
     */
   // private MyStatusDosShowReceiver myStatusDosShowReceiver;
    /**
     *所有的广播
     */
    private MyBroadcasReceiver myBroadcasReceiver;
    private  boolean isQuit = false;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserAction.isGoLogin(MainActivity.this, this);
        BaseLibrary.addActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        timer  = new Timer();
        initViewpager();
        if(null!=userMessage.getUserName()){
            //初始极光推送配置信息
            initJPushConfiguration(userMessage.getLoginName(), "tag");
        }
        //如果网络连接时，连接融云
        if (G.isNetworkConnected(this)) {
            BaseLibrary.connect(userMessage.getRyId(), MainActivity.this, userMessage.getUserName(), userMessage.getHoldImgUrl());
            setNoreadMessage();
        }
        RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        registerReceiver();
        initCount();
    }
    /**
     * 初始化viewpager
     */
    private void initViewpager() {
        userMessage = UserMessage.getInstance(this);
        statusFragement = new StatusFragement();
        schoolFragement = new SchoolFragement();
        discoveryFragement = new DiscoveryFragement();
        messageFragement = new MessageFragement();
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(discoveryFragement);
        fragmentList.add(schoolFragement);
        fragmentList.add(statusFragement);
        fragmentList.add(messageFragement);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserChooseActivity.flag == 1 || HMDroidGap.flag==1) {
            initViewpager();
            UserChooseActivity.flag = 0;
            HMDroidGap.flag=0;
        }
        setNoreadMessage();
    }

    /**
     * 通过点击事件改变tab
     */
    @OnClick({R.id.ll_status, R.id.ll_school, R.id.ll_discovery, R.id.ll_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_discovery:
                flag = 0;
                break;
            case R.id.ll_school:
                flag = 1;
                break;
            case R.id.ll_status:
                flag = 2;
                break;
            case R.id.ll_message:
                flag = 3;
                break;
        }
        if (isconnect && count % 2 == 0) {
            initViewpager();
            BaseLibrary.connect(userMessage.getRyId(), this, userMessage.getUserName(), userMessage.getHoldImgUrl());
            count = 1;
        }
        viewPager.setCurrentItem(flag, false);
        setBaseBar(flag);
    }

    /**
     * 注册监听网络广播广播
     */
    private void registerReceiver() {
        IntentFilter filter3 = new IntentFilter(MyBroadcasReceiver.HIDEMAINTAB);
        IntentFilter filter2 = new IntentFilter(MyBroadcasReceiver.STATUSDOSHOW);
        IntentFilter filter1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myBroadcasReceiver = new MyBroadcasReceiver();
        this.registerReceiver(myBroadcasReceiver, filter2);
        this.registerReceiver(myBroadcasReceiver, filter3);
        this.registerReceiver(myBroadcasReceiver, filter1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcasReceiver);
    }

    /**
     * 未读消息监听
     */
    private void setNoreadMessage() {
        Handler handler = new Handler();
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
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
    private int myflag;
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && flag!=0) {
            //这里处理逻辑代码
            if (isQuit) {
                // 这是两次点击以后
                if (myflag==flag){
                    timer.cancel();
                    BaseLibrary.exit();
                }else {
                    pressBackOneTime();
                }
            } else {
               pressBackOneTime();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 按下一次物理返回键
     */
    private void pressBackOneTime(){
        isQuit = true;
        myflag = flag;
        Toast.makeText(this.getApplicationContext(), "再按一次返回到桌面",Toast.LENGTH_SHORT).show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isQuit = false;
            }
        };
        timer.schedule(task, 2000);
    }
    /**
     * 设置底部颜色状态
     *
     * @param chooseType
     */
    private void setBaseBar(int chooseType) {
        tvDiscovery.setTextColor(getResources().getColor(R.color.default_grey));
        tvMessage.setTextColor(getResources().getColor(R.color.default_grey));
        tvSchool.setTextColor(getResources().getColor(R.color.default_grey));
        tvStatus.setTextColor(getResources().getColor(R.color.default_grey));
        ivSchool.setImageResource(R.mipmap.school);
        ivDiscovery.setImageResource(R.mipmap.discovery);
        ivMessage.setImageResource(R.mipmap.message);
        ivStatus.setImageResource(R.mipmap.status);
        switch (chooseType) {
            case 0:
                tvDiscovery.setTextColor(getResources().getColor(R.color.main_green));
                ivDiscovery.setImageResource(R.mipmap.discovery_p);
                break;
            case 1:
                tvSchool.setTextColor(getResources().getColor(R.color.main_green));
                ivSchool.setImageResource(R.mipmap.school_p);
                break;
            case 2:
                tvStatus.setTextColor(getResources().getColor(R.color.main_green));
                ivStatus.setImageResource(R.mipmap.status_p);
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
    private void initCount() {
        if (G.isNetworkConnected(this)) {
            count = 1;
        } else {
            count = -1;
        }
    }

    /**
     *  全屏播放视频隐藏底部tab广播 HIDEMAINTAB
     *  接收动态小红点的广播  STATUSDOSHOW
     *  注册监听网络广播广播  ConnectivityManager.CONNECTIVITY_ACTION
     */
    public class MyBroadcasReceiver extends BroadcastReceiver {
        public static final String HIDEMAINTAB = "net.hunme.kidsworld.hideMainTabReceiver";
        public static final String STATUSDOSHOW = "net.hunme.kidsworld.MyStatusDosShowReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action  = intent.getAction();
            if (action.equals(HIDEMAINTAB)) {
                Bundle bundle = intent.getExtras();
                if (bundle.getBoolean("isVisible", false)) {
                    llTab.setVisibility(View.VISIBLE);
                } else {
                    llTab.setVisibility(View.GONE);
                }
            } else if (action.equals(STATUSDOSHOW)) {
                Bundle bundle = intent.getExtras();
                if (bundle.getInt("count", 0) == 1) {
                    tvStatusDos.setVisibility(View.VISIBLE);
                } else {
                    tvStatusDos.setVisibility(View.GONE);
                }
            }else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    isconnect = false;
                    //断网
                    count++;
                }else {
                    //连上网络
                    isconnect = true;
                }
            }
        }
    }
}



