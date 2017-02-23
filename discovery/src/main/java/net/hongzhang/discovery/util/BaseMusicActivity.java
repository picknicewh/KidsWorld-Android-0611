package net.hongzhang.discovery.util;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.util.PermissionsChecker;
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
public abstract class BaseMusicActivity extends AppCompatActivity {

    public     LoadingDialog dialog;
    // 访问所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseLibrary.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        G.setTranslucent(this);
        PermissionsChecker checker = PermissionsChecker.getInstance(this);
        if (checker.lacksPermissions(PERMISSIONS)) {
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        // 账号抢登监听
        if (RongIM.getInstance()!=null){
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        }
    }
    public <T extends View> T $(@IdRes int resId){
        return (T)super.findViewById(resId);
    }

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
