package net.hongzhang.school.activity;

import android.os.Bundle;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;

public class ReleaseActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("发布动态");
        setSubTitle("发送");
    }
}
