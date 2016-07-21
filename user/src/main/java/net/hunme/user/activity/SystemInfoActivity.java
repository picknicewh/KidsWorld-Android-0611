package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class SystemInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
    }

    @Override
    protected void setToolBar() {
        setCententTitle("系统消息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
