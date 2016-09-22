package net.hunme.user.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

import net.hunme.baselibrary.activity.UpdateMessageActivity;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MyAlertDialog;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.LoginActivity;
import net.hunme.login.UserChooseActivity;
import net.hunme.user.R;
import net.hunme.user.util.CacheHelp;
import net.hunme.user.util.CheckUpdate;
import net.hunme.baselibrary.util.PackageUtils;

import java.io.File;

import io.rong.imkit.RongIM;

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
    private TextView tv_cache;
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
            showPhoneialog();
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
            showAlertDialog(1);
        } else if (viewID == R.id.ll_cleancache) {
            showAlertDialog(0);
        } else if (viewID == R.id.ll_checkupadte) {
//            String url = "http://apkegg.mumayi.com/cooperation/2016/06/17/101/1013262/doupocangqiong_V1.4.1_mumayi_64934.apk";
            new CheckUpdate(this,true);
        }else if(viewID==R.id.tv_provsion){
            startActivity(new Intent(this,ProvsionActivity.class));
        }
    }

    /**
     * 退出提示框
     * @param  flag
     */
    private void showAlertDialog(final int flag) {
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.alertdialog_message, null);
        final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, this,1);
        Button pop_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button pop_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        if (flag == 1) {
            pop_mastrigst.setText("确认退出");
            pop_title.setText("是否退出当前账号？");
        } else {
            pop_mastrigst.setText("确认");
            pop_title.setText("确认清除缓存的数据和图片吗？");
        }
        pop_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (flag == 1) {
                    //退出账号
                    if(RongIM.getInstance()!=null){
                        RongIM.getInstance().disconnect();
                    }
                    MobclickAgent.onProfileSignOff();
                    UserMessage.getInstance(USettingActivity.this).clean();
                    Intent intent = new Intent(USettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CacheHelp.deleteFolderFile(Environment.getExternalStorageDirectory().toString() + "/ChatFile",true);
                    tv_cache.setText("暂无缓存");
                    //清除缓存
//                    alertDialog.dismiss();
                }
                alertDialog.dismiss();
            }
        });
        pop_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 修改手机号对话框
     */
    private void showPhoneialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_modifyphone, null);
        final AlertDialog alertDialog = MyAlertDialog.getDialog(view, this,1);
        Button bt_conform = (Button) view.findViewById(R.id.bt_dg_conform);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dg_cancel);
        final EditText et_password = (EditText) view.findViewById(R.id.et_dg_password);
        bt_conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    G.showToast(USettingActivity.this, "密码不能为空");
                    return;
                } else if (!password.equals(UserMessage.getInstance(USettingActivity.this).getPassword())) {
                    G.showToast(USettingActivity.this, "输入密码不正确");
                    return;
                }
                intent.putExtra("phone", password);
                intent.setClass(USettingActivity.this, UpdateMessageActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    private class ShowSysDosReceiver extends BroadcastReceiver{
        public static final String SHOWSYSDOS = "net.hunme.user.activity.ShowSysDosReceiver";
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