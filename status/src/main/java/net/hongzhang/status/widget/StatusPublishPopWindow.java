package net.hongzhang.status.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import net.hongzhang.baselibrary.widget.CommonPubishPopWindow;
import net.hongzhang.status.R;
import net.hongzhang.status.activity.PublishStatusActivity;

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
    /**
     *
     */
    public static final int COURSE=4;
    private Activity context;
    private String classId;
    public StatusPublishPopWindow(Activity context,String classId) {
        super(context);
        this.context = context;
        this.classId = classId;
        init();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(context, PublishStatusActivity.class);
        if (view.getId()== R.id.iv_text){
            intent.putExtra("type",WORDS);
            intent.putExtra("groupId",classId);
        }else if (view.getId()==  R.id.iv_photo){
           // intent.setClass(context,)
            intent.putExtra("type",PICTURE);
            intent.putExtra("groupId",classId);
        }/*else if (view.getId()==  R.id.iv_move){
            intent.putExtra("type",VEDIO);
        }*/
        context.startActivity(intent);
        dismiss();
    }
}
