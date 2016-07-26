package net.hunme.user.activity;

import android.os.Bundle;
import android.widget.ListView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class SystemInfoActivity extends BaseActivity {
    private ListView lv_systeminfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
    }

    @Override
    protected void setToolBar() {
        setCententTitle("系统消息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }
    private void initView(){
        lv_systeminfo=$(R.id.lv_systeminfo);
    }
}
