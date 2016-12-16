package net.hunme.discovery.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.discovery.MainPlayActivity;
import net.hunme.discovery.R;

/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class NoPayComformPopWindow extends PopupWindow {
    private View contentView;
    private int width;
    private MainPlayActivity context;

    public NoPayComformPopWindow(MainPlayActivity context,String content) {
        this.context = context;
        width = (int) (G.size.W * 0.8);
        initView(content);
        init();
    }

    private void initView(String content) {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        Button btn_conform = (Button) contentView.findViewById(R.id.btn_conform);
        TextView pop_title = (TextView) contentView.findViewById(R.id.tv_title);
        btn_conform.setText("我知道了");
        pop_title.setText(content);
        btn_conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
                dismiss();
            }
        });
    }

    private void init() {
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(width);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
    }
}
