package net.hongzhang.baselibrary.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.hongzhang.baselibrary.R;


/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BaseConformDialog implements View.OnClickListener{
    /**
     * 确定按钮
     */
    public    Button btn_conform;
    /**
     * 取消按钮
     */
    public Button btn_concal;
    /**
     * 提示内容
     */
    public TextView tv_content;
    /**
     * 对话框
     */
    public AlertDialog alertDialog;
    public void initView(Activity context, String content) {
         View contentView= LayoutInflater.from(context).inflate(R.layout.dialog_base_conform, null);
         btn_conform = (Button) contentView.findViewById(R.id.bt_conform);
         alertDialog = MyAlertDialog.getDialog(contentView, context,1);
         btn_concal = (Button) contentView.findViewById(R.id.bt_cancel);
         tv_content = (TextView) contentView.findViewById(R.id.tv_content);
         tv_content.setText(content);
         btn_concal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_conform.setOnClickListener(this);
    }
}
