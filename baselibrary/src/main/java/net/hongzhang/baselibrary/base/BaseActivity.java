package net.hongzhang.baselibrary.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.util.ToolBarHelper;
import net.hongzhang.baselibrary.widget.LoadingDialog;

import io.rong.imkit.RongIM;


/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/13
 * 描    述：所有Activity父类
 * 版    本：1.0 添加Toolbar代码
 *          1.2 添加友盟统计
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ToolBarHelper mToolBarHelper ;
    public Toolbar toolbar ;
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private TextView tv_subTitle;
    public     LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseLibrary.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        G.setTranslucent(this);
    }
    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;
        initToolbar(toolbar);
        setToolBar();
        // 账号抢登监听
        if (RongIM.getInstance()!=null){
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        }
    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
    }

    private void initToolbar(Toolbar toolbar){
//        iv_left= (ImageView) toolbar.findViewById(R.id.iv_left);
        iv_right= (ImageView) toolbar.findViewById(R.id.iv_right);
        tv_title= (TextView) toolbar.findViewById(R.id.tv_title);
        tv_subTitle= (TextView) toolbar.findViewById(R.id.tv_subtitle);
    }

    public void setLiftImage(int imageResource){
        toolbar.setNavigationIcon(imageResource);
//        iv_left.setImageResource(imageResource);
//        iv_left.setVisibility(View.VISIBLE);
    }

    public void setRightImage(int imageResource){
        iv_right.setImageResource(imageResource);
        iv_right.setVisibility(View.VISIBLE);
    }

    public void setCententTitle(String title){
        tv_title.setText(title);
        tv_title.setVisibility(View.VISIBLE);
    }

    public void setSubTitle(String title){
        tv_subTitle.setText(title);
        tv_subTitle.setVisibility(View.VISIBLE);
    }

    public void setSubTitleOnClickListener(View.OnClickListener listener){
        tv_subTitle.setOnClickListener(listener);
    }

    public void setLiftOnClickListener(View.OnClickListener listener){
//        iv_left.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(listener);
    }
    public void setLiftOnClickClose(){
//        iv_left.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setRightOnClickListener(View.OnClickListener listener){
        iv_right.setOnClickListener(listener);
    }

    public <T extends View> T $(@IdRes int resId){
        return (T)super.findViewById(resId);
    }

    protected abstract void setToolBar();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void showLoadingDialog() {
        if(dialog==null)
            dialog=new LoadingDialog(this, R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    public void stopLoadingDialog() {
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
