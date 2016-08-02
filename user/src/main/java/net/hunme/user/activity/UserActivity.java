package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
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
    private LinearLayout ll_myphoto;       //我的相册
    private LinearLayout ll_usersetting;   //用户设置
    private LinearLayout ll_mycollection;  //我的收藏
    private LinearLayout ll_mydynamics;    //我的动态
    private CircleImageView cv_portrait;
    private TextView tv_id;
    private TextView tv_name;
    private TextView tv_address;
    private final String MYCOLLECTION="http://192.168.5.136:8989/webSVN/kidsWorld/paradise/index.html#/collect";
    private final String MYDYNAMICS="http://192.168.1.179:8787/web/kidsWorld/space/view/myDynamic.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我的");
    }

    private void initView(){
        ll_userMassage=$(R.id.ll_userMassage);
        ll_myphoto=$(R.id.ll_myphoto);
        ll_usersetting=$(R.id.ll_usersetting);
        ll_mycollection=$(R.id.ll_mycollection);
        ll_mydynamics=$(R.id.ll_mydynamics);
        cv_portrait=$(R.id.cv_portrait);
        tv_id=$(R.id.tv_id);
        tv_name=$(R.id.tv_name);
        tv_address=$(R.id.tv_address);
        ll_userMassage.setOnClickListener(this);
        ll_myphoto.setOnClickListener(this);
        ll_usersetting.setOnClickListener(this);
        ll_mycollection.setOnClickListener(this);
        ll_mydynamics.setOnClickListener(this);
    }

    private void initData(){
        UserMessage um=UserMessage.getInstance(this);
//        cv_portrait.setImageResource();  头像暂时无法获取
        if("1".equals(um.getType())){
            tv_id.setText("学");
            tv_id.setBackgroundResource(R.drawable.user_study_selecter);
        }else{
           tv_id.setText("师");
           tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
        tv_name.setText(um.getUserName());
        tv_address.setText(um.getSchoolName()+"-"+um.getClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
         initData();
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        Intent intent=null;
        if(viewId==R.id.ll_userMassage){
            intent=new Intent(UserActivity.this,UMassageActivity.class);
        }else if(viewId==R.id.ll_usersetting){
            intent=new Intent(UserActivity.this,USettingActivity.class);
        }else if(viewId==R.id.ll_myphoto){
            intent=new Intent(UserActivity.this,UPhotoActivity.class);
        }else if(viewId==R.id.ll_mycollection){
            intent=new Intent(UserActivity.this,HMDroidGap.class);
            intent.putExtra("loadUrl",MYCOLLECTION);
            intent.putExtra("title","我的收藏");
        }else if(viewId==R.id.ll_mydynamics){
            intent=new Intent(UserActivity.this,HMDroidGap.class);
            intent.putExtra("loadUrl",MYDYNAMICS);
            intent.putExtra("title","我的动态");
        }
        if(null!=intent){
            startActivity(intent);
        }
    }
}
