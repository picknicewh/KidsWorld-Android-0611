package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;

public class UserMassageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_massage);
    }

    @Override
    protected void setToolBar() {
        setCententTitle("个人信息");
        setLiftImage(R.mipmap.ic_launcher);
        setLiftOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewID=v.getId();
        if(viewID==R.id.iv_left){
            finish();
        }
    }
}
