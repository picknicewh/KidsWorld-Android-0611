package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class UpdateMessageActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_heckNumber;
    private EditText et_password;
    private Button b_finish;
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);
        initView();
        initData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("修改密码");
    }

    private void initView(){
        et_heckNumber=$(R.id.et_heckNumber);
        et_password=$(R.id.et_password);
        b_finish=$(R.id.b_finish);
        tv_time=$(R.id.tv_time);
    }

    private void initData(){
        String type=getIntent().getStringExtra("type");
        if("pw".equals(type)){
            setCententTitle("修改密码");
            et_password.setHint("请输入你的密码");
            b_finish.setText("完成");
        }else{
            setCententTitle("修改手机号");
            et_password.setHint("请输入你的新号码");
            b_finish.setText("确定");
        }

    }

    @Override
    public void onClick(View view) {

    }
}
