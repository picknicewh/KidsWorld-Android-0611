package net.hongzhang.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.StorageUtils;

import net.hongzhang.baselibrary.activity.UpdateMessageActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PackageUtils;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.login.UserChooseActivity;
import net.hongzhang.user.R;
import net.hongzhang.user.util.CacheHelp;
import net.hongzhang.user.util.CheckUpdate;
import net.hongzhang.user.widget.OperateDialog;

import java.io.File;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户设置
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class USettingActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 修改密码
     */
    private LinearLayout ll_changepass;
    /**
     * 手机号码
     */
    private LinearLayout ll_changephone;
    /**
     * 系统消息
     */
    private LinearLayout ll_systeminfo;
    /**
     * 意见反馈
     */
    private LinearLayout ll_callback;
    /**
     * 退出账号
     */
    private LinearLayout ll_exit;
    /**
     * 清除缓存
     */
    private LinearLayout ll_cleancache;
    /**
     * 版本更新
     */
    private LinearLayout ll_checkupadte;
    /**
     * 手机号码
     */
    private TextView tv_phone;
    /**
     * 缓存值
     */
    public static TextView tv_cache;
    /**
     * 版本号
     */
    private TextView tv_version;
    private UserMessage um;
    private File path;
    private TextView tv_provsion;
    /**
     * 系统红点
     */
    private TextView tv_sysdos;
    /**
     * 系统红点显示广播
     */
    private ShowSysDosReceiver showSysDosReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        initView();
//        initDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
        setCententTitle("账户设置");
        setSubTitle("切换");
        if (UserMessage.getInstance(this).getCount()>1){
            findViewById(R.id.tv_subtitle).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_subtitle).setVisibility(View.GONE);
        }

    }
    public  static  TextView getTv_cache(){
        return tv_cache;
    }
    private void initView() {
        ll_changepass = $(R.id.ll_changepasswd);
        ll_changephone = $(R.id.ll_changephone);
        ll_systeminfo = $(R.id.ll_systeminfo);
        ll_callback = $(R.id.ll_callback);
        ll_cleancache = $(R.id.ll_cleancache);
        ll_exit = $(R.id.ll_exit);
        ll_checkupadte = $(R.id.ll_checkupadte);
        tv_phone=$(R.id.tv_phone);
        tv_cache=$(R.id.tv_cache);
        tv_version=$(R.id.tv_version);
        tv_provsion=$(R.id.tv_provsion);
        tv_sysdos = $(R.id.tv_sysdos);
        ll_exit.setOnClickListener(this);
        ll_cleancache.setOnClickListener(this);
        ll_changephone.setOnClickListener(this);
        ll_changepass.setOnClickListener(this);
        ll_systeminfo.setOnClickListener(this);
        ll_callback.setOnClickListener(this);
        ll_checkupadte.setOnClickListener(this);
        tv_provsion.setOnClickListener(this);
        registerReceiver();

    }
    /**
     * 注册监听网络广播广播
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ShowSysDosReceiver.SHOWSYSDOS);
        showSysDosReceiver = new ShowSysDosReceiver();
        this.registerReceiver(showSysDosReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(showSysDosReceiver);
    }

    private void initDate(){
        um=UserMessage.getInstance(this);
        String phoneNum=um.getLoginName();
        if(phoneNum.length()==11)
            tv_phone.setText(phoneNum.substring(0,3)+"****"+phoneNum.substring(phoneNum.length()-4,phoneNum.length()));
        else
            tv_phone.setText(phoneNum);

        path = StorageUtils.getOwnCacheDirectory(this, "ChatFile");
        try {
            String cacheSize=CacheHelp.getPathSize(path);
            G.log(cacheSize+"--------");
            if(cacheSize.indexOf("Byte")!=-1){
                tv_cache.setText("暂无缓存");
            }else{
                tv_cache.setText(cacheSize);
            }
        } catch (Exception e) {
            G.log(e);
            e.printStackTrace();
        }
        tv_version.setText("V"+PackageUtils.getVersionName(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.tv_subtitle) {
            Intent intent = new Intent();
            intent.setClass(this, UserChooseActivity.class);
            intent.putExtra("type",true);
            startActivity(intent);

        } else if (viewID == R.id.ll_changepasswd) {
            Intent intent = new Intent();
            intent.putExtra("type", "pw");
            intent.putExtra("phoneNumber",um.getLoginName());
            intent.setClass(this, UpdateMessageActivity.class);
            startActivity(intent);
        } else if (viewID == R.id.ll_changephone) {
            OperateDialog dialog = new OperateDialog(this);
            dialog.initPasswordView();
        } else if (viewID == R.id.ll_systeminfo) {
             Intent intent = new Intent();
             intent.setClass(this, SystemInfoActivity.class);
             startActivity(intent);
            tv_sysdos.setVisibility(View.GONE);
        } else if (viewID == R.id.ll_callback) {
            Intent intent = new Intent();
            intent.setClass(this, AdviceActivity.class);
            startActivity(intent);
        } else if (viewID == R.id.ll_exit) {
            OperateDialog dialog = new OperateDialog(this,1);
            dialog.initexitView();
        } else if (viewID == R.id.ll_cleancache) {
            OperateDialog dialog = new OperateDialog(this,2);
            dialog.initexitView();
        } else if (viewID == R.id.ll_checkupadte) {
//            String url = "http://apkegg.mumayi.com/cooperation/2016/06/17/101/1013262/doupocangqiong_V1.4.1_mumayi_64934.apk";
            new CheckUpdate(this,true);
        }else if(viewID==R.id.tv_provsion){
            startActivity(new Intent(this,ProvsionActivity.class));
        }
    }
    private class ShowSysDosReceiver extends BroadcastReceiver{
        public static final String SHOWSYSDOS = "net.hongzhang.user.activity.ShowSysDosReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SHOWSYSDOS)) {
                Bundle bundle = intent.getExtras();
                if (bundle.getBoolean("isVisible", false)) {
                    tv_sysdos.setVisibility(View.VISIBLE);
                } else {
                    tv_sysdos.setVisibility(View.GONE);
                }
            }
        }
    }
}