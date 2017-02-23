package net.hongzhang.school.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import net.hongzhang.school.R;


/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DatePopWindow extends PopupWindow {
    private View contentView;
    private Context context;
    private DateDismissReceiver receiver;
    public static final String DISMISS = "net.hongzhang.school.util.dismiss";
    public DatePopWindow(Context context) {
        this.context = context;
        registerReceiver();
        init();
    }
    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_date, null);
        MulSelectDateView dateView = (MulSelectDateView) contentView.findViewById(R.id.mulselectdateview);
        dateView.show();
    }
    private void init() {
        initView();
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
    }
    public void registerReceiver(){
        receiver = new DateDismissReceiver();
        IntentFilter filter  = new IntentFilter(DISMISS);
        context.registerReceiver(receiver,filter);
    }
    public void unregisteReceiver(){
       context.unregisterReceiver(receiver);
    }
    public class DateDismissReceiver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {
           if (intent.getAction().equals(DISMISS)){
               dismiss();
           }
       }
   }
}
