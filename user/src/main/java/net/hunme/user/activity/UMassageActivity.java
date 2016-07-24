package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户信息
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UMassageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_massage);
    }

    @Override
    protected void setToolBar() {
        setCententTitle("个人信息");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View v) {

    }
}
