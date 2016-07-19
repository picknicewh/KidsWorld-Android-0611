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
        init();
        setConent();
    }
    private void setConent(){
        tv_poptext1.setText("学校通知");
        tv_poptext2.setText("班级通知");
        tv_poptext3.setText("老师通知");
        iv_popimage1.setOnClickListener(this);
        iv_popimage2.setOnClickListener(this);
        iv_popimage3.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId()== net.hunme.baselibrary.R.id.iv_popimage1){

        }else if (view.getId()== net.hunme.baselibrary.R.id.iv_popimage2){

        }else if (view.getId()== net.hunme.baselibrary.R.id.iv_popimage3){

        }
    }
}
