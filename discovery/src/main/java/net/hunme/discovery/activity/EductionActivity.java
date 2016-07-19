package net.hunme.discovery.activity;

import android.os.Bundle;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.discovery.R;

public class EductionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_class);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("教育咨询");
        setSubTitle("搜索");
    }
}
