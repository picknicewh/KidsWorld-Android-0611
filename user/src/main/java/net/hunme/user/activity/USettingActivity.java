package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户设置
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class USettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setLiftOnClickListener(this);
        setCententTitle("账户设置");
    }

    @Override
    public void onClick(View v) {
        int viewID=v.getId();
        if(viewID==R.id.iv_left){
            finish();
        }
    }
}
