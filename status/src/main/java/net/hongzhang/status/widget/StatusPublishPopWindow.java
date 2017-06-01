package net.hongzhang.status.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.widget.CommonPubishPopWindow;
import net.hongzhang.status.R;
import net.hongzhang.status.activity.CaptureVideoActivity;
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
        int viewId = view.getId();
        Intent intent =null;
        if (viewId==R.id.iv_text){
            intent = new Intent(context, PublishStatusActivity.class);
            intent.putExtra("type",WORDS);
            intent.putExtra("groupId",classId);
            MobclickAgent.onEvent(context, "releaseTextDynamic");
        }else if (viewId== R.id.iv_photo){
            intent = new Intent(context, PublishStatusActivity.class);
            intent.putExtra("type",PICTURE);
            intent.putExtra("groupId",classId);
            MobclickAgent.onEvent(context, "releasePhotoDynamic");
        }else if (viewId== R.id.iv_movie){
            intent = new Intent(context, CaptureVideoActivity.class);
            intent.putExtra("type",VEDIO);
            intent.putExtra("groupId",classId);
            MobclickAgent.onEvent(context, "releaseVideoDynamic");
        }
        context.startActivity(intent);
        dismiss();
    }
}
