package net.hunme.school.activity;

import android.os.Bundle;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/18
 * 名称：食谱
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class FoodListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
    }

    @Override
    protected void setToolBar() {
      setLiftImage(R.mipmap.ic_arrow_lift);
      setLiftOnClickClose();
      setCententTitle("食谱");

    }
}
