package main.picturesee.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import main.picturesee.R;

/**
 * Created by wanghua on 2017/2/28.
 */
public class SavePicturePopWindow extends PopupWindow implements View.OnClickListener {
    /**
     * 文本
     */
    private Activity context;
    /**
     * 内容view
     */
    private View conentView;
    private Bitmap bitmap;
    public SavePicturePopWindow(Activity context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        init();
    }
    private void initview() {
        conentView = LayoutInflater.from(context).inflate(R.layout.pop_save_picture, null);
        Button save = (Button) conentView.findViewById(R.id.btn_save);
        Button cancal = (Button) conentView.findViewById(R.id.btn_cancle);
        save.setOnClickListener(this);
        cancal.setOnClickListener(this);
    }

    public void init() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        //this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
     //   this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_cancle) {
            dismiss();
        } else if (viewId == R.id.btn_save) {
            File file = FileUtil.createRecordFile("jpg");
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(context, file.getAbsoluteFile()+"", Toast.LENGTH_SHORT).show();
                dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
