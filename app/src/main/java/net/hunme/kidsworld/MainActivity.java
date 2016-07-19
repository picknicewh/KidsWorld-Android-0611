package net.hunme.kidsworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.discovery.DiscoveryFragement;
import net.hunme.message.fragment.MessageFragement;
import net.hunme.school.SchoolFragement;
import net.hunme.status.StatusFragement;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import main.jpushlibrary.JPush.JPushBaseActivity;


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
    /**
     * 通讯
     */
    private MessageFragement messageFragement;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    /**
     * 消息广播
     */
    private ShwoMessageReceiver mBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        registerBoradcastReceiver();
        initJPushConfiguration();
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
        transaction.replace(R.id.content, statusFragement);
        transaction.commit();
        mBroadcastReceiver = new ShwoMessageReceiver();
    }

    @OnClick({R.id.ll_status, R.id.ll_school, R.id.ll_discovery, R.id.ll_message})
    public void onClick(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.ll_status:
                fragment = statusFragement;
                tvDiscovery.setTextColor(getResources().getColor(R.color.black));
                tvMessage.setTextColor(getResources().getColor(R.color.black));
                tvSchool.setTextColor(getResources().getColor(R.color.black));
                tvStatus.setTextColor(getResources().getColor(R.color.red));
                ivSchool.setImageResource(R.mipmap.school);
                break;
            case R.id.ll_school:
                fragment = schoolFragement;
                tvDiscovery.setTextColor(getResources().getColor(R.color.black));
                tvMessage.setTextColor(getResources().getColor(R.color.black));
                tvSchool.setTextColor(getResources().getColor(R.color.red));
                tvStatus.setTextColor(getResources().getColor(R.color.black));
                ivSchool.setImageResource(R.mipmap.school_p);
                break;
            case R.id.ll_discovery:
                fragment = discoveryFragement;
                tvDiscovery.setTextColor(getResources().getColor(R.color.red));
                tvMessage.setTextColor(getResources().getColor(R.color.black));
                tvSchool.setTextColor(getResources().getColor(R.color.black));
                tvStatus.setTextColor(getResources().getColor(R.color.black));
                ivSchool.setImageResource(R.mipmap.school);
                break;
            case R.id.ll_message:
                fragment = messageFragement;
                tvDiscovery.setTextColor(getResources().getColor(R.color.black));
                tvMessage.setTextColor(getResources().getColor(R.color.red));
                tvSchool.setTextColor(getResources().getColor(R.color.black));
                tvStatus.setTextColor(getResources().getColor(R.color.black));
                ivSchool.setImageResource(R.mipmap.school);
                break;
        }
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * 注册广播
     */
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(SHOWDOS);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
    /**
     * 对消息的圆点处理广播
     */
    private class ShwoMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SHOWDOS)) {
                int count = intent.getIntExtra("count",0);
                if (count>0){
                    tvMeaasgeDos.setVisibility(View.VISIBLE);
                }else {
                    tvMeaasgeDos.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver!=null){
            unregisterReceiver(mBroadcastReceiver);
        }
    }

}



