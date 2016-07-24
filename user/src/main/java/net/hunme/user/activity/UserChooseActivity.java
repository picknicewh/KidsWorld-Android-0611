package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;

public class UserChooseActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 账户选择（学生）
     */
    private LinearLayout ll_choose1;
    private ImageView iv_choose1;
    /**
     * 账户选择（老师）
     */
    private LinearLayout ll_choose2;
    private ImageView iv_choose2;
    /**
     * 账户选择（校长）
     */
    private LinearLayout ll_choose3;
    private ImageView iv_choose3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        initView();
    }
    private void  initView(){
        iv_choose1 = $(R.id.iv_choose1);
        iv_choose2 = $(R.id.iv_choose2);
        iv_choose3 = $(R.id.iv_choose3);
        ll_choose1 = $(R.id.ll_choose1);
        ll_choose2 = $(R.id.ll_choose2);
        ll_choose3 = $(R.id.ll_choose3);
        ll_choose1.setOnClickListener(this);
        ll_choose2.setOnClickListener(this);
        ll_choose3.setOnClickListener(this);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择账号");
    }

    @Override
    public void onClick(View view) {
        int viewID =view.getId();
        if (viewID==R.id.ll_choose1){
            iv_choose1.setVisibility(View.VISIBLE);
            iv_choose2.setVisibility(View.GONE);
            iv_choose3.setVisibility(View.GONE);
        }else if (viewID==R.id.ll_choose2){
            iv_choose2.setVisibility(View.VISIBLE);
            iv_choose1.setVisibility(View.GONE);
            iv_choose3.setVisibility(View.GONE);
        } else if (viewID==R.id.ll_choose3){
            iv_choose3.setVisibility(View.VISIBLE);
            iv_choose2.setVisibility(View.GONE);
            iv_choose1.setVisibility(View.GONE);
        }
        finish();
    }
}
