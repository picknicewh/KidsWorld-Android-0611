package net.hunme.school.activity;

import android.os.Bundle;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;

public class PublishinfoChooseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishinfo_choose);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择学校对象");
        setSubTitle("完成");
    }
}
