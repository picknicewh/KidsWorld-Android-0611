package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.school.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/18
 * 名称：请假
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveActivity extends BaseActivity {

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
        setCententTitle("请假");
       setSubTitle("我要请假");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LeaveActivity.this,LeaveAskActivity.class);
                startActivity(intent);
            }
        });
    }
}
