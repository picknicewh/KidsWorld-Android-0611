package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;

public class PublishInformActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_permitchoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_inform);
        initdata();
    }
    private void  initdata(){
        rl_permitchoose = $(R.id.rl_permitchoose);
        rl_permitchoose.setOnClickListener(this);
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
        if (view.getId()==R.id.rl_permitchoose){
            Intent intent = new Intent(this,PublishinfoChooseActivity.class);
            startActivity(intent);
        }
    }
}
