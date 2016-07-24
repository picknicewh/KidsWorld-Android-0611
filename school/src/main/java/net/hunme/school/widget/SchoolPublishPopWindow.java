package net.hunme.school.widget;

import android.app.Activity;
import android.view.View;

import net.hunme.baselibrary.widget.CommonPubishPopWindow;

/**
 * 作者： Administrator
 * 时间： 2016/7/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SchoolPublishPopWindow extends CommonPubishPopWindow {

    private Activity context;
    public SchoolPublishPopWindow(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== net.hunme.baselibrary.R.id.iv_text){

        }else if (view.getId()== net.hunme.baselibrary.R.id.iv_photo){

        }else if (view.getId()== net.hunme.baselibrary.R.id.iv_move){

        }
    }
}
