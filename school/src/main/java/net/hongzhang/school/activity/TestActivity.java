package net.hongzhang.school.activity;

import android.app.Activity;
import android.os.Bundle;

import net.hongzhang.school.R;

/**
 * Created by wanghua on 2017/1/9
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // G.setTranslucent(this);
        setContentView(R.layout.activity_test);
    }
}
