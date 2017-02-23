package net.hongzhang.user.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/9
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class StatusCommentPopWindow extends PopupWindow {
    private View conentView;
    private Context context;
    private InputMethodManager imm;
    public StatusCommentPopWindow(Context context,int layoutSource) {
        super(context);
        this.context=context;
        init(layoutSource);
    }

    private void initView(int layoutSource){
        conentView = LayoutInflater.from(context).inflate(layoutSource,null);
//         td_commnet= (EditText) conentView.findViewById(R.id.et_comment);
//        Button b_comment= (Button) conentView.findViewById(R.id.b_comment);
//        td_commnet.setFocusable(true);
        //打开软键盘
        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        b_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
    }

    private void init(int layoutSource) {
        initView(layoutSource);
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

//    @Override
//    public void dismiss() {
//        super.dismiss();
//        //关闭软键盘
//        imm.hideSoftInputFromWindow(td_commnet.getWindowToken(), 0);
//    }

    public View getView(){
        return conentView;
    }

}
