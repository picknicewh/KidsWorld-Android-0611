package net.hunme.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * 作者： Administrator
 * 时间： 2016/7/18
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PubishPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private View conentView;
    /**
     * 第一个文本
     */
    private TextView tv_poptext1;
    /**
     * 第一个图片
     */
    private ImageView iv_popimage1;
    /**
     * 第二个文本
     */
    private TextView tv_poptext2;
    /**
     * 第二个图片
     */
    private ImageView iv_popimage2;
    /**
     * 第三个文本
     */
    private TextView tv_poptext3;
    /**
     * 第三个图片
     */
    private ImageView iv_popimage3;
    //ImageView image1,TextView text1
    private  int flag;
    public  PubishPopWindow(Activity context, int flag){
        this.context = context;
        this.flag = flag;
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
        if (flag==1){
            tv_poptext1.setText("文字");
            tv_poptext2.setText("相册");
            tv_poptext3.setText("视频");
        }else {
            tv_poptext1.setText("学校通知");
            tv_poptext2.setText("班级通知");
            tv_poptext1.setText("教师通知");
        }
        iv_popimage1.setOnClickListener(this);
        iv_popimage2.setOnClickListener(this);
        iv_popimage3.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
      if (view.getId()==R.id.iv_popimage1){
          Intent intent = new Intent();
      }else if (view.getId()==R.id.iv_popimage2){

      }else if (view.getId()==R.id.iv_popimage3){

      }
    }
    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
      */
    public static int dp2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
