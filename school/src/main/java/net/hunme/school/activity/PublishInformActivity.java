package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;

public class PublishInformActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_permitchoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_inform);
        initdata();
    }
    private void  initdata(){
        ll_permitchoose = $(R.id.ll_permitchoose);
        ll_permitchoose.setOnClickListener(this);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("发通知");
        setSubTitle("完成");
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ll_permitchoose){
            Intent intent = new Intent(this,PublishInformActivity.class);
            startActivity(intent);
        }
    }
}
