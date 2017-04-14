package net.hongzhang.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.message.R;

/**
 * 作者： wh
 * 时间： 2016/9/8
 * 名称：确认拨打电话
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConformDialog implements View.OnClickListener {
    private Activity context;
    /**
     * 对话框布局
     */
    private View contentView;
    /**
     * 确定
     */
    private Button bt_conform;
    /**
     * 取消
     */
    private Button bt_cancel;
    /**
     * 电话名
     */
    private TextView tv_phone;
    private String phoneNum;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;

    public ConformDialog(Activity context, String phoneNum){
        this.context  = context;
        this.phoneNum = phoneNum;
    }
    public void  initView(){
         contentView = LayoutInflater.from(context).inflate(R.layout.dialog_confrom, null);
         alertDialog = MyAlertDialog.getDialog(contentView, context,1);
         bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
         bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
         tv_phone = (TextView) contentView.findViewById(R.id.tv_phone);
         tv_phone.setText(phoneNum);
         bt_cancel.setOnClickListener(this);
         bt_conform.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
     if (view.getId()==R.id.bt_cancel){
         alertDialog.dismiss();
     }else if (view.getId()==R.id.bt_conform){
         if (!G.isEmteny(phoneNum)){
             Uri phoneUri =  Uri.parse("tel:"+phoneNum);
             Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);
         }
         alertDialog.dismiss();
      }
    }

}
