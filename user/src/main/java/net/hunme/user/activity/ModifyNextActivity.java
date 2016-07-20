package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class ModifyNextActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 确认
     */
    private TextView bt_conform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_next);
        initView();
    }
    private void  initView(){
        bt_conform = $(R.id.bt_conform);
        bt_conform.setOnClickListener(this);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCententTitle("输入验证码");
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            Intent intent = new Intent();
            intent.setClass(this,ResetPhoneActivity.class);
            startActivity(intent);
        }
    }
}
