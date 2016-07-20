package net.hunme.school.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;
import net.hunme.school.widget.SchoolPublishPopWindow;

/**
 * 作者： wh
 * 时间： 2016/7/18
 * 名称：通知
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class InformActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
    }

    @Override
    protected void setToolBar() {
         setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
       });
        setCententTitle("通知");
        setSubTitle("发通知");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SchoolPublishPopWindow pubishPopWindow = new SchoolPublishPopWindow(InformActivity.this);
                pubishPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                pubishPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            pubishPopWindow.dismiss();
                        }
                    }
                });
            }
        });
    }
}
