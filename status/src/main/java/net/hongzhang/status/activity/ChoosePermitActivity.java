package net.hongzhang.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.status.R;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：选择可见范围
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class  ChoosePermitActivity extends BaseActivity implements View.OnClickListener {
    public  static  final  int CHOOSE_PERMIT = 2;
    /**
     * 班级空间和校园空间
     */
    private RadioButton rb_allroom;
    /**
     * 班级空间
     */
    private RadioButton rb_classroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_permit);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("选择可见范围");
        setLiftOnClickClose();
    }

    private void  initView(){
        rb_allroom = $(R.id.rb_allroom);
        rb_classroom = $(R.id.rb_classroom);
        rb_classroom.setOnClickListener(this);
        rb_allroom.setOnClickListener(this);
        String chooseValue=getIntent().getStringExtra("permit");
        if(rb_allroom.getText().toString().equals(chooseValue)){
            rb_allroom.setChecked(true);
        }else{
            rb_classroom.setChecked(true);
        }
    }

    private void choose(String permit){
        Intent intent = new Intent();
        intent.setClass(this,PublishStatusActivity.class);
        intent.putExtra("permit",permit);
        setResult(CHOOSE_PERMIT,intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int viewId =view.getId();
        if (viewId==R.id.rb_allroom){
            choose(rb_allroom.getText().toString());
        }else if (viewId==R.id.rb_classroom) {
            choose(rb_classroom.getText().toString());
        }
    }
}
