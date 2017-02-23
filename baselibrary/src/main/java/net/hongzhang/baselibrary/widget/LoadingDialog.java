package net.hongzhang.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.R;

/**
 * 作者： wh
 * 时间： 2016/8/5
 * 名称：加载
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LoadingDialog extends Dialog {

    private TextView tv_prompt;
    public LoadingDialog(Context context) {
        super(context);
    }
    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        this.setCancelable(false);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Ll_loading);
        tv_prompt= (TextView) this.findViewById(R.id.tv_prompt);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setLoadingText(String text){
        tv_prompt.setText(text);
    }

}
