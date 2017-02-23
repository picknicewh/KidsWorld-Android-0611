package net.hongzhang.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.util.G;

/**
 * 作者： wh
 * 时间： 2016/7/18
 * 名称：发布弹窗
 * 版本说明：v1.0基本ui的编写
 * 附加注释：根据传递的flag值不同，分别作用于动态--发布动态，学校-发布通知的
 * 主要接口：
 */
public abstract class CommonPubishPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private View conentView;
    /**
     *  发布文字
     */
    private ImageView iv_text;
    /**
     * 发布照片
     */
    private ImageView iv_photo;
    /**
     * 发布视频
     */
    private ImageView iv_move;
    /**
     * 下面的阴影
     */
    private LinearLayout ll_shade;
    public CommonPubishPopWindow(Activity context){
        this.context = context;
        init();
    }
    private void initview(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_inform, null);
        iv_text= (ImageView) conentView.findViewById(R.id.iv_text);
        iv_photo= (ImageView) conentView.findViewById(R.id.iv_photo);
        ll_shade = (LinearLayout) conentView.findViewById(R.id.ll_shade);
     //   iv_move= (ImageView) conentView.findViewById(R.id.iv_move);
        iv_text.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        ll_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    //    iv_move.setOnClickListener(this);

    }

    public void init() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        //设置SignPopupWindow弹出窗体的高
        int height =  context.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        this.setHeight(height-G.dp2px(context,70));
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


}
