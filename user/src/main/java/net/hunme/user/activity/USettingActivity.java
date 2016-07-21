package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
        setCententTitle("账户设置");
        setSubTitle("切换");
    }
    private  void  initView(){
        ll_changepass = $(R.id.ll_changepasswd);
        ll_changephone = $(R.id.ll_changephone);
        ll_systeminfo = $(R.id.ll_systeminfo);
        ll_callback = $(R.id.ll_callback);
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
        }else if (viewID==R.id.iv_right) {
            Intent intent = new Intent();
            intent.setClass(this, UserChooseActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changepasswd){
            Intent intent = new Intent();
            intent.setClass(this,ModifyPhoneActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_changephone){
            Intent intent = new Intent();
            intent.setClass(this,UserChooseActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_systeminfo){
            Intent intent = new Intent();
            intent.setClass(this,SystemInfoActivity.class);
            startActivity(intent);
        }else if (viewID==R.id.ll_callback){
            Intent intent = new Intent();
            intent.setClass(this,AdviceActivity.class);
            startActivity(intent);
        }
    }
}
