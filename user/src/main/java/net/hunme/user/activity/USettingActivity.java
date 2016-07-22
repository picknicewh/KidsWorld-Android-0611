package net.hunme.user.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(this);
        setSubTitleOnClickListener(this);
        setCententTitle("账户设置");
        setSubTitle("切换");
    }
    private  void  initView(){
        ll_changepass = $(R.id.ll_changepasswd);
        ll_changephone = $(R.id.ll_changephone);
        ll_systeminfo = $(R.id.ll_systeminfo);
        ll_callback = $(R.id.ll_callback);
        ll_cleancache = $(R.id.ll_cleancache);
        ll_exit = $(R.id.ll_exit);
        ll_exit.setOnClickListener(this);
        ll_cleancache.setOnClickListener(this);
        ll_changephone.setOnClickListener(this);
        ll_changepass.setOnClickListener(this);
        ll_systeminfo.setOnClickListener(this);
        ll_callback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int viewID=v.getId();
        if(viewID==R.id.iv_left){
            finish();
        }else if (viewID==R.id.tv_subtitle) {
            Intent intent = new Intent();
            intent.setClass(this, UserChooseActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changepasswd){
            Intent intent = new Intent();
            intent.setClass(this,ChangePswdActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changephone){
            Intent intent = new Intent();
            intent.setClass(this,PhoneNumberActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_systeminfo){
            Intent intent = new Intent();
            intent.setClass(this,SystemInfoActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_callback){
            Intent intent = new Intent();
            intent.setClass(this,AdviceActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_exit){
            showPopupWindow(1);
        }else if (viewID==R.id.ll_cleancache){
            showPopupWindow(0);
        }
    }
    /**
     * 退出提示框
     */
    private void showPopupWindow(int flag) {
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.pop_exit, null);
        final AlertDialog coupons_s = new AlertDialog.Builder(this).create();
        coupons_s.setCanceledOnTouchOutside(true);
        coupons_s.show();
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params =
                coupons_s.getWindow().getAttributes();
//        params.height = (int) (display.getHeight());
        params.width = (int) (display.getWidth() * 0.8);
        coupons_s.getWindow().setAttributes(params);
        coupons_s.getWindow().setBackgroundDrawableResource(R.drawable.fillet_pop);
        coupons_s.getWindow().setContentView(coupons_view);
        Button pop_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button pop_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        TextView pop_title =  (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        if (flag==1){
            pop_mastrigst.setText("确认退出");
            pop_title.setText("是否退出当前账号？");
        }else {
            pop_mastrigst.setText("确认");
            pop_title.setText("确认清除缓存的数据和图片吗？");
        }
        pop_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            /*    //清除个人所有记录
              *//*  userInfo.clean();
                HMDroidGap.synCookies(USettingActivity.this);*//*
                Intent intent = new Intent(USettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
            }
        });
        pop_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupons_s.cancel();
            }
        });
    }


}
