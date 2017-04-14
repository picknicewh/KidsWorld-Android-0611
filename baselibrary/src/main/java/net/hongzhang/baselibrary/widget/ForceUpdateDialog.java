package net.hongzhang.baselibrary.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hongzhang.baselibrary.R;

/**
 * 作者： wanghua
 * 时间： 2017/4/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ForceUpdateDialog  extends AlertDialog{

    private ProgressBar progressBar;
    private TextView tv_speed;

    public ForceUpdateDialog(Context context) {
        super(context);
    }

    public ForceUpdateDialog( Context context,int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_force_update);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        setCanceledOnTouchOutside(false);
    }
    public void setMessageData(float progress,float totleSize,float currentSize){
        String mTotleSize = String.format("%.2f", totleSize);
        String mCurrentSize = String.format("%.2f", currentSize);
        tv_speed.setText(mTotleSize+"MB/"+mCurrentSize+"MB");
        progressBar.setProgress((int)(progress*100));
    }
}

