package net.hunme.status.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import net.hunme.baselibrary.widget.CommonPubishPopWindow;
import net.hunme.status.R;
import net.hunme.status.activity.PublishStatusActivity;

/**
 * 作者： Administrator
 * 时间： 2016/7/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusPublishPopWindow extends CommonPubishPopWindow {
     /**
     * 文字
     */
     public  static  final int   WORDS = -1;
    /**
     * 图片
     */
    public  static  final int   PICTURE = 0;
    /**
     * 视频
     */
    public  static  final int   VEDIO = 1;
    private Activity context;
    public StatusPublishPopWindow(Activity context) {
        super(context);
        this.context = context;
        init();
        setConent();
    }
    private void setConent(){
        tv_poptext1.setText("文字");
        tv_poptext2.setText("相册");
        tv_poptext3.setText("视频");
        iv_popimage1.setOnClickListener(this);
        iv_popimage2.setOnClickListener(this);
        iv_popimage3.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(context, PublishStatusActivity.class);
        if (view.getId()== R.id.iv_popimage1){
            intent.putExtra("type",WORDS);
        }else if (view.getId()==  R.id.iv_popimage2){
           // intent.setClass(context,)
            intent.putExtra("type",PICTURE);
        }else if (view.getId()==  R.id.iv_popimage3){
            intent.putExtra("type",VEDIO);
        }
        context.startActivity(intent);
        dismiss();
    }
}
