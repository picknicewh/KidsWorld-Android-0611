package net.hongzhang.school.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.hongzhang.school.R;

/**
 * Created by wanghua on 2017/1/9
 */
public class TestActivity extends Activity {
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
      /*  tv_time = (TextView) findViewById(R.id.tv_time);
        //时间选择器
        TimePickerView pvTime = DateUtil.getTimePickerView(this, new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {

            }
        });
        pvTime.show();*/
    }


}
