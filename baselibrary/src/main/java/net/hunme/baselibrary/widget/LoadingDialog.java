package net.hunme.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.R;

/**
 * 作者： wh
 * 时间： 2016/8/5
 * 名称：加载
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LoadingDialog extends Dialog {

    private static TextView tv_prompt;
    private static LoadingDialog dialog;
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
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        tv_prompt= (TextView) this.findViewById(R.id.tv_prompt);
        linearLayout.getBackground().setAlpha(210);
    }

    public static LoadingDialog getInstance(Context context){
        if(null==dialog){
            dialog=new LoadingDialog(context,R.style.LoadingDialogTheme);
        }
        return dialog;
    }

    public static void setLoadingText(String text){
        tv_prompt.setText(text);
    }

}
