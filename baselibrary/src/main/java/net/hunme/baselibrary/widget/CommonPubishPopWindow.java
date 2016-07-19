package net.hunme.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hunme.baselibrary.R;

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
     * 第一个文本
     */
    public TextView tv_poptext1;
    /**
     * 第一个图片
     */
    public ImageView iv_popimage1;
    /**
     * 第二个文本
     */
    public TextView tv_poptext2;
    /**
     * 第二个图片
     */
    public ImageView iv_popimage2;
    /**
     * 第三个文本
     */
    public TextView tv_poptext3;
    /**
     * 第三个图片
     */
    public ImageView iv_popimage3;
    public CommonPubishPopWindow(Activity context){
        this.context = context;
        init();
    }
      private void initview(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_inform, null);
        tv_poptext1 = (TextView) conentView.findViewById(R.id.tv_poptext1);
        tv_poptext2 = (TextView) conentView.findViewById(R.id.tv_poptext2);
        tv_poptext3 = (TextView) conentView.findViewById(R.id.tv_poptext3);
        iv_popimage1 = (ImageView)conentView.findViewById(R.id.iv_popimage1);
        iv_popimage2 = (ImageView)conentView.findViewById(R.id.iv_popimage2);
          iv_popimage3 = (ImageView)conentView.findViewById(R.id.iv_popimage3);
    }
    public void init() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        //设置SignPopupWindow弹出窗体的宽
        int height =  context.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        this.setHeight(height-dp2px(context,80));
        //设置SignPopupWindow弹出窗体的高
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
      */
    public static int dp2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
