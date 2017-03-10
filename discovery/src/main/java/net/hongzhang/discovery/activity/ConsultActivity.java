package net.hongzhang.discovery.activity;

import android.os.Bundle;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;

public class ConsultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_list);
        String resId=getIntent().getStringExtra("resourceId");
        G.showToast(this,resId);
        G.log("======resID====="+resId);
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("资讯详情");
    }
}
