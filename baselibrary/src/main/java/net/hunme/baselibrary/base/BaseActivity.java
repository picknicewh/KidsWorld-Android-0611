package net.hunme.baselibrary.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.R;
import net.hunme.baselibrary.util.MyConnectionStatusListener;
import net.hunme.baselibrary.util.ToolBarHelper;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseLibrary.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //取消状态栏背景 状态栏和toolbar重合  toolbar的背景改变状态栏背景也会改变  实现了动态改变状态栏颜色  也是与主流最为相似
        // 缺点是 由于和toolbar重合 必须设置toolbar的高度 paddingTop 让其空出位置给状态栏  但是  由于baseActivyt是先加入toolbar 再加入布局
        //这与会让布局背toolbar遮住 所以 但是不用这个baseActivity是可行的

        //最终方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //5.0 全透明实现
             //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
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

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
