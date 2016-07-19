package net.hunme.status.activity;

import android.os.Bundle;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.status.R;

public class ChoosePhotoAvtivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_avtivity);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("选择照片");
        setSubTitle("取消");
    }
}
