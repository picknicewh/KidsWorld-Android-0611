package net.hongzhang.school.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.activity.CameraCaptureActivity;
import net.hongzhang.school.activity.SubmitTaskActivityS;

/**
 * 作者： wanghua
 * 时间： 2017/4/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PictureChoosePopWindow extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private View contentView;

    public PictureChoosePopWindow(Activity context) {
        this.context = context;
        init();
    }

    private void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_picture_choose, null);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tv_alubm = (TextView) contentView.findViewById(R.id.tv_album);
        LinearLayout ll_camera = (LinearLayout) contentView.findViewById(R.id.ll_camera);
        tv_alubm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
    }

    private boolean hasVideo;

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    /**
     * 初始化popwindow各项参数
     */
    private void init() {
        initView();
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
      /*  if (viewId==R.id.tv_concal){
        }else */
        if (viewId == R.id.tv_album) {
            if (context instanceof SubmitTaskActivityS) {
                SubmitTaskActivityS activity = (SubmitTaskActivityS) context;
                activity.goSelectImager(activity);
            }
        } else if (viewId == R.id.ll_camera) {
            Intent intent = new Intent(context, CameraCaptureActivity.class);
            intent.putExtra("hasVideo",hasVideo);
            //是第二个参数的问题，该参数必须大于0才能在返回值，并激活onActivityResult方法。
            // 最开始是用的一个activity默认的常量：RESULT_OK，跟踪了代码后发现，该常量的值为-1，
            // 当然没法激活 onActivityResult方法了，随后随便修改为一个大于0的整数，程序即通跑成功。
            context.startActivityForResult(intent, 1);
        }
        dismiss();
    }
}
