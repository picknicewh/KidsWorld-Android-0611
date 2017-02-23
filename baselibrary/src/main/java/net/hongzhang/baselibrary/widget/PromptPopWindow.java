package net.hongzhang.baselibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;


/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PromptPopWindow extends PopupWindow {
    private View contentView;
    private int width;
    private Context context;
    public PromptPopWindow(Context context, String content) {
        this.context = context;
        width = (int) (G.size.W * 0.8);
        initView(content);
        init();
    }

    private void initView(String content) {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_prompt, null);
        Button btn_conform = (Button) contentView.findViewById(R.id.btn_conform);
        TextView pop_title = (TextView) contentView.findViewById(R.id.tv_title);
        btn_conform.setText("我知道了");
        pop_title.setText(content);
        btn_conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
