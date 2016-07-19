package net.hunme.user.activity;

import android.os.Bundle;
import android.view.View;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户收藏
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setCententTitle("我的收藏");
        setSubTitle("搜索");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
