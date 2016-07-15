package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_userMassage;
    private LinearLayout ll_more;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setViewAction();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setLiftOnClickListener(this);
        setCententTitle("我的");
    }

    private void setViewAction(){
        ll_userMassage=$(R.id.ll_userMassage);
        ll_more=$(R.id.ll_more);
        ll_userMassage.setOnClickListener(this);
        ll_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.iv_left){
            finish();
        }else if(viewId==R.id.ll_userMassage){
            startActivity(new Intent(UserActivity.this,UserMassageActivity.class));
        }else if(viewId==R.id.ll_more){
            startActivity(new Intent(UserActivity.this,UserSettingActivity.class));
        }

    }

}
