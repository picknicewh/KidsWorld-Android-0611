package net.hongzhang.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;

public class InfoDetailsActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);
        initView();
        initDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("消息详情");
    }

    private void initView(){
        tv_title=$(R.id.tv_title);
        tv_date=$(R.id.tv_date);
        tv_content=$(R.id.tv_content);
    }

    private void initDate(){
        Intent intent=getIntent();
        tv_title.setText(intent.getStringExtra("title"));
        tv_date.setText(intent.getStringExtra("date").substring(0,10));
        tv_content.setText(intent.getStringExtra("content"));
    }
}
