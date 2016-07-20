package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class ModifyPhoneActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 修改密码编辑框
     */
    private EditText et_password;
    /**
     * 下一步
     */
    private Button bt_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        initView();
    }
    private  void  initView(){
        et_password = $(R.id.et_password);
        bt_next = $(R.id.bt_pnext);
        bt_next.setOnClickListener(this);
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
        setCententTitle("修改手机号");
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_pnext){
            Intent intent = new Intent();
            intent.setClass(this,ModifyNextActivity.class);
            startActivity(intent);
        }
    }
}
