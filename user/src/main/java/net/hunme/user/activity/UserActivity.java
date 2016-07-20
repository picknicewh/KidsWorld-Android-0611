package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户页面
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_userMassage;   //用户信息
    private LinearLayout ll_more;          //更多
    private LinearLayout ll_myphoto;       //我的相册
    private LinearLayout ll_usersetting;   //用户设置
    private LinearLayout ll_mycollection;  //我的收藏
    private LinearLayout ll_mydynamics;    //我的动态
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
        ll_myphoto=$(R.id.ll_myphoto);
        ll_usersetting=$(R.id.ll_usersetting);
        ll_mycollection=$(R.id.ll_mycollection);
        ll_mydynamics=$(R.id.ll_mydynamics);

        ll_userMassage.setOnClickListener(this);
        ll_more.setOnClickListener(this);
        ll_myphoto.setOnClickListener(this);
        ll_usersetting.setOnClickListener(this);
        ll_mycollection.setOnClickListener(this);
        ll_mydynamics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.iv_left){
            finish();
        }else if(viewId==R.id.ll_userMassage){
            startActivity(new Intent(UserActivity.this,UMassageActivity.class));
        }else if(viewId==R.id.ll_usersetting){
            startActivity(new Intent(UserActivity.this,USettingActivity.class));
        }else if(viewId==R.id.ll_myphoto){
            startActivity(new Intent(UserActivity.this,UPhotoActivity.class));
        }else if(viewId==R.id.ll_mycollection){
            startActivity(new Intent(UserActivity.this,UCollectionActivity.class));
        }else if(viewId==R.id.ll_mydynamics){
            startActivity(new Intent(UserActivity.this,UDynamicsActivity.class));
        }else if (viewId==R.id.ll_more){
            startActivity(new Intent(UserActivity.this,ModifyPhoneActivity.class));
        }
    }
}
