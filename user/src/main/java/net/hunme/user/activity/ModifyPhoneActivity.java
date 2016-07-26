package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class ModifyPhoneActivity extends BaseActivity {
    /**
     * 手机号码
     */
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        initData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("修改手机号");
    }
    private void initData(){
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }
}
