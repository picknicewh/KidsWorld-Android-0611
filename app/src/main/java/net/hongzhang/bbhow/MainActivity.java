package net.hongzhang.bbhow;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.fragment.DiscoveryFragement;
import net.hongzhang.login.util.UserAction;
import net.hongzhang.message.fragment.MessageFragement;
import net.hongzhang.school.SchoolFragement;
import net.hongzhang.status.StatusFragement;

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
     * 园所圆点
     */
    @Bind(R.id.tv_dos_school)
    TextView tvDosSchool;
    /**
     * 底部tab
     */
    @Bind(R.id.ll_tab)
    LinearLayout llTab;
//    @Bind(R.id.content)
//    FrameLayout content;

//    private FragmentManager fragmentManager;
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
    /**
     * 判断网络是否连接
     */
    public static boolean isconnect;
    public static int count;
    /**
     * 所有的广播
     */
    private MyBroadcasReceiver myBroadcasReceiver;
    private boolean isQuit = false;
    private Timer timer;
    private FragmentTransaction ft;
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserAction.isGoLogin(MainActivity.this, this);
        BaseLibrary.addActivity(this);
        setContentView(R.layout.activity_main);
        initData();
        initViewpager();
        Log.i("EEE",userMessage.getTsId());

    }
    private void initData(){
        userMessage = UserMessage.getInstance(this);
        ButterKnife.bind(this);
        timer = new Timer();
        if (null != userMessage.getUserName()) {
            //初始极光推送配置信息
            initJPushConfiguration(userMessage.getLoginName(), "tag");
        }
        //如果网络连接时，连接融云
        if (G.isNetworkConnected(this)) {
            BaseLibrary.connect(userMessage.getRyId(), MainActivity.this, userMessage.getUserName(), userMessage.getHoldImgUrl());
            setNoreadMessage();
        }
        // 账号抢登监听
        if (RongIM.getInstance()!=null){
            Log.i("jjj","=====================");
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        }
     //   RongIM.setConnectionStatusListener(new MyConnectionStatusListener(MainActivity.this));
        registerReceiver();
        initCount();
    }
    /**
     * 初始化viewpager
     */
    private void initViewpager() {
        statusFragement = new StatusFragement();
        schoolFragement = new SchoolFragement();
        discoveryFragement = new DiscoveryFragement();
        messageFragement = new MessageFragement();
        ft = getSupportFragmentManager().beginTransaction();//获取FragmentTransaction 实例
        ft.replace(R.id.content, discoveryFragement); //使用DetailsFragment 的实例
        ft.commit();
        mContent = discoveryFragement;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if ( G.KisTyep.isChooseId ) {
            initViewpager();
            G.KisTyep.isChooseId = false;
        }
        if (G.KisTyep.isUpadteHold){
            initViewpager();
            G.KisTyep.isUpadteHold  = false;
        }
        setNoreadMessage();
    }

    /**
     * 通过点击事件改变tab
     */
    @OnClick({R.id.ll_status, R.id.ll_school, R.id.ll_discovery, R.id.ll_message})
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.ll_discovery:
                flag = 0;
                fragment = discoveryFragement;
                break;
            case R.id.ll_school:
                flag = 1;
                fragment = schoolFragement;
                break;
            case R.id.ll_status:
                flag = 2;
                fragment = statusFragement;
                break;
            case R.id.ll_message:
                flag = 3;
                fragment = messageFragement;
                break;
        }
        switchContent(fragment);
        if (isconnect && count % 2 == 0) {
            initViewpager();
            BaseLibrary.connect(userMessage.getRyId(), this, userMessage.getUserName(), userMessage.getHoldImgUrl());
            count = 1;
        }
//        viewPager.setCurrentItem(flag, false);
        setBaseBar(flag);
    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) // 先判断是否被add过
                transaction.hide(mContent).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            else
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            mContent = to; //重新赋值
        }
    }

    /**
     * 注册监听网络广播广播
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstant.MAINSTATUSDOS);
        filter.addAction(BroadcastConstant.HIDEMAINTAB);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(BroadcastConstant.MAINSCHOOLDOS);
        myBroadcasReceiver = new MyBroadcasReceiver();
        this.registerReceiver(myBroadcasReceiver, filter);

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
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && flag != 0) {
            //这里处理逻辑代码
            if (isQuit) {
                // 这是两次点击以后
                if (myflag == flag) {
                    timer.cancel();
                    BaseLibrary.exit();
                } else {
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
    private void pressBackOneTime() {
        isQuit = true;
        myflag = flag;
        Toast.makeText(this.getApplicationContext(), "再按一次返回到桌面", Toast.LENGTH_SHORT).show();
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
        tvDiscovery.setTextColor(getResources().getColor(R.color.main_text_grey));
        tvMessage.setTextColor(getResources().getColor(R.color.main_text_grey));
        tvSchool.setTextColor(getResources().getColor(R.color.main_text_grey));
        tvStatus.setTextColor(getResources().getColor(R.color.main_text_grey));
        ivSchool.setImageResource(R.mipmap.school);
        ivDiscovery.setImageResource(R.mipmap.discovery);
        ivMessage.setImageResource(R.mipmap.message);
        ivStatus.setImageResource(R.mipmap.status);
        switch (chooseType) {
            case 0:
                tvDiscovery.setTextColor(getResources().getColor(R.color.main_text_green));
                ivDiscovery.setImageResource(R.mipmap.discovery_p);
                break;
            case 1:
                tvSchool.setTextColor(getResources().getColor(R.color.main_text_green));
                ivSchool.setImageResource(R.mipmap.school_p);
                break;
            case 2:
                tvStatus.setTextColor(getResources().getColor(R.color.main_text_green));
                ivStatus.setImageResource(R.mipmap.status_p);
                break;
            case 3:
                tvMessage.setTextColor(getResources().getColor(R.color.main_text_green));
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
     * 全屏播放视频隐藏底部tab广播
     * 接收动态小红点的广播
     * 注册监听网络广播广播  ConnectivityManager.CONNECTIVITY_ACTION
     */
    public class MyBroadcasReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                if (action.equals(BroadcastConstant.HIDEMAINTAB)) {
                    if (bundle.getBoolean("isVisible", false)) {
                        llTab.setVisibility(View.VISIBLE);
                    } else {
                        llTab.setVisibility(View.GONE);
                    }
                } else if (action.equals(BroadcastConstant.MAINSTATUSDOS)) {
                    if (bundle.getInt("count", 0) == 1) {
                        tvStatusDos.setVisibility(View.VISIBLE);
                    } else {
                        tvStatusDos.setVisibility(View.GONE);
                    }
                }else if (action.equals(BroadcastConstant.MAINSCHOOLDOS)) {
                    if (bundle.getInt("count", 0)==1) {
                        tvDosSchool.setVisibility(View.VISIBLE);
                    } else {
                        tvDosSchool.setVisibility(View.GONE);
                    }
                } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                        isconnect = false;
                        //断网
                        count++;
                    } else {
                        //连上网络
                        isconnect = true;
                    }
                }
            }
        }
    }
}



