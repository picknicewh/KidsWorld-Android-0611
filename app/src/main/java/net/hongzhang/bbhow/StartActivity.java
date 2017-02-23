package net.hongzhang.bbhow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import main.jpushlibrary.JPush.JPushBaseActivity;

public class StartActivity extends JPushBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initWindow();
        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(StartActivity.this, MainActivity.class));
               finish();
           }
       },1000);
    }
    /**
     * 标题栏透明
     * 仅支持api大于等于19
     */
    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
