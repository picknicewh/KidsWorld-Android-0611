package net.hunme.message.activity;

import android.os.Bundle;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.message.R;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯首页--班级
 * 版本说明：
 * 附加注释：通过服务端传递过来的班级列表，进行群聊
 * 主要接口：
 */
public class ClassActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
    }

    @Override
    protected void setToolBar() {

    }
}
