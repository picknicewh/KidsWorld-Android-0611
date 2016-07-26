package net.hunme.user.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.login.UserChooseActivity;
import net.hunme.user.R;
import net.hunme.user.util.MyAlertDialog;

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
        setLiftOnClickClose();
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
        if (viewID==R.id.tv_subtitle) {
            Intent intent = new Intent();
            intent.setClass(this, UserChooseActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changepasswd){
            Intent intent = new Intent();
            intent.putExtra("type","pw");
            intent.setClass(this,UpdateMessageActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changephone){
          /*  Intent intent = new Intent();
            intent.putExtra("type","phone");
            intent.setClass(this,UpdateMessageActivity.class);
            startActivity(intent);*/
            showPhoneialog();
        }else if (viewID==R.id.ll_systeminfo){
            Intent intent = new Intent();
            intent.setClass(this,SystemInfoActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_callback){
            Intent intent = new Intent();
            intent.setClass(this,AdviceActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_exit){
            showAlertDialog(1);
        }else if (viewID==R.id.ll_cleancache){
            showAlertDialog(0);
        }
    }
    /**
     * 退出提示框
     */
    private void showAlertDialog(final int flag) {
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.alertdialog_message, null);
        final AlertDialog alertDialog= MyAlertDialog.getDialog(coupons_view,this,1);
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
                if(flag==1)
                    //退出账号
                    finish();
                else
                    //清除缓存
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
        final AlertDialog alertDialog=MyAlertDialog.getDialog(view,this,0);
        Button bt_conform = (Button) view.findViewById(R.id.bt_dg_conform);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dg_cancel);
          EditText et_password = (EditText) view.findViewById(R.id.et_dg_password);
        final String password = et_password.getText().toString();
        bt_conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if (password.equals("123465")){
                    Toast.makeText(getApplicationContext(),"输入密码不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("phone",password);
                intent.setClass(USettingActivity.this,ModifyPhoneActivity.class);
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

}
